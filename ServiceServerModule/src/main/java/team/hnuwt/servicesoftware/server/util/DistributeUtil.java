package team.hnuwt.servicesoftware.server.util;

import com.alibaba.rocketmq.shade.com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.server.constant.down.FUNTYPE;
import team.hnuwt.servicesoftware.server.constant.down.TAG;
import team.hnuwt.servicesoftware.server.message.CloseOfflineHandler;
import team.hnuwt.servicesoftware.server.message.SendHandler;
import java.util.List;

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
     * 明文转发，和部分控制指令
     * @param msg 消息
     * @TAG 业务类型
     */
    public static void plainDistribute(String msg, TAG tag) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(msg);

        /* 读表 */
        if (tag == TAG.READ_METER){
            runTask(PkgPackUtil.geneReadMeterPkg(root, TAG.READ_METER.getStr()));
        }

        /* 开阀 */
        else if (tag == TAG.CTRL_ON){
            runTask(PkgPackUtil.geneCtrlOnOffPkg(root, TAG.CTRL_ON.getStr(), true));
        }

        /* 关阀 */
        else if (tag == TAG.CTRL_OFF){
            runTask(PkgPackUtil.geneCtrlOnOffPkg(root, TAG.CTRL_ON.getStr(), false));
        }

        /* 清除离线集中器连接资源 */
        else if (tag == TAG.OFFLINE){
            List<String> offlineList = JSON.parseArray(msg, String.class);
            runTask(new CloseOfflineHandler(offlineList));
        }

        /* 上报开启 */
        else if (tag == TAG.UPLOAD_ON){
            runTask(PkgPackUtil.geneUploadOnOffPkg(root, TAG.UPLOAD_ON.getStr(), true));
        }

        /* 上报关闭 */
        else if (tag == TAG.UPLOAD_OFF){
            runTask(PkgPackUtil.geneUploadOnOffPkg(root, TAG.UPLOAD_OFF.getStr(), false));
        }

        /* 读取上报允许 */
        else if (tag == TAG.READ_UPLOAD){
            runTask(PkgPackUtil.geneReadUploadPkg(root, TAG.READ_UPLOAD.getStr()));
        }

        /* 下载档案 */
        else if (tag == TAG.ARCHIVE_DOWNLOAD){
            String torun = PkgPackUtil.geneArchive(root, TAG.ARCHIVE_DOWNLOAD.getStr());
            //runTask(PkgPackUtil.geneArchive(root, TAG.ARCHIVE_DOWNLOAD.getStr()));
        }

        /* 关闭档案 */
        else if (tag == TAG.ARCHIVE_CLOSE){
            String torun = PkgPackUtil.geneCloseArchive(root, TAG.ARCHIVE_CLOSE.getStr());
            //runTask(PkgPackUtil.geneCloseArchive(root, TAG.ARCHIVE_CLOSE.getStr()));
        }

        /* 其它 */
        else {
            throw new Exception("UNKNOWN TAG:" + tag);
        }

    }

    private static void runTask(String msgBody){
        DataProcessThreadUtil.getExecutor().execute(new SendHandler(msgBody));
    }

    private static void runTask(Runnable handler){
        DataProcessThreadUtil.getExecutor().execute(handler);
    }

}
