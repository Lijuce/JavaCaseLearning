package lijuce.rpc.client.discovery;

import lijuce.rpc.client.cache.ServerDiscoveryCache;
import org.I0Itec.zkclient.IZkChildListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @ClassName ZkChildListenerImpl
 * @Description 子节点监听触发后的具体执行任务
 * @Author Lijuce_K
 * @Date 2021/7/25 0025 20:19
 * @Version 1.0
 **/
public class ZkChildListenerImpl implements IZkChildListener {

    private static Logger logger = LoggerFactory.getLogger(ZkChildListenerImpl.class);

    /**
     * 监听子节点的删除和新增事件
     * @param parentPath
     * @param childList
     * @throws Exception
     */
    @Override
    public void handleChildChange(String parentPath, List<String> childList) {
        logger.info("Child change parentPath:[{}] -- childList:[{}]", parentPath, childList);
        // 只要子节点有变动就清空缓存
        String[] split = parentPath.split("/");
        ServerDiscoveryCache.removeAll(split[2]);
    }
}
