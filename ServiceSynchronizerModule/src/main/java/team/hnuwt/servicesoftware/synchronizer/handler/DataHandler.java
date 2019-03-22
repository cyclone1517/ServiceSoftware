package team.hnuwt.servicesoftware.synchronizer.handler;

import com.alibaba.fastjson.JSON;
import team.hnuwt.servicesoftware.synchronizer.constant.TAG;
import team.hnuwt.servicesoftware.synchronizer.model.Data;
import team.hnuwt.servicesoftware.synchronizer.service.DataService;
import team.hnuwt.servicesoftware.synchronizer.util.DataProcessThreadUtil;
import team.hnuwt.servicesoftware.synchronizer.util.HandlerUtil;
import team.hnuwt.servicesoftware.synchronizer.util.ProduceUtil;
import team.hnuwt.servicesoftware.synchronizer.util.RedisUtil;

import java.util.ArrayList;
import java.util.List;

public class DataHandler implements Runnable {

    private static final String DATA = "AUTO_UPLOAD";
    private int batchNum;

    public DataHandler(int batchNum){
        this.batchNum = batchNum;
    }

    @Override
    public void run() {
        DataProcessThreadUtil dptu = new DataProcessThreadUtil();

        List<Data> list = new ArrayList<>();
        for (int i = 0; i < batchNum; i++)      /* 连续取batchNum条 */
        {
            String s = RedisUtil.getData(DATA);
            if (s != null)
            {
                List<Data> data = JSON.parseArray(s, Data.class);
                list.addAll(data);
            } else                              /* 数据为空结束 */
                break;
        }
        if (list.size() > 0)
        {
            dptu.getExecutor().execute(new DataService(list));

            // 推送到消息队列
            String pubData = JSON.toJSONString(list);
            ProduceUtil.addQueue("UPSTREAM", TAG.AUTO_UPLOAD.getStr(), pubData);
        }
    }
}
