package lijuce.rpc.client.discovery;

import lijuce.rpc.common.Service;

import java.util.List;

/**
 * 服务发现抽象类
 */
public interface ServiceDiscover {
    List<Service> getServices(String name);
}
