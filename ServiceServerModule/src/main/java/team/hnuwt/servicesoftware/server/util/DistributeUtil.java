package team.hnuwt.servicesoftware.server.util;

import team.hnuwt.servicesoftware.server.constant.TAG;
import team.hnuwt.servicesoftware.server.message.SendHandler;

/**
 * @author yuanlong chen
 * 下发指令工具类，用于给三方指令分类、包装与转发
 */
public class DistributeUtil {

    public static void distributeMessage(TAG tag, String msg){
        if (tag == TAG.DIRECT){     /* 透明转发 */
            directDistribute(msg);
        }
        if (tag == TAG.PLAIN){      /* 明文转发 */
            plainDistribute(msg);
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
    private static void plainDistribute(String msg){

    }

    private static void runTask(String msgBody){
        DataProcessThreadUtil.getExecutor().execute(new SendHandler(msgBody));
    }

}
