package lijuce.rpc.client.balance;

import lijuce.rpc.common.Service;

import java.util.List;
import java.util.Random;

/**
 * @ClassName RandomBalance
 * @Description 随机负载均衡-随机选择一个服务提供者（软负载均衡）
 * @Author Lijuce_K
 * @Date 2021/7/24 0024 14:34
 * @Version 1.0
 **/
public class RandomBalance implements LoadBalance{
    private static Random random = new Random();

    @Override
    public Service chooseOne(List<Service> services) {
        return services.get(random.nextInt(services.size()));
    }
}
