package lijuce.rpc.common.protocal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 序列化消息协议
 */
public class JavaSerializeMessageProtocol implements MessageProtocol{

    private byte[] serialize(Object obj) throws Exception {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bout);
        out.writeObject(obj);
        return bout.toByteArray();
    }
    @Override
    public byte[] marshallingRequest(MyRequest req) throws Exception {
        return this.serialize(req);
    }

    @Override
    public MyRequest unmarshallingRequest(byte[] data) throws Exception {
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(data));
        return (MyRequest) in.readObject();
    }

    @Override
    public byte[] marshallingResponse(MyResponse rsp) throws Exception {
        return this.serialize(rsp);
    }

    @Override
    public MyResponse unmarshallingResponse(byte[] data) throws Exception {
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(data));
        return (MyResponse) in.readObject();
    }
}
