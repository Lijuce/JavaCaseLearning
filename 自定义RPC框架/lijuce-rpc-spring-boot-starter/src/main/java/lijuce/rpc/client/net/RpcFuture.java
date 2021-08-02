package lijuce.rpc.client.net;

import java.util.concurrent.*;

/**
 * @ClassName RpcFuture
 * @Description TODO
 * @Author Lijuce_K
 * @Date 2021/7/29 0029 21:09
 * @Version 1.0
 **/
public class RpcFuture<T> implements Future {

    private T response;

    /**
     * 因为请求和响应是一一对应的，所以这里是1
     */
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        if (response != null) {
            return true;
        }
        return false;
    }

    /**
     * 等待数据读取(计算)完成，数据响应
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @Override
    public T get() throws InterruptedException, ExecutionException {
        countDownLatch.await();
        return response;
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (countDownLatch.await(timeout, unit)) {
            return response;
        }
        return null;
    }

    public void setResponse(T response) {
        this.response = response;
        countDownLatch.countDown();
    }

}
