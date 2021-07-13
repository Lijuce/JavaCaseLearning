package lijuce.rpc.common.protocal;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MyRequest implements Serializable {
    private static final long serialVersionUID = -5200571424236772650L;

    /**
     * 服务名
     */
    private String serviceName;

    /**
     * 服务方法
     */
    private String method;

    /**
     * 请求头
     */
    private Map<String, String> headers = new HashMap<>();

    private Class<?>[] parameterTypes;

    private Object[] parameters;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
    public String getHeaders(String name) {
        return this.headers == null? null: this.headers.get(name);
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }
}
