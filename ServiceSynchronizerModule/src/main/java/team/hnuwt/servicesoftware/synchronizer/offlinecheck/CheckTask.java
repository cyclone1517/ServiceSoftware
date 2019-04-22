package team.hnuwt.servicesoftware.synchronizer.offlinecheck;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.synchronizer.dao.CheckDao;
import team.hnuwt.servicesoftware.synchronizer.util.InnerProduceUtil;
import team.hnuwt.servicesoftware.synchronizer.util.ProduceUtil;

import java.util.List;
import java.util.TimerTask;

public class CheckTask extends TimerTask {

    private static final String TOPIC = "DOWNSTREAM";
    private static final String TAG = "OFFLINE";
    private static int checkTime = -1;    // 检查间隔秒数，-1是不检查

    public CheckTask(){}

    public CheckTask(int checkSecond) {
        checkTime = checkSecond * 60;
    }

    private static Logger logger = LoggerFactory.getLogger(CheckTask.class);

    @Override
    public void run() {
        List<String> offList = new CheckDao().check(checkTime);
        if (offList.size() != 0) {
            String offListStr = JSON.toJSONString(offList);
            InnerProduceUtil.addQueue(TOPIC, TAG, offListStr);
        }
        else {
            logger.info("no newly offline collectors in @#@" + checkTime + " seconds");
        }
    }
}
