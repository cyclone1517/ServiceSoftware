package team.hnuwt.servicesoftware.server.constant.up;

/**
 * 上行数据的业务编号
 * 计算方法：
 *      1. 数据标识位为 10000107
 *      2. 两两反向为   07010010
 *      3. 从16进制转为10进制  117506064
 */
public class FUNID {

    // 功能相关
    public static final long HEARTBEAR = 262160;
    public static final long LOGIN = 65552;
    public static final long READ_METER = 117506048;
    public static final long AUTOUPLOAD = 117506064;    /* 10000107 */
    public static final long HEART_LOGIN_RE = 262144;

    // 操作反馈相关
    public static final long GATE_SUCCESS = 65536;
    public static final long GATE_FAIL = 131072;
    public static final long ARCHIVE_DOWNLOAD_SUCCESS = 65536;  /* 00000100 */
    public static final long ARCHIVE_DOWNLOAD_FAIL = 131072;    /* 00000200 */

}