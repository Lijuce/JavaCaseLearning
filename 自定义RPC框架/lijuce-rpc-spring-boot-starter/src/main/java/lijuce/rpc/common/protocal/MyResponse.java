package lijuce.rpc.common.protocal;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MyResponse implements Serializable {
    public MyResponse(MyStatus myStatus) {
        this.myStatus = myStatus;
    }

    private static final long serialVersionUID = -4317845782629589997L;

    /**
     * 响应头信息
     */
    private Map<String, String> headers = new HashMap<>();

    /**
     * ？？
     */
    private Object returnValue;

    /**
     * 响应状态码
     */
    private MyStatus myStatus;

    /**
     * 异常类型
     */
    private Exception exception;

    public MyStatus getMyStatus() {
        return myStatus;
    }

    public void setMyStatus(MyStatus myStatus) {
        this.myStatus = myStatus;
    }


    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Object getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
