package lijuce.rpc.common.protocal;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 序列化消息协议
 * @author Lijuce
 */
public class JavaSerializeMessageProtocol implements MessageProtocol{
    static Schema<Object> schema = RuntimeSchema.getSchema(Object.class);

    private byte[] serialize(Object obj) throws Exception {
        LinkedBuffer buffer = LinkedBuffer.allocate(512);
        final byte[] protoStuff;
        protoStuff = ProtobufIOUtil.toByteArray(obj, schema, buffer);
        return protoStuff;
    }

    private Object unSerialize(byte[] protoStuff){
        // 读取对象，反序列化
        Object obj = schema.newMessage();
        ProtobufIOUtil.mergeFrom(protoStuff, obj, schema);
        return obj;
    }

    @Override
    public byte[] marshallingRequest(MyRequest req) throws Exception {
        return this.serialize(req);
    }

    @Override
    public MyRequest unmarshallingRequest(byte[] data) throws Exception {
        return (MyRequest) this.unSerialize(data);
    }

    @Override
    public byte[] marshallingResponse(MyResponse rsp) throws Exception {
        return this.serialize(rsp);
    }

    @Override
    public MyResponse unmarshallingResponse(byte[] data) throws Exception {
        return (MyResponse) this.unSerialize(data);
    }
}
