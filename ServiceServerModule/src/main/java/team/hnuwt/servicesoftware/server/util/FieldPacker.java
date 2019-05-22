package team.hnuwt.servicesoftware.server.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yuanlong chen
 * calculate for some field to be assembled
 */
public class FieldPacker {

    private static Logger logger = LoggerFactory.getLogger(FieldPacker.class);
    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static int getMeterNum(JsonNode root){
        try {
            String numStr = root.path("count").asText();
            return Integer.parseInt(numStr);
        }catch (Exception e){
            logger.error("wrong num type, cannot be transfered to a num", e);
            return 0;
        }
    }

    /**
     * 计算包长度
     * @param originLen 固定初始长度(从0到数据单元标识长18字节，CS+末位长2字节)
     * @param num       数据单元个数
     * @param unitLen   每个单元长度(字节)
     * @return
     */
    public static String getMultiMeterPkgLen(int originLen, int num, int unitLen){
        int len = originLen;
        len += num * unitLen;
        len = (len<<2) + 1;     /* 编码规律 */
        if (len > 1400){
            logger.warn("cannot pack such a long package");
        }
        return formativeLen(len);
    }

    public static String genePkgLen(int byteLen){
        byteLen = (byteLen<<2) + 1;  /* 编码规律 */
        return formativeLen(byteLen);
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

    /**
     * 计算校验和（为组装下发报文用）
     * @param sb
     * @return
     */
    public static String calcuCs(StringBuilder sb){
        String csStr = sb.toString().substring(12);

        int sum = 0;
        for (int i = 0; i < csStr.length(); i += 2)
        {
            sum += Integer.parseInt(csStr.substring(i, i + 2), 16);
        }

        return getNBitHexNumNoRvs(sum % 256, 2);
    }

    /**
     * 计算校验和（为检测收到的消息用）
     * @param pkg
     * @return
     */
    public static boolean isCorrectAgencyCs(ByteBuilder pkg){
        byte sum = 0;
        int len = pkg.length();
        for (int i = 0; i < 13; i++)
        {
            byte b = pkg.getByte(i);
            sum += b;
        }
        return sum == pkg.getByte(13);
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

    public static String getNBitHexNum(int num, int bit){   /* 自带反转 */
        StringBuilder result = new StringBuilder();
        result.append(Integer.toHexString(num));
        while (result.length() < bit) result.insert(0, "0");
        return reverseEnd(result.toString());
    }

    private static String getNBitHexNumNoRvs(int num, int bit){   /* 无反转 */
        StringBuilder result = new StringBuilder();
        result.append(Integer.toHexString(num));
        while (result.length() < bit) result.insert(0, "0");
        return result.toString();
    }

    public static String reverseEnd(String code){
        StringBuilder result = new StringBuilder();

        int len = code.length();
        for (int i=len-2; i>=0; i-=2){
            result.append(code, i, i+2);
        }

        return result.toString();
    }

    public static String toIntAddrId(String addr){
        String addrId = reverseEnd(addr).substring(2,6);
        try {
            return Integer.parseInt(addrId, 16) +"";
        } catch (NumberFormatException e){
            return null;
        }
    }

    public static String toHexAddrId(int addr){
        return "0000" + getNBitHexNum(addr, 4) + "00";
    }

    public static String getSysTime(){
        return df.format(new Date());
    }

    public static String parseHexStr2Byte(String hexStr){
        StringBuilder sb = new StringBuilder();
        if (hexStr.length() < 1) return null;

        char[] hexArr = hexStr.toCharArray();
        for (char i: hexArr){
            int val = Integer.parseInt(i+"", 16);
            StringBuilder binStr = new StringBuilder(Integer.toBinaryString(val));
            while (binStr.length() < 4){
                binStr.insert(0, 0);
            }
            sb.append(binStr);
        }

        return sb.toString();
    }

    public static String geneArchive(JsonNode archive){
        StringBuilder sb =  new StringBuilder();
        if (archive instanceof ArrayNode){
            for (JsonNode ac: archive){
                sb.append(geneSgArchive(ac));
            }
        } else {
            sb.append(geneSgArchive(archive));
        }
        return sb.toString();
    }

    public static String geneSgArchive(JsonNode ac){
        StringBuilder result = new StringBuilder();
        JsonNode id = ac.get("id");
        JsonNode madd = ac.get("madd");
        JsonNode prtc = ac.get("prtc");
        JsonNode port = ac.get("port");
        JsonNode cadd = ac.get("cadd");

        result.append((id==null)? "": id.asText())          /* 表序号 */
                .append((madd==null)? "": madd.asText())    /* 表地址 */
                .append("33000010")                         /* 测量点性质、接线方式 */
                .append("01000000")                         /* 线路表箱编号 */
                .append((port==null)? "": port.asText())    /* 端口 */
                .append((prtc==null)? "": prtc.asText())    /* 协议 */
                .append((cadd==null)? "": cadd.asText());   /* 测量地址 */

        return result.toString();
    }

    /**
     * 当连接标识为行政编码+集中器ID时，得到能读懂的日志需要重新计算，截断行政码（区号）
     */
    @Deprecated
    public static int getRealId(Long id){
        String rstStr = Long.toHexString(id);
        rstStr = rstStr.substring(0, rstStr.length()-4);        /* 舍弃区号 */
        return Integer.parseInt(rstStr,16);
    }

    /* 集中器ID字节范围 */
    private static final int startId = 9;
    private static final int end = 12;

    public static long getId(ByteBuilder message){
        return message.BINToLong(startId, end);
    }

    /**
     * 把16进制字符串转换成字节数组
     * @return byte[]
     */
    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static int toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }
}
