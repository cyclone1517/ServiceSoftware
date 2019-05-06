package team.hnuwt.servicesoftware.synchronizer.handler;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.synchronizer.constant.TAG;
import team.hnuwt.servicesoftware.synchronizer.model.HeartBeat;
import team.hnuwt.servicesoftware.synchronizer.model.Login;
import team.hnuwt.servicesoftware.synchronizer.service.DetailService;
import team.hnuwt.servicesoftware.synchronizer.service.HeartBeatService;
import team.hnuwt.servicesoftware.synchronizer.service.LoginService;
import team.hnuwt.servicesoftware.synchronizer.util.DataProcessThreadUtil;
import team.hnuwt.servicesoftware.synchronizer.util.HandlerUtil;
import team.hnuwt.servicesoftware.synchronizer.util.ProduceUtil;
import team.hnuwt.servicesoftware.synchronizer.util.RedisUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 一类两用，批量读取登录或批量读取登出
 * 每次只执行其一，根据读取的TAG决定业务类型
 */
public class LoginHandler implements Runnable {

    private String DATA = "LOGIN";
    private int batchNum;
    private int state;      // 1-登录 0-掉线 2-登出
    private static ObjectMapper mapper = new ObjectMapper();
    private static Logger logger = LoggerFactory.getLogger(LoginHandler.class);

    // 这个类掌管状态1-登录 和 2-登出，OfflineReHandler掌管状态0-掉线
    public LoginHandler(int batchNum, int state){
        this.batchNum = batchNum;
        this.state = state;
        if (state == 2){
            DATA = "LOGOUT";
        }
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
            List<Login> loginList = new ArrayList<>();
            List<String> loginIds = new ArrayList<>();
            list.forEach(loginStateNode -> {
                JsonNode root = HandlerHelper.getJsonNodeRoot(loginStateNode);
                long cdId;
                JsonNode cdIdNode = root.get("addr");
                try {
                    if (cdIdNode != null) cdId = Long.parseLong(cdIdNode.asText());
                    else return;
                } catch (NumberFormatException e){
                    logger.error("Id packed failed, @#@ id: " + cdIdNode.asText());
                    return;
                }
                loginList.add(new Login(cdId, state));
                loginIds.add(HandlerUtil.packAddrToHex(cdId));
            });

            // 更新登录表
            DataProcessThreadUtil.getExecutor().execute(new LoginService(loginList, state==1));

            // 更新登录详情表
            DataProcessThreadUtil.getExecutor().execute(new DetailService(loginList, state==1));

            // 推送到消息队列（现取消：登录和离线不再推送）
        }

    }
}
