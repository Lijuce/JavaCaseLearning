package lijuce.rpc.client.cache;

import lijuce.rpc.common.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ServerDiscoveryCache
 * @Description TODO
 * @Author Lijuce_K
 * @Date 2021/7/25 0025 16:03
 * @Version 1.0
 **/
public class ServerDiscoveryCache {
    private static final Map<String, List<Service>> ServerMap = new HashMap<>();

    public static final List<String> ServiceClassNames = new ArrayList<>();

    public static void put(String serviceName, List<Service> serviceList) {
        ServerMap.put(serviceName, serviceList);
    }

    /**
     * 根据服务名称获取缓存列表
     * @param serviceName
     * @return
     */
    public static List<Service> get(String serviceName) {
        return ServerMap.get(serviceName);
    }

    /**
     * 判断服务缓存列表是否为空
     * @param serviceName
     * @return
     */
    public static Boolean isEmpty(String serviceName) {
        return !ServerMap.containsKey(serviceName) || ServerMap.get(serviceName).size() == 0;
    }

}
