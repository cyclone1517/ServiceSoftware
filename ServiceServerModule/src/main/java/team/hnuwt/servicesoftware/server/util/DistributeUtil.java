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

    /**
     * 透明转发
     * @param msg 原始消息包
     */
    public static void directDistribute(String msg){
        //不做任何处理
        runTask(msg);
    }

    /**
     * 明文转发
     * @param msg 消息
     * @TAG 业务类型
     */
    public static void plainDistribute(String msg, TAG tag) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(msg);
        if (tag == TAG.READ_METER){
            runTask(PkgPackUtil.geneReadMeterPkg(root, TAG.READ_METER.getStr()));
        }
        else if (tag == TAG.CTRL_TIME){

        }
        else if (tag == TAG.CTRL_ONOFF){

        }
        else {
            throw new Exception("UNKNOWN TAG:" + tag);
        }

    }

    private static void runTask(String msgBody){
        DataProcessThreadUtil.getExecutor().execute(new SendHandler(msgBody));
    }

}
