package team.hnuwt.servicesoftware.server.message;

import com.alibaba.rocketmq.shade.com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.server.constant.down.TAG;
import team.hnuwt.servicesoftware.server.model.HeartData;
import team.hnuwt.servicesoftware.server.util.RedisUtil;

public class SubHeartBeatHandler implements Runnable {

    private Long id;

    public SubHeartBeatHandler(Long id){
        this.id = id;
    }

    private static Logger logger = LoggerFactory.getLogger(SubHeartBeatHandler.class);

    @Override
    public void run() {
        HeartData data = new HeartData();
        data.setAddr(id);

        String temp = JSON.toJSONString(data);
        RedisUtil.pushQueue(TAG.HEARTBEAT.getStr(), temp);
        RedisUtil.publishData(TAG.HEARTBEAT.getStr());
        logger.info("SEND supplementary HEARTBEAT from:" + id);
    }
}
