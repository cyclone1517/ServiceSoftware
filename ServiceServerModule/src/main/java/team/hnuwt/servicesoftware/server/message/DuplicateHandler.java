package team.hnuwt.servicesoftware.server.message;

import com.alibaba.rocketmq.shade.com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.server.model.Duplicate;
import team.hnuwt.servicesoftware.server.model.Logout;
import team.hnuwt.servicesoftware.server.util.RedisUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.List;

/**
 * 重复集中器ID报告类
 * 如果检测到有集中器ID重复，则存入数据库
 */
public class DuplicateHandler implements Runnable {

    private long id;
    private SocketChannel oldSc;
    private SocketChannel newSc;

    private static Logger logger = LoggerFactory.getLogger(DuplicateHandler.class);

    public DuplicateHandler(long id, SocketChannel oldSc, SocketChannel newSc) {
        this.id = id;
        this.oldSc = oldSc;
        this.newSc = newSc;
    }

    @Override
    public void run()
    {
        try {
            Duplicate duplicate = new Duplicate();
            duplicate.setId(id);
            duplicate.setOldScInfo(((InetSocketAddress)oldSc.getRemoteAddress()).toString());   /* 不要删去强转类型的代码 */
            duplicate.setNewScInfo(((InetSocketAddress)newSc.getRemoteAddress()).toString());
            RedisUtil.pushQueue("DUPLICATE", JSON.toJSONString(duplicate));
            RedisUtil.publishData("DUPLICATE");
            logger.info("Notified SynchronizeModule Duplicate Login ID");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
