package team.hnuwt.servicesoftware.server.util;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.server.constant.down.DATACODE;

import java.util.List;

/**
 * @author yuanlong chen
 * 将明文数据打包成报文的包工具
 */
public class PkgPackUtil {

    private static Logger logger = LoggerFactory.getLogger(PkgPackUtil.class);

    public static String geneReadMeterPkg(JsonNode root, String TAG) throws Exception {
        StringBuilder result = new StringBuilder();

        // get num of meters
        int meterNum = FieldPacker.getMeterNum(root);
        String numStr = FieldPacker.getNBitHexNum(meterNum, 4);
        String L = FieldPacker.getMultiMeterPkgLen(15, meterNum, 2);
        List<String> ids = FieldPacker.getMeterIds(root.path("meter"));
        int addr = root.path("termAddr").asInt();
        String addrId = FieldPacker.toHexAddrId(addr);

        result.append("68");
        result.append(L);       /* extraLen 抄表方式1字节 + 表数量2字节 */
        result.append(L);
        result.append("68");
        result.append(DATACODE.getCtrlCode(TAG));   /* 控制符 */
        result.append(addrId);                        /* 地址域 */
        result.append(DATACODE.getAfnCode(TAG));    /* AFN功能码 */
        result.append("70");                        /* 序列号，7单帧需回复，0保留 */
        result.append(DATACODE.getDataId(TAG));     /* 数据单元标识 */
        result.append(DATACODE.getReadMtd(TAG));    /* 抄读方式 */
        result.append(numStr);  /* 表数量 */
        ids.forEach(result::append);                /* 表编号 */
        result.append(FieldPacker.calcuCs(result));             /* 校验位 */
        result.append("16");                        /* 结束符 */

        if (result.toString().contains("null")){
            throw new Exception("package pack failed!!!");
        }

        return result.toString();
    }

    public static String geneReadTimePkg(JsonNode root, String FUN){
        StringBuilder result = new StringBuilder();

        // get num of meters
        int meterNum = FieldPacker.getMeterNum(root);
        String L = FieldPacker.getMultiMeterPkgLen(20, meterNum, 2);
        int addr = root.path("termAddr").asInt();
        String addrId = FieldPacker.toHexAddrId(addr);

        result.append("68");
        result.append(L);
        result.append(L);
        result.append("68");
        result.append(DATACODE.getCtrlCode(FUN));   /* 控制符 */
        result.append(addrId);                      /* 地址域 */
        result.append(DATACODE.getAfnCode(FUN));    /* AFN功能码 */
        result.append("70");                        /* 序列号，7单帧需回复，0保留 */
        result.append(DATACODE.getDataId(FUN));     /* 数据单元标识 */
        result.append(DATACODE.getReadMtd(FUN));    /* 抄读方式 */
        result.append(FieldPacker.calcuCs(result)); /* 校验位 */
        result.append("16");                        /* 结束符 */

        return result.toString();
    }

    public static String geneCtrlOnOffPkg(JsonNode root, String FUN, boolean on){
        StringBuilder result = new StringBuilder();

        // get len of pkg
        String L = FieldPacker.genePkgLen(33);  /* 用户字段长33字节 */
        int addr = root.path("termAddr").asInt();
        String addrId = FieldPacker.toHexAddrId(addr);
        List<String> ids = FieldPacker.getMeterIds(root.path("id"));

        result.append("68");
        result.append(L);
        result.append(L);
        result.append("68");
        result.append(DATACODE.getCtrlCode(FUN));   /* 控制符 */
        result.append(addrId);                      /* 地址域 */
        result.append(DATACODE.getAfnCode(FUN));    /* AFN功能码 */
        result.append(DATACODE.getSerial(FUN));     /* 序列号，7单帧需回复，0保留 */
        result.append(DATACODE.getDataId(FUN));     /* 数据单元标识 */
        result.append("563412");                    /* 密码：未用 */
        ids.forEach(result::append);                /* 表序号 */
        result.append("01111111");                  /* 密钥：未用 */
        result.append("00");                        /* 中继方式 */
        result.append(on? "55":"AA");               /* 控制字 */
        result.append("39383736353433323130");      /* 消息认证码PW：未用 */
        result.append(FieldPacker.calcuCs(result)); /* 校验位 */
        result.append("16");                        /* 结束符 */

        return result.toString();
    }

    public static String geneUploadOnOffPkg(JsonNode root, String FUN, boolean on){
        StringBuilder result = new StringBuilder();

        // get len of pkg
        String L = FieldPacker.genePkgLen(29);      /* 用户字段长29字节 */
        int addr = root.path("termAddr").asInt();
        String addrId = FieldPacker.toHexAddrId(addr);
        //List<String> ids = FieldPacker.getMeterIds(root.path("id"));

        result.append("68");
        result.append(L);
        result.append(L);
        result.append("68");
        result.append(DATACODE.getCtrlCode(FUN));   /* 控制符 */
        result.append(addrId);                      /* 地址域 */
        result.append(DATACODE.getAfnCode(FUN));    /* AFN功能码 */
        result.append(DATACODE.getSerial(FUN));     /* 序列号，7单帧需回复，0保留 */
        result.append(DATACODE.getDataId(FUN));     /* 数据单元标识 */
        result.append(on? "55":"AA");               /* 启停设置 */
        result.append("36353433323139383736353433323130");      /* 消息认证码PW：未用 */
        result.append(FieldPacker.calcuCs(result)); /* 校验位 */
        result.append("16");                        /* 结束符 */

        return result.toString();
    }

