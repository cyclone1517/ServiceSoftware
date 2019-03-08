package team.hnuwt.servicesoftware.server.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yuanlong chen
 * calculate for some field to be assembled
 */
public class FieldPacker {

    private static Logger logger = LoggerFactory.getLogger(FieldPacker.class);

    public static int getMeterNum(JsonNode root){
        try {
            String numStr = root.path("num").asText();
            return Integer.parseInt(numStr);
        }catch (Exception e){
            logger.error("wrong num type, cannot be transfered to a num");
            return 0;
        }
    }

    /**
     * 计算包长度
     * @param originLen 固定初始长度(从0到数据单元标识长18字节，CS+末位长2字节)
     * @param extraLen  额外数据单元长(如水表数量占2字节)
     * @param num       数据单元个数
     * @param unitLen   每个单元长度
     * @return
     */
    public static String getPkgLen(int originLen, int extraLen, int num, int unitLen){
        String result;
        int len = originLen;
        len += extraLen;
        len += num * unitLen;
        if (len > 1400){
            logger.warn("cannot pack such a long package");
        }
        result =  Integer.toHexString(len);
        switch (result.length()){
            case 1:
                logger.warn("package length calculate error!");
                return "0000";
            case 2:
                return result + "00";
            case 3:
                return result.substring(1) + "0" + result.substring(0,1);
            case 4:
                return result.substring(2) + result.substring(0, 2);
            default:
                logger.warn("package length calculate error!");
                return "0000";
        }
    }

    public static String calcuCs(StringBuilder sb){
        String csStr = sb.toString().substring(12);
        char[] csArr = csStr.toCharArray();

        byte sum = 0;
        for (char cs: csArr){
            sum += cs;
        }

        return sum+"";
    }

    public static List<String> getMeterIds(JsonNode ids){
        List<String> result = new ArrayList<>();

        if (ids instanceof ArrayNode){
            for (JsonNode id: ids){
                result.add(id.asText());
            }
        } else {
            result.add(ids.asText());
        }
        return result;
    }
}
