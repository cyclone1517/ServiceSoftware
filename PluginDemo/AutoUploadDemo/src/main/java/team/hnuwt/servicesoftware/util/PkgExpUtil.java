package team.hnuwt.servicesoftware.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.model.EncodeFormat;
import team.hnuwt.servicesoftware.model.ListImformation;
import team.hnuwt.servicesoftware.packet.Packet;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author yuanlong Chen
 * 从配置文件加载数据包结构的常量信息
 */
public class PkgExpUtil {

    private final static String XML_PATH = "pkgDesc.xml";
    private final static String PKG_PATH = "team.hnuwt.servicesoftware.packet";

    private static HashMap<Long, List<String>> totalFieldName;    /* 数据字段名 */
    private static HashMap<Long, List<Integer>> totalFieldLen;    /* 每个字段长 */
    private static HashMap<Long, List<EncodeFormat>> totalFieldCode;  /* 每个字段编码 */
    private static HashMap<Long, String> busiName;        /* 业务编号对应实体名称 */
    private static HashMap<Long, Boolean> isBulkMap;        /* 批量数据包标识位 */
    private static HashMap<Long, List<ListImformation>> rptFieldMap; /* 批量读取字段信息 */

    private static Logger logger = LoggerFactory.getLogger(PkgExpUtil.class);

    static {
        try {
            init();
            loadPropXml();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String xmlToJson() throws IOException {
        InputStream in = PkgExpUtil.class.getClassLoader().getResourceAsStream(XML_PATH);
        if (null == in){
            logger.info("The XML path for package description is not available");
            return null;
        }
        String xml = IOUtils.toString(in);
        JSONObject xmlJsonObject = XML.toJSONObject(xml);
        return xmlJsonObject.toString();
    }

    private static void init() {
        totalFieldCode = new HashMap<>();
        totalFieldName = new HashMap<>();
        totalFieldLen = new HashMap<>();
        busiName = new HashMap<>();
        isBulkMap = new HashMap<>();
        rptFieldMap = new HashMap<>();
    }

    /**
     * 从XML配置文件中加载插件
     */
    private static void loadPropXml() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String data = xmlToJson();
        JsonNode root = mapper.readTree(data);
        JsonNode headProp = root.path("PackageInfos").path("headProp");
        JsonNode userProp = root.path("PackageInfos").path("userProp");

        //组合数据包结构的临时变量
        List<String> headNameList = new ArrayList<>();
        String headLenStr = "";
        String headCodeStr = "";

        //获取数据包头相关信息
        JsonNode headNames = headProp.path("prop").get("fn");
        if (null != headNames) {
            for (JsonNode hn : headNames) {   /*首部字段名*/
                headNameList.add(hn.textValue());
            }
        }
        headLenStr = headProp.path("fldlen").asText();
        headCodeStr = headProp.path("encode").asText();

        //获取用户包相关信息并组合
        JsonNode props = userProp.path("prop");
        for (JsonNode prop: props){
            List<String> fieldNameList = new ArrayList<>(headNameList);
            String fieldLenStr = headLenStr;
            String fieldCodeStr = headCodeStr;

            Long id = Long.parseLong(prop.path("id").asText());   /* 读取业务id */
            String function = prop.path("function").asText();   /* 业务功能名 */

            JsonNode fieldNames = prop.path("fieldNames").get("fn");  /*  用户字段名 */
            if (null != fieldNames){
                for (JsonNode fn: fieldNames){
                    fieldNameList.add(fn.asText());
                }
            }

            fieldLenStr += prop.path("fldlen").asText();
            fieldCodeStr += prop.path("encode").asText();

            String isBulkStr = prop.path("isBulk").asText();   /* 读取批量数据标识位 */
            Boolean isBulk = Boolean.valueOf(isBulkStr);

            if (isBulk) {
                JsonNode rptField = prop.path("RptField").get("rpf");      /* 读取重复字段信息区 */
                List<ListImformation> listImformations = PkgExpHelper.geneListImformations(rptField);
                rptFieldMap.put(id, listImformations);
            }

            //存入数据
            busiName.put(id, function);
            totalFieldName.put(id, fieldNameList);
            totalFieldLen.put(id, fldLenToArray(fieldLenStr));
            totalFieldCode.put(id, encodeToArray(fieldCodeStr));
            isBulkMap.put(id, isBulk);
        }
    }

    private static List<Integer> fldLenToArray(String fldlen){
        List<Integer> result = new ArrayList<>();
        char[] lenArr = fldlen.toCharArray();
        for (char la: lenArr){
            result.add(Integer.parseInt(la+""));
        }
        return result;
    }

    private static List<EncodeFormat> encodeToArray(String code){
        List<EncodeFormat> result = new ArrayList<>();
        char[] lenArr = code.toCharArray();
        for (char la: lenArr){
            if (la == '0') result.add(EncodeFormat.BIN);
            else result.add(EncodeFormat.BCD);
        }
        return result;
    }

    public static String getBusiName(long id){
        return busiName.get(id);
    }

    public static String[] getFiledName(long id){
        List<String> result = totalFieldName.get(id);
        if (null != result) {   /* 涉及强制转型，做非空判断 */
            return result.toArray(new String[0]);
        }
        return null;
    }

    public static Integer[] getFiledLen(long id){
        List<Integer> result = totalFieldLen.get(id);
        if (null != result) {
            return result.toArray(new Integer[0]);
        }
        return null;
    }

    public static EncodeFormat[] getFiledCode(long id){
        List<EncodeFormat> result = totalFieldCode.get(id);
        if (null != result) {
            return result.toArray(new EncodeFormat[0]);
        }
        return null;
    }

    public static ListImformation[] getReptedListInfo(long id){
        List<ListImformation> result = rptFieldMap.get(id);
        if (null != result){
            return result.toArray(new ListImformation[0]);
        }
        return null;
    }

    public static Packet getPacketModel(long id){
        try {
            String clazzName = PKG_PATH + ".Packet" + getBusiName(id);
            Class clazz = Class.forName(clazzName);
            return (Packet) clazz.newInstance();
        } catch (ClassNotFoundException e) {
            logger.error("This Packet Model dose not exist, Please check configuration or the class path");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isBulk(long id){
        return isBulkMap.get(id);
    }

}
