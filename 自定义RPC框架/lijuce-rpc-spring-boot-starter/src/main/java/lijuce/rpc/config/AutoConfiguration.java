package lijuce.rpc.config;

import lijuce.rpc.annotation.LoadBalanceAno;
import lijuce.rpc.annotation.MessageProtocolAno;
import lijuce.rpc.client.ClientProxyFactory;
import lijuce.rpc.client.balance.LoadBalance;
import lijuce.rpc.client.discovery.ZookeeperServiceDiscoverer;
import lijuce.rpc.client.net.NettyNetClient;
import lijuce.rpc.common.protocal.JavaSerializeMessageProtocol;
import lijuce.rpc.common.protocal.MessageProtocol;
import lijuce.rpc.common.protocal.protoUtils.SerializeMessageJdk;
import lijuce.rpc.exception.rpcException;
import lijuce.rpc.properties.RpcProperty;
import lijuce.rpc.server.NettyRpcServer;
import lijuce.rpc.server.RequestHandle;
import lijuce.rpc.server.RpcServer;
import lijuce.rpc.server.register.DefaultRpcProcessor;
import lijuce.rpc.server.register.ServiceRegister;
import lijuce.rpc.server.register.ZookeeperExportServiceRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * Spring boot 自动配置类
 * 用于将自定义包打包成依赖所需的配置类
 */
@Configuration
@EnableConfigurationProperties(RpcProperty.class)
public class AutoConfiguration {

    @Bean
    public RpcProperty rpcProperty(){
        return new RpcProperty();
    }

    @Bean
    public DefaultRpcProcessor defaultRpcProcessor() {
        return new DefaultRpcProcessor();
    }

    @Bean
    public ClientProxyFactory clientProxyFactory(@Autowired RpcProperty rpcProperty) {
        ClientProxyFactory clientProxyFactory = new ClientProxyFactory();
        // 设置服务发现者
        clientProxyFactory.setServiceDiscover(new ZookeeperServiceDiscoverer(rpcProperty.getRegisterAddress()));

        // 设置支持的协议
        Map<String, MessageProtocol> supportMessageProtocols = new HashMap<>();
        supportMessageProtocols.put("protoStuff", new JavaSerializeMessageProtocol());
        supportMessageProtocols.put("javaJdk", new SerializeMessageJdk());
        clientProxyFactory.setMessageProtocol(supportMessageProtocols);
        clientProxyFactory.setLoadBalance(getLoadBalance(rpcProperty.getLoadBalance()));
        // 设置网络层实现
        clientProxyFactory.setNetClient(new NettyNetClient());
        return clientProxyFactory;
    }

    @Bean
    public ServiceRegister serviceRegister(@Autowired RpcProperty rpcProperty) {
        return new ZookeeperExportServiceRegister(
                rpcProperty.getRegisterAddress(),
                rpcProperty.getServerPort(),
                rpcProperty.getProtocol());
    }

    @Bean
    public RequestHandle requestHandler(@Autowired ServiceRegister serviceRegister,
                                        @Autowired RpcProperty rpcProperty) {
        return new RequestHandle(getMessageProtocol(rpcProperty.getProtocol()), serviceRegister);
    }

    @Bean
    public RpcServer rpcServer(@Autowired RequestHandle requestHandler,
                               @Autowired RpcProperty rpcProperty) {
        return new NettyRpcServer(rpcProperty.getServerPort(),
                rpcProperty.getProtocol(), requestHandler);
    }

    @Bean
    public RpcProperty leisureRpcProperty() {
        return new RpcProperty();
    }

    /**
     * 根据配置动态获取协议类型
     * @param protocolName
     * @return
     */
    public static MessageProtocol getMessageProtocol(String protocolName){
        // 利用ServiceLoader得到已定义的类
        ServiceLoader<MessageProtocol> loader = ServiceLoader.load(MessageProtocol.class);
        Iterator<MessageProtocol> iterator = loader.iterator();
        while (iterator.hasNext()) {
            MessageProtocol messageProtocol = iterator.next();
            MessageProtocolAno annotation = messageProtocol.getClass().getAnnotation(MessageProtocolAno.class);
            // 消息协议空异常
            Assert.notNull(annotation, "protocal can not be empty");
            System.out.println("annotation.value() " + annotation.value());
            if (protocolName.equals(annotation.value())) {
                return messageProtocol;
            }
        }
        // 未能匹配有效协议，手动抛出异常
        throw new rpcException("invalid protocal found!!!");
    }

    public static LoadBalance getLoadBalance(String loadBalanceType) {
        ServiceLoader<LoadBalance> loader = ServiceLoader.load(LoadBalance.class);
        Iterator<LoadBalance> iterator = loader.iterator();
        while (iterator.hasNext()) {
            LoadBalance loadBalance = iterator.next();
            LoadBalanceAno annotation = loadBalance.getClass().getAnnotation(LoadBalanceAno.class);
            if (loadBalanceType.equals(annotation.value())) {
                return loadBalance;
            }
        }
        // 未能匹配有效负载均衡类型，手动抛出异常
        throw new rpcException("invalid LoadBalance found!!!");
    }
}
