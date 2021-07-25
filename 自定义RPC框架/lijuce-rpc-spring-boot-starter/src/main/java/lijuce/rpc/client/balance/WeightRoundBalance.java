package lijuce.rpc.client.balance;

import lijuce.rpc.annotation.LoadBalanceAno;
import lijuce.rpc.common.Service;

import java.util.List;

/**
 * @ClassName WeightRoundBalance
 * @Description TODO
 * @Author Lijuce_K
 * @Date 2021/7/25 0025 14:59
 * @Version 1.0
 **/
@LoadBalanceAno("weightRound")
public class WeightRoundBalance implements LoadBalance{

    private static int index;

    @Override
    public Service chooseOne(List<Service> services) {
        int sumWeight = services.stream().mapToInt(Service::getWeight).sum();
        int number = (index++) % sumWeight;
        for(Service service: services) {
            if (service.getWeight() > number) {
                return service;
            }
            number -= service.getWeight();
        }
        return null;
    }
}
