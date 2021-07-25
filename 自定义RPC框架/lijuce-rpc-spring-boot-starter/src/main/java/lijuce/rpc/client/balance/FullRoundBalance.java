package lijuce.rpc.client.balance;

import lijuce.rpc.annotation.LoadBalanceAno;
import lijuce.rpc.common.Service;

import java.util.List;

/**
 * @ClassName FullRoundBalance
 * @Description 负载均衡之轮询算法实现
 * @Author Lijuce_K
 * @Date 2021/7/25 0025 14:51
 * @Version 1.0
 **/
@LoadBalanceAno("fullRound")
public class FullRoundBalance implements LoadBalance{

    private int index;

    @Override
    public synchronized Service chooseOne(List<Service> services) {
        // 加锁防止多线程下的index超出services.size
        if (index == services.size()) {
            // 轮询完毕后，开始新一回的轮询
            index = 0;
        }
        return services.get(index);
    }
}
