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
        String L = FieldPacker.getReadMeterPkgLen(15, meterNum, 2);
        List<String> ids = FieldPacker.getMeterIds(root.path("id"));
        String addr = root.path("addr").asText();

        result.append("68");
        result.append(L);       /* extraLen 抄表方式1字节 + 表数量2字节 */
        result.append(L);
        result.append("68");
        result.append(DATACODE.getCtrlCode(TAG));   /* 控制符 */
        result.append(addr);                        /* 地址域 */
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
        String L = FieldPacker.getReadMeterPkgLen(20, meterNum, 2);
        String addr = root.path("addr").asText();

        result.append("68");
        result.append(L);
        result.append(L);
        result.append("68");
        result.append(DATACODE.getCtrlCode(FUN));   /* 控制符 */
        result.append(addr);                        /* 地址域 */
        result.append(DATACODE.getAfnCode(FUN));    /* AFN功能码 */
        result.append("70");                        /* 序列号，7单帧需回复，0保留 */
        result.append(DATACODE.getDataId(FUN));     /* 数据单元标识 */
        result.append(DATACODE.getReadMtd(FUN));    /* 抄读方式 */
        result.append(FieldPacker.calcuCs(result)); /* 校验位 */
        result.append("16");                        /* 结束符 */

        return result.toString();
    }

    public static String geneReadVersionPkg(JsonNode root, String FUN){
        return geneReadTimePkg(root, FUN);
    }

    public static String geneCtrlTimePkg(JsonNode root, String FUN){
        return "";
    }

    public static String geneCtrlOnOffPkg(JsonNode root, String FUN, boolean on){
        StringBuilder result = new StringBuilder();

        // get len of pkg
        String L = FieldPacker.getOnOffPkgLen();
        String addr = root.path("addr").asText();
        List<String> ids = FieldPacker.getMeterIds(root.path("id"));

        result.append("68");
        result.append(L);
        result.append(L);
        result.append("68");
        result.append(DATACODE.getCtrlCode(FUN));   /* 控制符 */
        result.append(addr);                        /* 地址域 */
        result.append(DATACODE.getAfnCode(FUN));    /* AFN功能码 */
        result.append("70");                        /* 序列号，7单帧需回复，0保留 */
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
}
