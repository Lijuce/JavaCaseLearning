package lijuce.rpc.client.net;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.channel.*;
import lijuce.rpc.client.net.handler.SendHandler;
import lijuce.rpc.client.net.handler.SendHandlerV2;
import lijuce.rpc.common.Service;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lijuce.rpc.common.protocal.MessageProtocol;
import lijuce.rpc.common.protocal.MyRequest;
import lijuce.rpc.common.protocal.MyResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.*;

public class NettyNetClient implements NetClient {
    // 指定类初始化日志对象，在日志输出的时候，可以打印出日志信息所在类
    private static Logger logger = LoggerFactory.getLogger(NettyNetClient.class);

    /**
     * 网络连接缓存池（长连接的重要对象）
     */
    public static Map<String, SendHandlerV2> connectedServerNodes = new ConcurrentHashMap<>();

    /**
     * TCP高并发情况下的线程池
     */
    private ExecutorService threadPool = new ThreadPoolExecutor(
            4,
            10,
            200,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(1000),
            new ThreadFactoryBuilder().setNameFormat("rpcClient-%d").build());

    @Override
    public byte[] sendRequest(byte[] data, Service service)  {
        // 获取服务端完整地址
        String[] addInfoArray = service.getAddress().split(":");
        // 获取服务端地址前部分URL
        String serverAddress = addInfoArray[0];
        // 端口号
        String serverPort = addInfoArray[1];

        // 初始化发送数据，准备发送至服务端
        SendHandler sendHandler = new SendHandler(data);
        byte[] respData = new byte[0];
        // 配置客户端
        // 1. IO 线程组初始化
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            // 2. 创建客户端启动引导/辅助类bootstrap
            Bootstrap bootstrap = new Bootstrap();
            // 3. 指定线程模型
            bootstrap.group(group)
                    // 4. 指定I/O模型
                    .channel(NioSocketChannel.class)
                    // TCP的链接属性设置， ChannelOption.TCP_NODELAY 要求低延迟
                    .option(ChannelOption.TCP_NODELAY, true)
                    // 不同于服务端，客户端只需要一个事件线程组对事件进行处理（而没有所谓的接收连接组）
                    // 因此只需（且仅有）handler方法进行事件处理
                    .handler(new ChannelInitializer<SocketChannel>(){
                        @Override
                        public void initChannel(SocketChannel ch){
                            ChannelPipeline pipeline = ch.pipeline();
                            // 5. 自定义息的业务处理逻辑
                            pipeline.addLast(sendHandler);
                        }
                    });
            // 6. 启动客户端尝试连接，调用 sync 方法阻塞知道连接成功否
            bootstrap.connect(serverAddress, Integer.parseInt(serverPort)).sync();
            // 获取服务端返回数据
            respData = (byte[]) sendHandler.respData();
            logger.info("SendRequest get reply: {}", respData);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 释放线程组资源
            group.shutdownGracefully();
        }

        // 响应数据
        return respData;
    }

    @Override
    public MyResponse sendRequest(MyRequest myRequest, Service service, MessageProtocol messageProtocol) {
        String address = service.getAddress();
        synchronized (address) {
            // 地址本身存在缓存池中，复用该连接
            if (connectedServerNodes.containsKey(address)) {
                SendHandlerV2 sendHandler = connectedServerNodes.get(address);
                logger.info("使用现有连接");
                return sendHandler.sendRequest(myRequest);
            }

            String[] addrInfo = address.split(":");
            final String serverAddress = addrInfo[0];
            final String serverPort = addrInfo[1];
            SendHandlerV2 sendHandler = new SendHandlerV2(messageProtocol, serverAddress);

            // 配置客户端
            // 1. IO 线程组初始化
            NioEventLoopGroup group = new NioEventLoopGroup(4);

            threadPool.submit(() -> {
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(group)
                        .channel(NioSocketChannel.class)
                        .option(ChannelOption.TCP_NODELAY,true)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            public void initChannel(SocketChannel ch) {
                                ChannelPipeline pipeline = ch.pipeline();
                                pipeline.addLast(sendHandler);
                            }
                        });
                // 启动客户端尝试连接
                ChannelFuture channelFuture = bootstrap.connect(address, Integer.parseInt(serverPort));
                channelFuture.addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        connectedServerNodes.put(address, sendHandler);
                    }
                });
            });

            logger.info("使用新的连接。。。");
            return sendHandler.sendRequest(myRequest);
        }
    }
}
