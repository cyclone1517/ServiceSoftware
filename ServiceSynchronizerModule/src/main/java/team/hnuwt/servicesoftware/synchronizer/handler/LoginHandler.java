package team.hnuwt.servicesoftware.synchronizer.handler;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.synchronizer.constant.TAG;
import team.hnuwt.servicesoftware.synchronizer.model.Login;
import team.hnuwt.servicesoftware.synchronizer.service.DetailService;
import team.hnuwt.servicesoftware.synchronizer.service.RedisLoginService;
import team.hnuwt.servicesoftware.synchronizer.util.DataProcessThreadUtil;
import team.hnuwt.servicesoftware.synchronizer.util.HandlerUtil;
import team.hnuwt.servicesoftware.synchronizer.util.RedisUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * 一类两用，批量读取登录或批量读取登出
 * 每次只执行其一，根据读取的TAG决定业务类型
 */
public class LoginHandler implements Runnable {

    private String DATA = TAG.LOGIN_PIPE.getStr();      /* 取出登录数据 */
    private int batchNum;
    private int state;      // 1-登录 0-掉线 2-登出
    private static ObjectMapper mapper = new ObjectMapper();
    private static Logger logger = LoggerFactory.getLogger(LoginHandler.class);

    // 这个类掌管状态1-登录 和 2-登出，OfflineReHandler掌管状态0-掉线
    public LoginHandler(int batchNum, int state){
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
            List<String> loginIds = new ArrayList<>();
            list.forEach(loginSet-> {
                loginIds.addAll(JSON.parseArray(loginSet, String.class));       /* 获得所有登录集中器的Id */
            });

            // 根据登录的集中器Id, 构建登录详情对象即可
            List<Login> loginList = new ArrayList<>();
            for (String id: loginIds){
                loginList.add(new Login(Long.parseLong(id), state));
            }

            // 在协议栈把登录状态存入过Redis，这里无需重复存

            // 更新Mysql登录详情表
            //DataProcessThreadUtil.getExecutor().execute(new DetailService(loginList, state==1));
        }

    }
}
