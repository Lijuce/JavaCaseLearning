package lijuce.rpc.properties;

import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 参数配置类，实现用户自定义参数
 */
@EnableConfigurationProperties(RpcProperty.class)
@ConfigurationProperties("lijuce.rpc")
public class RpcProperty {
    /**
     * 服务注册中心地址
     */
    private String registerAddress = "127.0.0.1:2181";

    /**
     * 服务端暴露端口
     */
    private Integer serverPort = 19000;

    /**
     * 服务协议
     */
    private String protocol = "javaJdk";

    /**
     * 负载均衡算法
     */
    private String loadBalance = "random";

    public String getLoadBalance() {
        return loadBalance;
    }

    public void setLoadBalance(String loadBalance) {
        this.loadBalance = loadBalance;
    }

    public String getRegisterAddress() {
        return registerAddress;
    }

    public void setRegisterAddress(String registerAddress) {
        this.registerAddress = registerAddress;
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }


}
