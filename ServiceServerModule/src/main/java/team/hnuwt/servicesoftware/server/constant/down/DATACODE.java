package team.hnuwt.servicesoftware.server.constant.down;

import java.util.HashMap;


/**
 * @author yuanlong chen
 * 存放各种命令的标识符，控制码，功能码，抄读方式，序列号
 */
public class DATACODE {

    private static HashMap<String, String> DataId;      /* 查询数据的标识符 */
    private static HashMap<String, String> ctrlCode;    /* 控制码 */
    private static HashMap<String, String> afnCode;     /* 功能码 */
    private static HashMap<String,String> readMtd;      /* 抄读方式 */
    private static HashMap<String, String> serial;      /* 序列号 */

    static {
        DataId = new HashMap<>();
        ctrlCode = new HashMap<>();
        afnCode = new HashMap<>();
        readMtd = new HashMap<>();
        serial = new HashMap<>();

        // 查询数据标识符
        DataId.put("READ_METER", "00000107");   /* 单多抄表 */
        DataId.put("READ_TIME", "00000100");   /* 读取集中器时钟 */
        DataId.put("READ_VERSION", "00000101");   /* 读取集中器版本 */

        // 控制命令标识符
        DataId.put("CTRL_TIME", "00001001");    /* 对时命令 */
        DataId.put("CTRL_ON", "00000100");    /* 远程开关阀 */
        DataId.put("CTRL_OFF", "00000100");    /* 远程开关阀 */
        DataId.put("UPLOAD_ON", "00004003");    /* 上报允许 */
        DataId.put("UPLOAD_OFF", "00004003");   /* 上报停止 */
        DataId.put("READ_UPLOAD", "00004003");   /* 读取上报是否允许 */
        DataId.put("ARCHIVE_DOWNLOAD", "11000100"); /* 档案下载 */
        DataId.put("ARCHIVE_CLOSE", "00008002"); /* 档案下载 */


        // 控制码
        ctrlCode.put("READ_METER", "70");
        ctrlCode.put("CTRL_ON", "70");
        ctrlCode.put("CTRL_OFF", "70");
        ctrlCode.put("UPLOAD_ON", "70");
        ctrlCode.put("UPLOAD_OFF", "70");
        ctrlCode.put("READ_UPLOAD", "70");
        ctrlCode.put("ARCHIVE_DOWNLOAD", "70");
        ctrlCode.put("ARCHIVE_CLOSE", "70");

        //功能码
        afnCode.put("READ_METER", "8C");
        afnCode.put("CTRL_ON", "85");
        afnCode.put("CTRL_OFF", "85");
        afnCode.put("UPLOAD_ON", "84");
        afnCode.put("UPLOAD_OFF", "84");
        afnCode.put("READ_UPLOAD", "8A");
        afnCode.put("ARCHIVE_DOWNLOAD", "84");
        afnCode.put("ARCHIVE_CLOSE", "84");


        //抄读方式
        readMtd.put("READ_METER", "00");
        readMtd.put("READ_TIME", "03");
        readMtd.put("READ_VERSION", "03");

        //序列号
        serial.put("CTRL_ON", "78");
        serial.put("CTRL_OFF", "77");
        serial.put("UPLOAD_ON", "70");
        serial.put("UPLOAD_OFF", "7F");
        serial.put("READ_UPLOAD", "70");
        serial.put("ARCHIVE_DOWNLOAD", "7B");
        serial.put("ARCHIVE_CLOSE", "7B");

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

    public static String getSerial(String key) { return serial.get(key);}

}
