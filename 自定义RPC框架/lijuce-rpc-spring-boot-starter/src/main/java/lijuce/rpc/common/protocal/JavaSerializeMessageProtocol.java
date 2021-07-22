package lijuce.rpc.common.protocal;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import java.io.*;

/**
 * 序列化消息协议
 * @author Lijuce
 */
public class JavaSerializeMessageProtocol implements MessageProtocol{

    /**
     * 序列化
     * @param obj
     * @return
     */
    public static <T> byte[] serialize(T obj){
        Schema<T> schema = RuntimeSchema.getSchema((Class<T>) obj.getClass());
        LinkedBuffer buffer = LinkedBuffer.allocate(512);
        final byte[] result;
        try {
            result = ProtobufIOUtil.toByteArray(obj, schema, buffer);
        } finally {
            buffer.clear();
        }
        return result;
    }

    /**
     * 反序列化
     * @param protoStuff
     * @return
     */
    private static <T> T unSerialize(byte[] protoStuff, Class<T> clazz){
        Schema<T> schema = RuntimeSchema.getSchema(clazz);
        T obj = schema.newMessage();
        ProtobufIOUtil.mergeFrom(protoStuff, obj, schema);
        return obj;
    }

    @Override
    public byte[] marshallingRequest(MyRequest req){
        return serialize(req);
    }

    @Override
    public MyRequest unmarshallingRequest(byte[] data) throws Exception {
        return unSerialize(data, MyRequest.class);
    }

    @Override
    public byte[] marshallingResponse(MyResponse rsp) throws Exception {
        return serialize(rsp);
    }

    @Override
    public MyResponse unmarshallingResponse(byte[] data) throws Exception {
        return unSerialize(data, MyResponse.class);
    }
}
