package lijuce.rpc.common.protocal;

import lijuce.rpc.annotation.MessageProtocolAno;
import lijuce.rpc.common.protocal.protoUtils.SerializeMessageProtoStuff;

/**
 * 序列化消息协议
 * @author Lijuce
 */
@MessageProtocolAno("protoStuff")
public class JavaSerializeMessageProtocol implements MessageProtocol{

    @Override
    public byte[] marshallingRequest(MyRequest req){
        return SerializeMessageProtoStuff.serialize(req);
    }

    @Override
    public MyRequest unmarshallingRequest(byte[] data) {
        return SerializeMessageProtoStuff.unSerialize(data, MyRequest.class);
    }

    @Override
    public byte[] marshallingResponse(MyResponse rsp) {
        return SerializeMessageProtoStuff.serialize(rsp);
    }

    @Override
    public MyResponse unmarshallingResponse(byte[] data) {
        return SerializeMessageProtoStuff.unSerialize(data, MyResponse.class);
    }
}
