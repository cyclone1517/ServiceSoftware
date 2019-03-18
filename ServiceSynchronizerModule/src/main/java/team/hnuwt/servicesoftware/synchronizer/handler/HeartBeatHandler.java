package team.hnuwt.servicesoftware.synchronizer.handler;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.synchronizer.model.HeartBeat;
import team.hnuwt.servicesoftware.synchronizer.service.HeartBeatService;
import team.hnuwt.servicesoftware.synchronizer.util.DataProcessThreadUtil;
import team.hnuwt.servicesoftware.synchronizer.util.RedisUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HeartBeatHandler implements Runnable {

    private static final String DATA = "HEARTBEAT";
    private int batchNum;
    private static Logger logger = LoggerFactory.getLogger(HeartBeatHandler.class);
    private static ObjectMapper mapper = new ObjectMapper();

    public HeartBeatHandler(int batchNum){
        this.batchNum = batchNum;
    }

    @Override
    public void run() {
        DataProcessThreadUtil dptu = new DataProcessThreadUtil();

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
            list.forEach(heartBeatNode -> {

                JsonNode root = HandlerHelper.getJsonNodeRoot(heartBeatNode);

                long cdId = Long.parseLong("0");
                JsonNode addr = root.get("addr");

                try {
                    if (addr != null) cdId = Long.parseLong(addr.asText());
                    else return;
                } catch (NumberFormatException e){
                    logger.error("Collector pack failed, @#@ id: " + addr.asText(), e);
                }
                heartList.add(new HeartBeat(cdId));
            });
            dptu.getExecutor().execute(new HeartBeatService(heartList));
        }
    }
}
