package team.hnuwt.servicesoftware.synchronizer.handler;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.synchronizer.model.Duplicate;
import team.hnuwt.servicesoftware.synchronizer.service.DuplicateService;
import team.hnuwt.servicesoftware.synchronizer.util.DataProcessThreadUtil;
import team.hnuwt.servicesoftware.synchronizer.util.RedisUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 重复ID集中器记录类
 */
public class DuplicateHandler implements Runnable {

    private static final String DATA = "DUPLICATE";
    private int batchNum;
    private static Logger logger = LoggerFactory.getLogger(DuplicateHandler.class);

    // 这个类掌管状态1-登录 和 2-登出，OfflineReHandler掌管状态0-掉线
    public DuplicateHandler(int batchNum){
        this.batchNum = batchNum;
    }

    @Override
    public void run() {
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
        if (list.size() > 0)
        {
            List<Duplicate> dupList = new ArrayList<>();
            list.forEach(dupInfo -> {
                dupList.add(JSON.parseObject(dupInfo, Duplicate.class));
            });

            DataProcessThreadUtil.getExecutor().execute(new DuplicateService(dupList));
        }

    }
}
