package team.hnuwt.servicesoftware.server.constant.down;

import java.util.HashMap;


/**
 * @author yuanlong chen
 * 存放各种命令的标识符，控制码
 */
public class DATACODE {

    private static HashMap<String, String> DataId;      /* 查询数据的标识符 */
    private static HashMap<String, String> ctrlCode;    /* 控制码 */
    private static HashMap<String, String> afnCode;     /* 功能码 */
    private static HashMap<String,String> readMtd;      /* 抄读方式 */

    static {
        DataId = new HashMap<>();
        ctrlCode = new HashMap<>();
        afnCode = new HashMap<>();
        readMtd = new HashMap<>();

        // 查询数据标识符初始化
        DataId.put("READ_METER", "00000107");   /* 单多抄表 */
        DataId.put("READ_TIME", "00000100");   /* 读取集中器时钟 */
        DataId.put("READ_VERSION", "00000101");   /* 读取集中器版本 */

        // 控制命令标识符初始化
        DataId.put("CTRL_TIME", "00001001");    /* 对时命令 */
        DataId.put("CTRL_ONOFF", "00000100");    /* 远程开关阀 */
        DataId.put("CTRL_SETONOFF", "00004523");    /* 定时开关阀（未用） */

        // 查询数据控制码
        ctrlCode.put("READ_METER", "70");

        //功能码
        afnCode.put("READ_METER", "8C");

        //抄读方式
        readMtd.put("READ_METEER", "00");
        readMtd.put("READ_TIME", "03");
        readMtd.put("READ_VERSION", "03");

    }

    public static String getDataId(String key){
        return DataId.get(key);
    }

    public static String getAfnCode(String key){
        return afnCode.get(key);
    }

    public static String getCtrlCode(String key){
        return ctrlCode.get(key);
    }

    public static String getReadMtd(String key){
        return readMtd.get(key);
    }

}
