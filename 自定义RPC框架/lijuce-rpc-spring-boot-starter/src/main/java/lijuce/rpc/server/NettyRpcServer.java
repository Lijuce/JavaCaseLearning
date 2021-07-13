package lijuce.rpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Netty RPC服务端，提供Netty网络服务开启、关闭的能力
 */
public class NettyRpcServer extends RpcServer{

    private static Logger logger = LoggerFactory.getLogger(NettyRpcServer.class);

    private Channel channel;

    public NettyRpcServer(int port, String protocal, RequestHandle handle) {
        super(port, protocal, handle);
    }

    @Override
    public void start() {
        // 配置服务器(服务端)
        // TODO(这一步很多操作不懂。。。)
        // 1.bossGroup 用于接收连接，workerGroup 用于具体的处理
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 2. 创建服务端启动引导/辅助类serverBootstrap
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 3. 给引导类配置两大线程组,确定了线程模型
            serverBootstrap.group(bossGroup, workerGroup)
                    // 4. 指定I/O模型
                    .channel(NioServerSocketChannel.class)
                    // ChannelOption.SO_BACKLOG对应的是tcp/ip协议
                    // backlog 用于构造服务端套接字ServerSocket对象
                    // 标识当服务器请求处理线程全满时，用于临时存放已完成三次握手的请求的队列的最大长度。
                    .option(ChannelOption.SO_BACKLOG, 100)
                    // 对bossGroup线程组起作用
                    .handler(new LoggingHandler(LogLevel.INFO))
                    // 对workerGroup线程组起作用
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel){
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 5. 可以自定义客户端消息的业务处理逻辑
                            pipeline.addLast(new ChannelRequestHandler());
                        }
                });

            // 启动服务
            // 6. 绑定端口,调用 sync 方法阻塞知道绑定完成
            ChannelFuture syncF = serverBootstrap.bind(port).sync();
            logger.info("Server started successfully.");
            channel = syncF.channel();
            // 等待服务通道关闭
            // 7. 阻塞等待直到服务器Channel关闭(closeFuture()方法获取Channel 的CloseFuture对象,然后调用sync()方法)
            syncF.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            // 释放线程组资源
            // 8.优雅关闭相关线程组资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    @Override
    public void stop() {
        this.channel.close();
    }

    /**
     * ChannelRequestHandler处理消息
     * 重写两个重要方法：
     *      channelRead() ：服务端接收客户端数据所调用的方法
     *      exceptionCaught() ：处理客户端消息发生异常的时候被调用
     */
    private class ChannelRequestHandler extends ChannelInboundHandlerAdapter{
        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            logger.info("Channel active：{}", ctx);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            logger.info("The server receives a message: {}", msg);
            // 在发送数据前，得转化为字节流格式
            ByteBuf msgBuf = (ByteBuf) msg;  //
            byte[] req = new byte[msgBuf.readableBytes()];
            msgBuf.readBytes(req);
            byte[] resBytes = handle.handleRequest(req);
            logger.info("Send response：{}", msg);
            // 创建缓冲区
            ByteBuf respBuf = Unpooled.buffer(resBytes.length);
            // 将response数据写入缓冲区，待发送回给客户端
            respBuf.writeBytes(resBytes);
            ctx.write(respBuf);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            // 读完后刷新缓存队列
            ctx.flush();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            logger.error("Exception occurred：{}", cause.getMessage());
            ctx.close();
        }
    }
}
