package lijuce.rpc.client.net.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lijuce.rpc.client.net.NettyNetClient;
import lijuce.rpc.client.net.RpcFuture;
import lijuce.rpc.common.protocal.MessageProtocol;
import lijuce.rpc.common.protocal.MyRequest;
import lijuce.rpc.common.protocal.MyResponse;
import lijuce.rpc.exception.rpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName SendHandlerV2
 * @Description 发送处理类-升级版：附增长连接机制
 * @Author Lijuce_K
 * @Date 2021/7/29 0029 20:43
 * @Version 1.0
 **/
public class SendHandlerV2 extends ChannelInboundHandlerAdapter {
    private static Logger logger = LoggerFactory.getLogger(SendHandlerV2.class);

    private MessageProtocol messageProtocol;

    private String remoteAddress;

    /**
     * CD锁
     */
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    private volatile Channel channel;

    private static Map<String, RpcFuture<MyResponse>> requestMap = new ConcurrentHashMap<>();

    public SendHandlerV2(MessageProtocol messageProtocol, String remoteAddress) {
        this.messageProtocol = messageProtocol;
        this.remoteAddress = remoteAddress;
    }

    public SendHandlerV2(MessageProtocol messageProtocol, CountDownLatch countDownLatch) {
        this.messageProtocol = messageProtocol;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        this.channel = ctx.channel();
        countDownLatch.countDown();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("Connect to server successfully:{}", ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        logger.error("channel inactive with remoteAddress:[{}]", remoteAddress);
        NettyNetClient.connectedServerNodes.remove(remoteAddress);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.debug("Client reads message:{}", msg);
        // 创建读取缓冲区
        ByteBuf msgBuf= (ByteBuf) msg;
        byte[] respData = new byte[msgBuf.readableBytes()];
        // 将读取的数据放入缓冲区
        msgBuf.readBytes(respData);
        // 手动回收缓冲区
        ReferenceCountUtil.release(msgBuf);
        MyResponse response = messageProtocol.unmarshallingResponse(respData);
        RpcFuture<MyResponse> future = requestMap.get(response.getRequestId());
        future.setResponse(response);
    }

    /**
     * 待所有数据读取至缓冲区后，刷新，正式读入队列
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        logger.error("Exception occurred:{}", cause.getMessage());
        ctx.close();
    }

    /**
     * 等待通道建立最大时间
     */
    static final int CHANNEL_WAIT_TIME = 4;
    /**
     * 等待响应最大时间
     */
    static final int RESPONSE_WAIT_TIME = 8;

    /**
     * 发送请求的处理方法，添加了长连接机制
     * @param request
     * @return
     */
    public MyResponse sendRequest(MyRequest request) {
        MyResponse response;
        // 创建新连接，存入缓存池中
        RpcFuture<MyResponse> future = new RpcFuture<>();
        requestMap.put(request.getRequestId(), future);
        try {
            byte[] data = messageProtocol.marshallingRequest(request);
            // 创建写入缓冲区
            ByteBuf reqBuf = Unpooled.buffer(data.length);
            // 将待发送的数据写入缓冲区
            reqBuf.writeBytes(data);
            if (countDownLatch.await(CHANNEL_WAIT_TIME, TimeUnit.SECONDS)) {
                channel.writeAndFlush(reqBuf);
                // 等待获取响应
                response = (MyResponse) future.get(RESPONSE_WAIT_TIME, TimeUnit.SECONDS);
            } else {
                throw new rpcException("建立通道超时");
            }
        } catch (Exception e) {
            throw  new rpcException(e.getMessage());
        } finally {
            requestMap.remove(request.getRequestId());
        }
        return response;
    }
}
