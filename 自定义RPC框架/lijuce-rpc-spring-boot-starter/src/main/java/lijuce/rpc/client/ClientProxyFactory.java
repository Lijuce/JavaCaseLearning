package lijuce.rpc.client;

import lijuce.rpc.client.balance.LoadBalance;
import lijuce.rpc.client.balance.RandomBalance;
import lijuce.rpc.client.discovery.ServiceDiscover;
import lijuce.rpc.client.net.NetClient;
import lijuce.rpc.common.Service;
import lijuce.rpc.common.protocal.MessageProtocol;
import lijuce.rpc.common.protocal.MyRequest;
import lijuce.rpc.common.protocal.MyResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import static java.lang.reflect.Proxy.newProxyInstance;

/**
 * 客户端代理工厂：用于创建远程服务代理类
 * 封装编组请求、请求发送、编组响应等操作。
 */
public class ClientProxyFactory {

    private ServiceDiscover serviceDiscover;

    private NetClient netClient;

    private Map<Class<?>, Object> objectCache = new HashMap<>();

    private Map<String, MessageProtocol> messageProtocol;

    public ServiceDiscover getServiceDiscover() {
        return serviceDiscover;
    }

    public void setServiceDiscover(ServiceDiscover serviceDiscover) {
        this.serviceDiscover = serviceDiscover;
    }

    public NetClient getNetClient() {
        return netClient;
    }

    public void setNetClient(NetClient netClient) {
        this.netClient = netClient;
    }

    public Map<Class<?>, Object> getObjectCache() {
        return objectCache;
    }

    public void setObjectCache(Map<Class<?>, Object> objectCache) {
        this.objectCache = objectCache;
    }

    public Map<String, MessageProtocol> getMessageProtocol() {
        return messageProtocol;
    }

    private LoadBalance loadBalance;

    public void setMessageProtocol(Map<String, MessageProtocol> messageProtocol) {
        this.messageProtocol = messageProtocol;
    }

    // 忽略 unchecked 警告信息，如使用List，ArrayList等未进行参数化产生的
    // 警告信息。
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> tClass){
        // lambda表达式
        return (T) this.objectCache.computeIfAbsent(tClass,
                cls -> newProxyInstance(cls.getClassLoader(), new Class<?>[]{cls}, new ClientInvocationHandler(cls)));
    }

    /**
     * 客户端服务代理类invoke实现细节
     */
    private class ClientInvocationHandler implements InvocationHandler{
        private Class<?> aClass;

        private Random random = new Random();

        public ClientInvocationHandler(Class<?> aClass){
            super();
            this.aClass = aClass;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().equals("toString")){
                return proxy.getClass().toString();
            }

            if (method.getName().equals("hashCode")){
                return proxy.getClass().hashCode();
            }

            // 1. 获取服务信息
            String serviceName = this.aClass.getName();
            List<Service> services = serviceDiscover.getServices(serviceName);
            if (services == null || services.isEmpty()){
                // 获取空服务，抛出异常
                System.out.println("空服务。。。");
                throw new Exception();
            }
            // 随机选择一个服务提供者（软负载均衡）
//            Service service = services.get(random.nextInt(services.size()));
            Service service = loadBalance.chooseOne(services);

            // 2. 构造request对象
            MyRequest request = new MyRequest();
            request.setServiceName(service.getName());
            request.setMethod(method.getName());
            request.setParameterTypes(method.getParameterTypes());
            request.setParameters(args);

            // 3. 协议层编组-获得方法对应的协议
            MessageProtocol protocol = messageProtocol.get(service.getProtocol());
            // 编组请求
            byte[] data = protocol.marshallingRequest(request);

            // 4. 调用网络层发送请求(同时取得响应数据)
            byte[] respData = netClient.sendRequest(data, service);

            // 5. 解组响应消息
            MyResponse response = protocol.unmarshallingResponse(respData);

            // 6. 结果处理(空异常处理)
            if (response.getException() != null){
                throw response.getException();
            }

            return response.getReturnValue();
        }
    }

}
