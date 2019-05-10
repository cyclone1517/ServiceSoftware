package team.hnuwt.servicesoftware.server.message;

import com.alibaba.rocketmq.shade.com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.server.model.Logout;
import team.hnuwt.servicesoftware.server.util.RedisUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 登出处理类
 * 由于登出信息没有报文，所以直接存入Redis即可
 */
public class LogoutHandler implements Runnable {

    private List<Logout> logoutAddr;
    private static ObjectMapper mapper = new ObjectMapper();

    private static Logger logger = LoggerFactory.getLogger(LogoutHandler.class);

    public LogoutHandler(List<Logout> logoutAddr) { /* 元素含有端口 */
        this.logoutAddr = logoutAddr;
    }

    @Override
    public void run(){
        List<String> logoutList = new ArrayList<>();    /* 元素不含端口 */
        for (Logout i: logoutAddr) {
            logoutList.add(i.getId()+"");
        }
        String toSend = JSON.toJSONString(logoutList);
        RedisUtil.pushQueue("LOGOUT", toSend);
        logger.info("SEND LOGOUT TO REDIS DIRECTLT @#@" + toSend);
        RedisUtil.publishData("LOGOUT");
    }
}
