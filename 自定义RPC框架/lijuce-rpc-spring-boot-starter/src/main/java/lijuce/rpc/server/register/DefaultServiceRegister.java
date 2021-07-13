package lijuce.rpc.server.register;

import java.util.HashMap;
import java.util.Map;

/**
 * 默认服务注册器
 */
public class DefaultServiceRegister implements ServiceRegister {
    /**
     * 不同服务对应各自的服务对象，用hashmap存储
     */
    private Map<String, ServiceObject> serviceMap = new HashMap<>();
    protected String protocol;
    protected Integer port;

    @Override
    public void register(ServiceObject so) throws Exception {
        if (so == null){
            // 服务对象为空时要异常处理
            throw new IllegalArgumentException("Parameter cannot be empty.");
        }
        this.serviceMap.put(so.getName(), so);
    }

    @Override
    public ServiceObject getServiceObject(String name) {
        return this.serviceMap.get(name);
    }
}
