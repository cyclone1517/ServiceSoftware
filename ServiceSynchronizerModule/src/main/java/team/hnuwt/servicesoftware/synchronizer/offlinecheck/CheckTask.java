package team.hnuwt.servicesoftware.synchronizer.offlinecheck;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.synchronizer.constant.TAG;
import team.hnuwt.servicesoftware.synchronizer.dao.CheckDao;
import team.hnuwt.servicesoftware.synchronizer.util.InnerProduceUtil;
import team.hnuwt.servicesoftware.synchronizer.util.ProduceUtil;
import team.hnuwt.servicesoftware.synchronizer.util.RedisUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TimerTask;

public class CheckTask extends TimerTask {

    private static final String TOPIC = "DOWNSTREAM";
    private static final String MQ_TAG = "OFFLINE";
    private static int checkTime = -1;    // 检查间隔秒数，-1是不检查

    public CheckTask(){}

    public CheckTask(int checkSecond) {
        checkTime = checkSecond * 60;
    }

    private static Logger logger = LoggerFactory.getLogger(CheckTask.class);

//    @Override
//    public void run() {
//        List<String> offList = new CheckDao().check(checkTime);
//        if (offList.size() != 0) {
//            String offListStr = JSON.toJSONString(offList);
//            InnerProduceUtil.addQueue(TOPIC, TAG, offListStr);
//        }
//        else {
//            logger.info("no newly offline collectors in @#@" + checkTime + " seconds");
//        }
//    }

    /**
     * 修改的定时任务，从Redis检查离线状况
     */
    @Override
    public void run(){
        List<String> offList = new ArrayList<>();
        long currMill = System.currentTimeMillis();

        Set<String> loginIds = RedisUtil.getSet(TAG.LOGIN.getStr());    /* 获取目前登录的设备集合 */
        for (String id: loginIds){
            String respStr =  RedisUtil.getHash(TAG.HEARTBEAT.getStr(), id);
            if (respStr != null)
            {
                long respMill = Long.parseLong(respStr);
                if (currMill - respMill > checkTime * 1000)
                {
                    offList.add(id);
                }
            }
        }

        if (offList.size()!=0){
            String offListStr = JSON.toJSONString(offList);
            InnerProduceUtil.addQueue(TOPIC, MQ_TAG, offListStr);
        }
        else {
            logger.info("no newly offline collectors in @#@" + checkTime + " seconds");
        }
    }
}
