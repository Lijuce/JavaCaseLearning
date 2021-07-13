package lijuce.rpc.client.net;

import lijuce.rpc.common.Service;

public interface NetClient {
    byte[] sendRequest(byte[] data, Service service) throws InterruptedException;
}
