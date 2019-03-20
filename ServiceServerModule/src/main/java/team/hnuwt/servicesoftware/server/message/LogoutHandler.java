package team.hnuwt.servicesoftware.server.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.server.model.Logout;
import team.hnuwt.servicesoftware.server.util.RedisUtil;
import java.util.List;

/**
 * 登出处理类
 * 由于登出信息没有报文，所以直接存入Redis即可
 */
public class LogoutHandler implements Runnable {

    private List<Logout> logoutAddr;
    private static ObjectMapper mapper = new ObjectMapper();

    private static Logger logger = LoggerFactory.getLogger(LogoutHandler.class);

    public LogoutHandler(List<Logout> logoutAddr) {
        this.logoutAddr = logoutAddr;
    }

    @Override
    public void run()
    {
        for (Logout i: logoutAddr) {
            ObjectNode node = mapper.createObjectNode();
            node.put("id", i.getId());
            node.put("port", i.getPort());
            RedisUtil.pushQueue("Logout", node.toString());
            logger.info("SEND LOGOUT TO REDIS DIRECTLT @#@ id: " + i.getId() + " @#@ port: " + i.getPort());
        }
        RedisUtil.publishData("LOGOUT");
    }
}
