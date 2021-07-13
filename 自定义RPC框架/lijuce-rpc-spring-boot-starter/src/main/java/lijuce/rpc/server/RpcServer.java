package lijuce.rpc.server;

/**
 * RPC服务端抽象类
 */
public abstract class RpcServer {
    /**
     * 服务端口
     */
    protected int port;

    /**
     * 服务协议
     */
    protected String protocol;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public RequestHandle getHandle() {
        return handle;
    }

    public void setHandle(RequestHandle handle) {
        this.handle = handle;
    }

    /**
     * 请求处理者
     */
    protected RequestHandle handle;

    public RpcServer(int port, String protocal, RequestHandle handle) {
        super();
        this.port = port;
        this.protocol = protocal;
        this.handle = handle;
    }

    /**
     * 开启服务
     */
    public abstract void start();

    /**
     * 停止服务
     */
    public abstract void stop();
}
