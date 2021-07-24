package lijuce.rpc.client.balance;

import lijuce.rpc.common.Service;

import java.util.List;

/**
 * 负载均衡算法接口
 */
public interface LoadBalance {
    Service chooseOne(List<Service> services);
}
