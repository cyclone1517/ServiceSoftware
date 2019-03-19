package team.hnuwt.servicesoftware.synchronizer.handler;

import com.alibaba.fastjson.JSON;
import team.hnuwt.servicesoftware.synchronizer.dao.CheckDao;
import team.hnuwt.servicesoftware.synchronizer.util.DataProcessThreadUtil;
import team.hnuwt.servicesoftware.synchronizer.util.RedisUtil;

import java.util.ArrayList;
import java.util.List;

public class OfflineReHandler implements Runnable{

    private String DATA = "OFFLINE_RE";
    private int batchNum;

    public OfflineReHandler(int batchNum){
        this.batchNum = batchNum;
    }

    @Override
    public void run() {
        DataProcessThreadUtil dptu = new DataProcessThreadUtil();
        List<String> offlineOKList = new ArrayList<>();

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
        if (list.size() > 0){
            list.forEach(l->{
                List<String> tempList = JSON.parseArray(l, String.class);
                offlineOKList.addAll(tempList);
            });
        }

        new CheckDao().resetOffline(offlineOKList);
    }
}
