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
     * @param num       数据单元个数
     * @param unitLen   每个单元长度
     * @return
     */
    public static String getReadMeterPkgLen(int originLen, int num, int unitLen){
        int len = originLen;
        len += num * unitLen;
        len = (len<<2) + 1;     /* 编码规律 */
        if (len > 1400){
            logger.warn("cannot pack such a long package");
        }
        return formativeLen(len);
    }

    public static String getOnOffPkgLen(){
        int len = 33;        /* 33字节 */
        len = (len<<2) + 1;  /* 编码规律 */
        return formativeLen(len);
    }

    /**
     * 将长度字段组装为需要的位数，一般为4位
     * @param len
     * @return
     */
    private static String formativeLen(int len){
        return formativeLen(len, 4);
    }
    private static String formativeLen(int len, int lenbit){
        String result =  Integer.toHexString(len);
        switch (result.length()){
            case 1:
                logger.warn("package length calculate error! too short: " + result.length());
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

        int sum = 0;
        for (int i = 0; i < csStr.length(); i += 2)
        {
            sum += Integer.parseInt(csStr.substring(i, i + 2), 16);
        }

        return Integer.toHexString(sum % 256);
    }

    public static List<String> getMeterIds(JsonNode ids){
        List<String> result = new ArrayList<>();

        if (ids instanceof ArrayNode){
            for (JsonNode id: ids){
                result.add(reverseEnd(id.asText()));
            }
        } else {
            result.add(reverseEnd(ids.asText()));
        }
        return result;
    }

    public static String getNBitHexNum(int num, int bit){
        StringBuilder result = new StringBuilder();
        result.append(Integer.toHexString(num));
        while (result.length() < bit) result.insert(0, "0");
        return reverseEnd(result.toString());
    }

    public static String reverseEnd(String code){
        StringBuilder result = new StringBuilder();

        int len = code.length();
        for (int i=len-2; i>=0; i-=2){
            result.append(code, i, i+2);
        }

        return result.toString();
    }
}