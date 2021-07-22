package lijuce.rpc.common.protocal;

/**
 * @ClassName ProtoBufMessageProtocol
 * @Description TODO
 * @Author Lijuce_K
 * @Date 2021/7/22 18:20
 * @Version 1.0
 **/
public class ProtoBufMessageProtocol implements MessageProtocol{
    @Override
    public byte[] marshallingRequest(MyRequest req) throws Exception {
        return new byte[0];
    }

    @Override
    public MyRequest unmarshallingRequest(byte[] data) throws Exception {
        return null;
    }

    @Override
    public byte[] marshallingResponse(MyResponse rsp) throws Exception {
        return new byte[0];
    }

    @Override
    public MyResponse unmarshallingResponse(byte[] data) throws Exception {
        return null;
    }
}
