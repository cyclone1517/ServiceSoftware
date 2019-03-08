package team.hnuwt.servicesoftware.server.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.server.constant.down.FUNTYPE;
import team.hnuwt.servicesoftware.server.constant.down.TAG;
import team.hnuwt.servicesoftware.server.message.SendHandler;

import java.io.IOException;

/**
 * @author yuanlong chen
 * 下发指令工具类，用于给三方指令分类、包装与转发
 */
public class DistributeUtil {

    private static Logger logger = LoggerFactory.getLogger(DistributeUtil.class);

    public static void distributeMessage(TAG tag, String msg){
        if (tag == TAG.DIRECT){     /* 透明转发 */
            directDistribute(msg);
        }
        if (tag == TAG.PLAIN){      /* 明文转发 */
            try {
                plainDistribute(msg);
            } catch (IOException e) {
                logger.error("plain transfer pkg cannot be packed!!!");
            }
        }
    }

    /**
     * 透明转发
     * @param msg 原始消息包
     */
    private static void directDistribute(String msg){
        //不做任何处理
        runTask(msg);
    }

    /**
     * 明文转发
     * @param msg
     */
    private static void plainDistribute(String msg) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(msg);
        JsonNode afn = root.get("afn"); /* 先按业务类型区分 */
        if (null != afn){
            String fun = afn.asText();
            String fn = root.path("fn").asText();
            FUNTYPE funtype = FUNTYPE.getFUN(fun);
            if (funtype == FUNTYPE.QUERY){
                runTask(PkgPackUtil.getQueryPkg(root, fn));
            }
            if (funtype == FUNTYPE.CONTROL){
                runTask(PkgPackUtil.getCtrlPkg(root, fn));
            }
        }
    }

    private static void runTask(String msgBody){
        DataProcessThreadUtil.getExecutor().execute(new SendHandler(msgBody));
    }

}
