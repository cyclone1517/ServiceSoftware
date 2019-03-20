package team.hnuwt.servicesoftware.server.message;

import com.alibaba.rocketmq.shade.com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.server.constant.down.TAG;
import team.hnuwt.servicesoftware.server.constant.down.TOPIC;
import team.hnuwt.servicesoftware.server.util.ByteBuilder;
import team.hnuwt.servicesoftware.server.util.ConcentratorUtil;
import team.hnuwt.servicesoftware.server.util.ProduceUtil;
import team.hnuwt.servicesoftware.server.util.RedisUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 心跳包处理类
 */
public class CloseOfflineHandler implements Runnable {

    private List<String> offlineList;

    private static Logger logger = LoggerFactory.getLogger(CloseOfflineHandler.class);

    public CloseOfflineHandler(List<String> offlineList)
    {
        this.offlineList = offlineList;
    }

    @Override
    public void run()
    {
        List<String> successList = new ArrayList<>();
        offlineList.forEach(ol->{
            Long offlineId = Long.parseLong(ol);
            if (ConcentratorUtil.removeAndOK(offlineId)){
                successList.add(ol);
            }
        });
        /* 操作完成回复消息 */
        if (successList.size() != 0) {
            String data = JSON.toJSONString(successList);
            RedisUtil.pushQueue("OFFLINE_RE", data);
            RedisUtil.publishData("OFFLINE_RE");
        }
    }
}
