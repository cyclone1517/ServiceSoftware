package team.hnuwt.servicesoftware.server.util;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.server.constant.down.DATACODE;

import java.util.List;

public class PkgPackUtil {

    private static Logger logger = LoggerFactory.getLogger(PkgPackUtil.class);

    public static String getQueryPkg(JsonNode root, String fn){
        switch (Integer.parseInt(fn)){
            case 1:
            case 2:
                return geneReadMeterPkg(root, "READ_METER");
            case 3:
                return geneReadTimePkg(root, "READ_TIME");
            case 4:
                return geneReadVersionPkg(root, "READ_VERSION");
            default:
                logger.error("no such packet function type:" + fn);
                return "";
        }
    }

    public static String getCtrlPkg(JsonNode root, String fn){
        switch (Integer.parseInt(fn)){
            case 1:
                return geneCtrlTimePkg(root, "CTRL_TIME");
            case 2:
            case 3:
                return geneCtrlOnOffPkg(root, "CTRL_ONOFF");
            default:
                logger.error("no such packet function type:" + fn);
                return "";
        }
    }

    private static String geneReadMeterPkg(JsonNode root, String FUN){
        StringBuilder result = new StringBuilder();

        // get num of meters
        int meterNum = FieldPacker.getMeterNum(root);
        String L = FieldPacker.getPkgLen(20, 3, meterNum, 2);
        List<String> ids = FieldPacker.getMeterIds(root.path("id"));
        String addr = root.path("addr").asText();

        result.append("68");
        result.append(L);       /* extraLen 抄表方式1字节 + 表数量2字节 */
        result.append(L);
        result.append("68");
        result.append(DATACODE.getCtrlCode(FUN));   /* 控制符 */
        result.append(addr);                        /* 地址域 */
        result.append(DATACODE.getAfnCode(FUN));    /* AFN功能码 */
        result.append("70");                        /* 序列号，7单帧需回复，0保留 */
        result.append(DATACODE.getDataId(FUN));     /* 数据单元标识 */
        result.append(DATACODE.getReadMtd(FUN));    /* 抄读方式 */
        result.append(meterNum);                    /* 表数量 */
        ids.forEach(result::append);                /* 表编号 */
        result.append(FieldPacker.calcuCs(result));             /* 校验位 */
        result.append("16");                        /* 结束符 */

        return result.toString();
    }

    private static String geneReadTimePkg(JsonNode root, String FUN){
        StringBuilder result = new StringBuilder();

        // get num of meters
        int meterNum = FieldPacker.getMeterNum(root);
        String L = FieldPacker.getPkgLen(20, 3, meterNum, 2);
        String addr = root.path("addr").asText();

        result.append("68");
        result.append(L);       /* extraLen 抄表方式1字节 + 表数量2字节 */
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

    private static String geneReadVersionPkg(JsonNode root, String FUN){
        return geneReadTimePkg(root, FUN);
    }

    private static String geneCtrlTimePkg(JsonNode root, String FUN){
        return "";
    }

    private static String geneCtrlOnOffPkg(JsonNode root, String FUN){
        return "";
    }

}
