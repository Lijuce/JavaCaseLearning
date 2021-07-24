package lijuce.rpc.common.protocal;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import lijuce.rpc.annotation.MessageProtocolAno;
import lijuce.rpc.common.protocal.protoUtils.ProtoBufMessageProtocol;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;

/**
 * 序列化消息协议
 * @author Lijuce
 */
@MessageProtocolAno("protoStuff")
public class JavaSerializeMessageProtocol implements MessageProtocol{

    @Override
    public byte[] marshallingRequest(MyRequest req){
        return ProtoBufMessageProtocol.serialize(req);
    }

    @Override
    public MyRequest unmarshallingRequest(byte[] data) {
        return ProtoBufMessageProtocol.unSerialize(data, MyRequest.class);
    }

    @Override
    public byte[] marshallingResponse(MyResponse rsp) {
        return ProtoBufMessageProtocol.serialize(rsp);
    }

    @Override
    public MyResponse unmarshallingResponse(byte[] data) {
        return ProtoBufMessageProtocol.unSerialize(data, MyResponse.class);
    }
}
