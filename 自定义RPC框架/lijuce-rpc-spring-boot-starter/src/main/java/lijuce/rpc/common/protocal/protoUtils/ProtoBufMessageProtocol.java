package lijuce.rpc.common.protocal.protoUtils;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import lijuce.rpc.common.protocal.MessageProtocol;
import lijuce.rpc.common.protocal.MyRequest;
import lijuce.rpc.common.protocal.MyResponse;

/**
 * @ClassName ProtoBufMessageProtocol
 * @Description TODO
 * @Author Lijuce_K
 * @Date 2021/7/22 18:20
 * @Version 1.0
 **/
public class ProtoBufMessageProtocol {

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
    public static <T> T unSerialize(byte[] protoStuff, Class<T> clazz){
        Schema<T> schema = RuntimeSchema.getSchema(clazz);
        T obj = schema.newMessage();
        ProtobufIOUtil.mergeFrom(protoStuff, obj, schema);
        return obj;
    }
}
