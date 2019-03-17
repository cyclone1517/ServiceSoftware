package team.hnuwt.servicesoftware.synchronizer.handler;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.synchronizer.model.HeartBeat;
import team.hnuwt.servicesoftware.synchronizer.service.HeartBeatService;
import team.hnuwt.servicesoftware.synchronizer.util.DataProcessThreadUtil;
import team.hnuwt.servicesoftware.synchronizer.util.RedisUtil;

import java.util.ArrayList;
import java.util.List;

public class HeartBeatHandler implements Runnable {

    private static final String DATA = "HeartBeat";
    private int batchNum;
    private static Logger logger = LoggerFactory.getLogger(HeartBeatHandler.class);

    public HeartBeatHandler(int batchNum){
        this.batchNum = batchNum;
    }

    @Override
    public void run() {
        DataProcessThreadUtil dptu = new DataProcessThreadUtil();
        while (true)
        {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < batchNum; i++)      /* 连续取batchNum条 */
            {
                String s = RedisUtil.getData(DATA);
                if (s != null)
                {
                    list.add(s);
                } else                              /* 数据为空结束 */
                    break;
            }
            if (list.size() > 0)                    /* 打包装入Mysql */
            {
                List<HeartBeat> heartList = new ArrayList<>();
                list.forEach(collectorId -> {
                    long cdId = Long.parseLong("0");
                    try {
                        cdId = Long.parseLong(collectorId);
                    } catch (NumberFormatException e){
                        logger.warn("Collector pack failed, @#@ id: " + collectorId);
                    }
                    heartList.add(new HeartBeat(cdId));
                });
                dptu.getExecutor().execute(new HeartBeatService(heartList));
            }
        }
    }
}
