package lijuce.rpc.server;

import lijuce.rpc.common.protocal.MessageProtocol;
import lijuce.rpc.common.protocal.MyRequest;
import lijuce.rpc.common.protocal.MyResponse;
import lijuce.rpc.common.protocal.MyStatus;
import lijuce.rpc.server.register.ServiceObject;
import lijuce.rpc.server.register.ServiceRegister;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 请求处理者，提供解组请求、编组响应等操作
 */
public class RequestHandle {
    private MessageProtocol protocol;

    private ServiceRegister serviceRegister;

    public RequestHandle(MessageProtocol protocol, ServiceRegister serviceRegister) {
        this.protocol = protocol;
        this.serviceRegister = serviceRegister;
    }

    public MessageProtocol getProtocol() {
        return protocol;
    }

    public void setProtocol(MessageProtocol protocol) {
        this.protocol = protocol;
    }

    public ServiceRegister getServiceRegister() {
        return serviceRegister;
    }

    public void setServiceRegister(ServiceRegister serviceRegister) {
        this.serviceRegister = serviceRegister;
    }

    public byte[] handleRequest(byte[] reqData) throws Exception {
        // 1、解组(请求)消息
        MyRequest request = this.protocol.unmarshallingRequest(reqData);
        // 2、查找服务对象
        ServiceObject serviceObject = this.serviceRegister.getServiceObject(request.getServiceName());

        MyResponse response = null;
        if (serviceObject == null){
            // 服务不存在的异常抛出
            System.out.println("服务不存在。。。");
            response = new MyResponse(MyStatus.NOT_FOUND);
        }else {
            // 3、反射调用对应的过程方法
            try {
                Method method = serviceObject.getaClass().getMethod(request.getMethod(), request.getParameterTypes());
                Object returnValue = method.invoke(serviceObject.getObj(), request.getParameters());
                response = new MyResponse(MyStatus.SUCCESS);
                response.setReturnValue(returnValue);
            }catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e){
                // 反射调用失败，直接抛出异常错误
                response = new MyResponse(MyStatus.ERROR);
                response.setException(e);
            }
        }
        // 4、编组响应信息
        return this.protocol.marshallingResponse(response);
    }
}
