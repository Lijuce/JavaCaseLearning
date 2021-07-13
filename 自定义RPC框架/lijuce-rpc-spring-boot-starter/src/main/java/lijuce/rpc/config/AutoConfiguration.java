package lijuce.rpc.config;

import lijuce.rpc.client.ClientProxyFactory;
import lijuce.rpc.client.discovery.ZookeeperServiceDiscoverer;
import lijuce.rpc.client.net.NettyNetClient;
import lijuce.rpc.common.protocal.JavaSerializeMessageProtocol;
import lijuce.rpc.common.protocal.MessageProtocol;
import lijuce.rpc.properties.RpcProperty;
import lijuce.rpc.server.NettyRpcServer;
import lijuce.rpc.server.RequestHandle;
import lijuce.rpc.server.RpcServer;
import lijuce.rpc.server.register.DefaultRpcProcessor;
import lijuce.rpc.server.register.ServiceRegister;
import lijuce.rpc.server.register.ZookeeperExportServiceRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Spring boot 自动配置类
 * 用于将自定义包打包成依赖所需的配置类
 */
@Configuration
public class AutoConfiguration {

    @Bean
    public DefaultRpcProcessor defaultRpcProcessor() {
        return new DefaultRpcProcessor();
    }

    @Bean
    public ClientProxyFactory clientProxyFactory(@Autowired RpcProperty leisureRpcProperty) {
        ClientProxyFactory clientProxyFactory = new ClientProxyFactory();
        // 设置服务发现者
        clientProxyFactory.setServiceDiscover(new ZookeeperServiceDiscoverer(leisureRpcProperty.getRegisterAddress()));

        // 设置支持的协议
        Map<String, MessageProtocol> supportMessageProtocols = new HashMap<>();
        supportMessageProtocols.put(leisureRpcProperty.getProtocol(), new JavaSerializeMessageProtocol());
        clientProxyFactory.setMessageProtocol(supportMessageProtocols);

        // 设置网络层实现
        clientProxyFactory.setNetClient(new NettyNetClient());
        return clientProxyFactory;
    }

    @Bean
    public ServiceRegister serviceRegister(@Autowired RpcProperty leisureRpcProperty) {
        return new ZookeeperExportServiceRegister(
                leisureRpcProperty.getRegisterAddress(),
                leisureRpcProperty.getServerPort(),
                leisureRpcProperty.getProtocol());
    }

    @Bean
    public RequestHandle requestHandler(@Autowired ServiceRegister serviceRegister) {
        return new RequestHandle(new JavaSerializeMessageProtocol(), serviceRegister);
    }

    @Bean
    public RpcServer rpcServer(@Autowired RequestHandle requestHandler,
                               @Autowired RpcProperty leisureRpcProperty) {
        return new NettyRpcServer(leisureRpcProperty.getServerPort(),
                leisureRpcProperty.getProtocol(), requestHandler);
    }

    @Bean
    public RpcProperty leisureRpcProperty() {
        return new RpcProperty();
    }
}
