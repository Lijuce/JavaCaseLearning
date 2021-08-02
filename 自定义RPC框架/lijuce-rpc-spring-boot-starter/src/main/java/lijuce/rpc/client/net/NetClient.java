package lijuce.rpc.client.net;

import lijuce.rpc.common.Service;
import lijuce.rpc.common.protocal.MessageProtocol;
import lijuce.rpc.common.protocal.MyRequest;
import lijuce.rpc.common.protocal.MyResponse;

public interface NetClient {
    byte[] sendRequest(byte[] data, Service service) throws InterruptedException;

    MyResponse sendRequest(MyRequest myRequest, Service service, MessageProtocol messageProtocol);
}
