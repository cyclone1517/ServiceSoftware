package team.hnuwt.servicesoftware.synchronizer.handler;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.synchronizer.constant.TAG;
import team.hnuwt.servicesoftware.synchronizer.model.Login;
import team.hnuwt.servicesoftware.synchronizer.service.DetailService;
import team.hnuwt.servicesoftware.synchronizer.service.LoginService;
import team.hnuwt.servicesoftware.synchronizer.service.RedisLogoutService;
import team.hnuwt.servicesoftware.synchronizer.util.DataProcessThreadUtil;
import team.hnuwt.servicesoftware.synchronizer.util.HandlerUtil;
import team.hnuwt.servicesoftware.synchronizer.util.RedisUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 一类两用，批量读取登录或批量读取登出
 * 每次只执行其一，根据读取的TAG决定业务类型
 */
public class LogoutHandler implements Runnable {

    private String DATA = "OFFLINE_RE";
    private int batchNum;
    private int state;      // 1-登录 0-掉线 2-登出
    private static ObjectMapper mapper = new ObjectMapper();
    private static Logger logger = LoggerFactory.getLogger(LogoutHandler.class);

    // 这个类掌管状态1-登录 和 2-登出
    public LogoutHandler(int batchNum, int state){
        this.batchNum = batchNum;
        this.state = state;
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
            // 首先获得登出集中器的id集合
            List<String> logoutIds = new ArrayList<>();
            list.forEach(logoutSet -> {
                logoutIds.addAll(JSON.parseArray(logoutSet, String.class));   /* 删除Redis中Login Set的相关条目 */
            });

            // 创建登出集中器详细信息对象
            List<Login> logoutList = new ArrayList<>();     /* 新建Login对象会自动生成系统时间 */
            for (String id: logoutIds){
                logoutList.add(new Login(Long.parseLong(id), state));
            }

            // 删除Redis中Login Set的相关条目
            DataProcessThreadUtil.getExecutor().execute(new RedisLogoutService(logoutIds));

            // 更新登录详情表
            //DataProcessThreadUtil.getExecutor().execute(new DetailService(logoutList, state==1));
        }

    }
}
