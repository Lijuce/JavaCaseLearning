package lijuce.rpc.server.register;

import com.alibaba.fastjson.JSON;
import lijuce.rpc.common.Service;
import lijuce.rpc.common.constants.Constants;
import lijuce.rpc.common.serializer.ZookeeperSerializer;
import org.I0Itec.zkclient.ZkClient;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;

/**
 * Zookeeper服务注册器，提供服务注册、服务暴露的能力
 * 将指定ServiceObject对象序列化后保存到ZK上，供客户端发现。同时会将服务对象缓存起来，
 * 在客户端调用服务时，通过缓存的ServiceObject对象反射指定服务，调用方法。
 */
public class ZookeeperExportServiceRegister extends DefaultServiceRegister implements ServiceRegister{
    private ZkClient client;

    public ZookeeperExportServiceRegister(String zkAddress, Integer port, String protocol) {
        client = new ZkClient(zkAddress);
        client.setZkSerializer(new ZookeeperSerializer());
        this.port = port;
        this.protocol = protocol;
    }

    /**
     * 服务注册
     * @param so
     */
    @Override
    public void register(ServiceObject so) throws Exception {
        super.register(so);  // 调用父类默认方法将服务对象注册进服务列表中
        // 以下为了将现有服务暴露，进行服务构建
        Service service = new Service();
        String host = InetAddress.getLocalHost().getHostAddress();  // 地址
        String address = host + ":" + port;  // 地址端口
        System.out.println("地址" + address);
        service.setAddress(address);
        service.setName(so.getaClass().getName());
        service.setProtocol(protocol);
        System.out.println("整个服务的信息"+ service);
        this.exportService(service);  // 将服务进行暴露
    }

    /**
     * 暴露服务操作
     * @param serviceResource 待暴露的服务信息
     */
    private void exportService(Service serviceResource){
        String serviceName = serviceResource.getName();  // 服务名
        String url = JSON.toJSONString(serviceResource);  // 将服务对象转化为json字符串格式
        try {
            url = URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String servicePath = Constants.ZK_SERVICE_PATH + "/" + serviceName + "/service";  // 服务类的所在路径
        System.out.println("服务类的全路径" + servicePath);
        if (!client.exists(servicePath))  // 若节点不存在，则递归创建（即父节点也会进行创建）
            client.createPersistent(servicePath, true);
        String urlPath = servicePath + "/" + url;
        if (client.exists(urlPath)){
            // 删除节点
            System.out.println("删除节点");
            client.delete(urlPath);
        }
        System.out.println("节点完整信息" + urlPath);
        client.createEphemeral(urlPath);
    }

}
