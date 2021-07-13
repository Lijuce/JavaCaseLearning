package lijuce.rpc.common;

import java.io.Serializable;

public class Service implements Serializable {
    /**
     * 服务名称
     */
    private String name;

    /**
     * 消息协议类型
     */
    private String protocol;

    /**
     * 服务地址，格式：IP：Port
     */
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Service{" +
                "name='" + name + '\'' +
                ", protocol='" + protocol + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
