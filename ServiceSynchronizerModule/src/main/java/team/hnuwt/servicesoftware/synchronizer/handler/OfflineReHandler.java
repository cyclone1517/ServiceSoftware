package team.hnuwt.servicesoftware.synchronizer.handler;

import com.alibaba.fastjson.JSON;
import team.hnuwt.servicesoftware.synchronizer.constant.TAG;
import team.hnuwt.servicesoftware.synchronizer.model.Login;
import team.hnuwt.servicesoftware.synchronizer.service.DetailService;
import team.hnuwt.servicesoftware.synchronizer.service.OfflineReService;
import team.hnuwt.servicesoftware.synchronizer.util.DataProcessThreadUtil;
import team.hnuwt.servicesoftware.synchronizer.util.HandlerUtil;
import team.hnuwt.servicesoftware.synchronizer.util.ProduceUtil;
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

            // 更新登陆表
            DataProcessThreadUtil.getExecutor().execute(new OfflineReService(offlineOKList));

            // 更新登录详情表
            List<Login> loginList = new ArrayList<>();
            for (String addr: offlineOKList){
                loginList.add(new Login(Long.parseLong(addr), 2));
            }
            DataProcessThreadUtil.getExecutor().execute(new DetailService(loginList, false));

            // 推送到消息队列（现取消：登录和离线不再推送）
        }

    }
}
