package team.hnuwt.servicesoftware.synchronizer.handler;

import com.alibaba.fastjson.JSON;
import team.hnuwt.servicesoftware.synchronizer.model.Data;
import team.hnuwt.servicesoftware.synchronizer.service.DataService;
import team.hnuwt.servicesoftware.synchronizer.util.DataProcessThreadUtil;
import team.hnuwt.servicesoftware.synchronizer.util.RedisUtil;

import java.util.ArrayList;
import java.util.List;

public class DataHandler implements Runnable {

    private static final String DATA = "UPLOAD";
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
        }

    }
}
