package team.hnuwt.servicesoftware.server.constant.up;

/**
 * 上行数据的业务编号
 */
public class FUNID {

    // 功能相关
    public static final long HEARTBEAR = 262160;
    public static final long LOGIN = 65552;
    public static final long READ_METER = 117506048;
    public static final long AUTOUPLOAD = 117506064;    /* 07010010 */
    public static final long HEART_LOGIN_RE = 262144;

    // 操作反馈相关
    public static final long GATE_SUCCESS = 65536;
    public static final long GATE_FAIL = 131072;
    public static final long ARCHIVE_DOWNLOAD_SUCCESS = 65553;  /* 00010011 */
    public static final long ARCHIVE_DOWNLOAD_FAIL = 131089;    /* 00020011 */

}
