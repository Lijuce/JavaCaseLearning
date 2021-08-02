package lijuce.rpc.client.net.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * 发送处理类，定义Netty入站处理细则，继承自ChannelInboundHandlerAdapter
 * 重写三个方法
 * channelActive：客户端和服务端的连接建立之后主动调用
 * channelRead：客户端接收服务端数据调用的方法
 * exceptionCaught：处理服务端消息发生异常的时候被调用
 *
 */
public class SendHandler extends ChannelInboundHandlerAdapter {
    private static Logger logger = LoggerFactory.getLogger(SendHandler.class);

    /**
     * CD锁
     */
    private CountDownLatch countDownLatch;

    /**
     * 读取得的数据
     */
    private Object readMsg = null;

    private byte[] data;

    public SendHandler(byte[] data) {
        countDownLatch = new CountDownLatch(1);
        this.data = data;
    }

    /**
     * 服务端成功连接，发送请求数据
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Successful connection to server: {}", ctx);
        // 创建写入缓冲区
        ByteBuf reqBuffer = Unpooled.buffer(data.length);
        // 将待发送的数据写入缓冲区
        reqBuffer.writeBytes(data);
        logger.info("Client sends message：{}", reqBuffer);
        // 写入后刷新队列
        ctx.writeAndFlush(reqBuffer);
    }

    /**
     * 读取数据，数据读取完毕释放CD锁
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("Client reads message: {}", msg);
        ByteBuf msgBuf= (ByteBuf) msg;  // 创建读取缓冲区
        byte[] respData = new byte[msgBuf.readableBytes()];
        msgBuf.readBytes(respData);  // 将读取的数据放入缓冲区
        readMsg = respData;
        countDownLatch.countDown();  // 释放CD锁
    }

    /**
     * 等待读取数据完成
     * @return
     * @throws InterruptedException
     */
    public Object respData() throws InterruptedException {
        countDownLatch.await();
        return readMsg;
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

    /**
     * 异常处理。只要出现异常，断开此连接
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        logger.error("Exception occurred：{}", cause.getMessage());
        ctx.close();
    }
}
