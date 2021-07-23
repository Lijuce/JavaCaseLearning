package lijuce.rpc.common.protocal.protoUtils;

import com.alibaba.fastjson.JSON;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import lijuce.rpc.annotation.MessageProtocolAno;
import lijuce.rpc.common.protocal.MessageProtocol;
import lijuce.rpc.common.protocal.MyRequest;
import lijuce.rpc.common.protocal.MyResponse;

import java.io.*;

/**
 * @ClassName SerializeMessageJDK
 * @Description 采用JDK原生序列化机制
 * @Author Lijuce_K
 * @Date 2021/7/23 14:21
 * @Version 1.0
 **/
@MessageProtocolAno("javaJdk")
public class SerializeMessageJdk implements MessageProtocol {

    /**
     * 序列化
     * @param obj
     * @return
     * @throws IOException
     */
    private byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bout);
        out.writeObject(obj);
        return bout.toByteArray();
    }

    /**
     * 反序列化
     * @param data
     * @return 反序列化后的对象
     * @throws Exception
     */
    private Object unSerialize(byte[] data) throws Exception {
        ObjectInputStream oin = new ObjectInputStream(new ByteArrayInputStream(data));
        return oin.readObject();
    }

    @Override
    public byte[] marshallingRequest(MyRequest req) throws Exception {
        return serialize(req);
    }

    @Override
    public MyRequest unmarshallingRequest(byte[] data) throws Exception {
        return (MyRequest) unSerialize(data);
    }

    @Override
    public byte[] marshallingResponse(MyResponse rsp) throws Exception {
        return serialize(rsp);
    }

    @Override
    public MyResponse unmarshallingResponse(byte[] data) throws Exception {
        return (MyResponse) unSerialize(data);
    }
}
