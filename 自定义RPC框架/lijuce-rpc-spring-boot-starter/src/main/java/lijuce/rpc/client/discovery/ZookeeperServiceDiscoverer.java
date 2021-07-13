package lijuce.rpc.client.discovery;

import com.alibaba.fastjson.JSON;
import lijuce.rpc.common.Service;
import lijuce.rpc.common.constants.Constants;
import lijuce.rpc.common.serializer.ZookeeperSerializer;
import org.I0Itec.zkclient.ZkClient;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ZookeeperServiceDiscoverer implements ServiceDiscover{
    private ZkClient zkClient;  // 注册中心

    public ZookeeperServiceDiscoverer(String zkAddress) {
        zkClient = new ZkClient(zkAddress);  // 给定Zookeeper地址，进行构建
        // TODO: ZookeeperSerializer类的实现
        zkClient.setZkSerializer(new ZookeeperSerializer());
    }

    /**
     * 通过服务类名获取服务列表
     * 服务名路径：接口全路径
     * @param name 服务类名
     * @return 服务列表
     */
    @Override
    public List<Service> getServices(String name) {
        // 获取服务类的全路径。该服务类的路径必须和server端的服务类路径保持一致
        String servicePath = Constants.ZK_SERVICE_PATH + "/" + name + "/service";
//        System.out.println("获取服务类的全路径" + servicePath);
        // 入口：获取服务，并将其装入列表待处理
        List<String> children = zkClient.getChildren(servicePath);
        // Optional.ofNullable避免显示判断null值，优雅且可读性高
        return Optional.ofNullable(children).orElse(new ArrayList<>()).stream().map(str -> {
            String deCh = null;
            try {
                // 将获取的服务进行解码
                deCh = URLDecoder.decode(str, Constants.UTF_8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return JSON.parseObject(deCh, Service.class);
        }).collect(Collectors.toList());
    }
}
