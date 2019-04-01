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
import team.hnuwt.servicesoftware.synchronizer.service.HeartBeatService;
import team.hnuwt.servicesoftware.synchronizer.service.LoginService;
import team.hnuwt.servicesoftware.synchronizer.util.DataProcessThreadUtil;
import team.hnuwt.servicesoftware.synchronizer.util.HandlerUtil;
import team.hnuwt.servicesoftware.synchronizer.util.ProduceUtil;
import team.hnuwt.servicesoftware.synchronizer.util.RedisUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
                int cdPort;
                JsonNode cdIdNode = root.get("addr");
                JsonNode cdPortNode = root.get("port");
                try {
                    if (cdIdNode != null) cdId = Long.parseLong(cdIdNode.asText());
                    else return;
                } catch (NumberFormatException e){
                    logger.error("Id packed failed, @#@ id: " + cdIdNode.asText());
                    return;
                }
                try {
                    if (cdPortNode != null) cdPort = Integer.parseInt(cdPortNode.asText());
                    else cdPort = 0;
                } catch (NumberFormatException e){
                    logger.error("Port packed failed, @#@ port: " + cdPortNode.asText());
                    return;
                }
                loginList.add(new Login(cdId, cdPort, state));
                loginIds.add(HandlerUtil.packAddrToHex(cdId));
            });
            DataProcessThreadUtil.getExecutor().execute(new LoginService(loginList));

            // 推送到消息队列
            String data = HandlerUtil.geneMsg(loginIds, state);
            ProduceUtil.addQueue("UPSTREAM", TAG.COLC_STATE.getStr(), data);
        }

    }
}