    public static String geneReadUploadPkg(JsonNode root, String FUN){
        StringBuilder result = new StringBuilder();

        // get len of pkg
        String L = FieldPacker.genePkgLen(12);      /* 用户字段长12字节 */
        int addr = root.path("termAddr").asInt();
        String addrId = FieldPacker.toHexAddrId(addr);

        result.append("68");
        result.append(L);
        result.append(L);
        result.append("68");
        result.append(DATACODE.getCtrlCode(FUN));   /* 控制符 */
        result.append(addrId);                      /* 地址域 */
        result.append(DATACODE.getAfnCode(FUN));    /* AFN功能码 */
        result.append(DATACODE.getSerial(FUN));     /* 序列号，7单帧需回复，0保留 */
        result.append(DATACODE.getDataId(FUN));     /* 数据单元标识 */
        result.append(FieldPacker.calcuCs(result)); /* 校验位 */
        result.append("16");                        /* 结束符 */

        return result.toString();
    }

    /**
     * 下载档案报文生成
     * @return
     */
    public static String geneArchive(JsonNode root, String FUN){
        StringBuilder result = new StringBuilder();

        // get num of meters
        int meterNum = FieldPacker.getMeterNum(root);
        String numStr = FieldPacker.getNBitHexNum(meterNum, 4);
        String L = FieldPacker.getMultiMeterPkgLen(14, meterNum, 22);
        JsonNode archive = root.get("archive");
        int addr = root.path("termAddr").asInt();
        String addrId = FieldPacker.toHexAddrId(addr);

        result.append("68");
        result.append(L);
        result.append(L);
        result.append("68");
        result.append(DATACODE.getCtrlCode(FUN));   /* 控制符 */
        result.append(addrId);                      /* 地址域 */
        result.append(DATACODE.getAfnCode(FUN));    /* AFN功能码 */
        result.append(DATACODE.getSerial(FUN));     /* 序列号，7单帧需回复，0保留 */
        result.append(DATACODE.getDataId(FUN));     /* 数据单元标识 */
        result.append(numStr);                      /* 表数量 */
        result.append(FieldPacker.geneArchive(archive));
        if (archive != null){
            result.append(FieldPacker.geneArchive(archive));
        } else {
            logger.error("No archive msg in ROCKETMQ order");
            return null;
        }
        result.append("36353433323139383736353433323130");
        result.append(FieldPacker.calcuCs(result)); /* 校验位 */
        result.append("16");                        /* 结束符 */

        if (!nullFiledCheck(result.toString())) return null;

        return result.toString();
    }

    /**
     * 关闭档案报文生成
     */
    public static String geneCloseArchive(JsonNode root, String FUN){
        StringBuilder result = new StringBuilder();

        // get num of meters
        int meterNum = FieldPacker.getMeterNum(root);
        String numStr = FieldPacker.getNBitHexNum(meterNum, 4);
        String L = FieldPacker.getMultiMeterPkgLen(14, meterNum, 2);
        List<String> ids = FieldPacker.getMeterIds(root.path("id"));
        int addr = root.path("termAddr").asInt();
        String addrId = FieldPacker.toHexAddrId(addr);

        result.append("68");
        result.append(L);
        result.append(L);
        result.append("68");
        result.append("70");                        /* 控制符 */
        result.append(addrId);                      /* 地址域 */
        result.append(DATACODE.getAfnCode(FUN));    /* AFN功能码 */
        result.append(DATACODE.getSerial(FUN));     /* 序列号，7单帧需回复，0保留 */
        result.append(DATACODE.getDataId(FUN));     /* 数据单元标识 */
        result.append(numStr);                      /* 表数量 */
        ids.forEach(result::append);                /* 表序号 */
        result.append(FieldPacker.calcuCs(result)); /* 校验位 */
        result.append("16");                        /* 结束符 */

        if (!nullFiledCheck(result.toString())) return null;

        return result.toString();
    }

    /**
     * 为了回显数据，没有实质功能的作用
     * @param bytes
     * @return
     */
    public static String bytes2hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        String tmp = null;
        for (byte b : bytes) {
            // 将每个字节与0xFF进行与运算，然后转化为10进制，然后借助于Integer再转化为16进制
            tmp = Integer.toHexString(0xFF & b);
            if (tmp.length() == 1) {
                tmp = "0" + tmp;
            }
            sb.append(tmp);
        }
        return sb.toString();

    }

    private static boolean nullFiledCheck(String toCheck){
        if (toCheck.contains("null")){
            try {
                throw new Exception("package pack failed!!!");
            } catch (Exception e) {
                logger.error("Error pkg packing!!" + toCheck);
                return false;
            }
        }
        return true;
    }
}
