package team.hnuwt.servicesoftware.autoupload.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.autoupload.model.EncodeFormat;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class PkgExplainUtil {

    private final static String XML_PATH = "pkgDesc.xml";

    private static HashMap<String, List<String>> totalFieldName;    /* 数据字段名 */
    private static HashMap<String, List<Integer>> totalFieldLen;    /* 每个字段长 */
    private static HashMap<String, List<EncodeFormat>> totalFieldCode;  /* 每个字段编码 */
    private static HashMap<String, String> busiName;        /* 业务编号对应实体名称 */

    private static Logger logger = LoggerFactory.getLogger(PkgExplainUtil.class);

    static {
        try {
            loadPropXml();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String xmlToJson() throws IOException {
        InputStream in = PkgExplainUtil.class.getResourceAsStream(XML_PATH);
        String xml = IOUtils.toString(in);
        JSONObject xmlJsonObject = XML.toJSONObject(xml);
        return xmlJsonObject.toString();
    }

    /**
     * 从XML配置文件中加载插件
     */
    private static void loadPropXml() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String data = xmlToJson();
        JsonNode root = mapper.readTree(data);
        JsonNode headProp = root.path("headProp");
        JsonNode userProp = root.path("userProp");

        //组合数据包结构的临时变量
        List<String> headNameList = new ArrayList<>();
        String headLenStr = "";
        String headCodeStr = "";

        //获取数据包头相关信息
        JsonNode headNames = headProp.path("prop").path("fn");
        for (JsonNode hn: headNames){   /*首部字段名*/
            headNameList.add(hn.textValue());
        }
        headLenStr = headProp.path("fldlen").asText();
        headCodeStr = headProp.path("encode").asText();

        //获取用户包相关信息并组合
        JsonNode props = userProp.path("prop");
        for (JsonNode prop: props){
            List<String> fieldNameList = new ArrayList<>(headNameList);
            String fieldLenStr = headLenStr;
            String fieldCodeStr = headCodeStr;

            String id = prop.path("id").asText();   /* 读取业务id */
            String function = prop.path("function").asText();   /* 业务功能名 */

            JsonNode fieldNames = prop.path("fieldNames");  /*  用户字段名 */
            for (JsonNode fn: fieldNames) fieldNameList.add(fn.asText());

            fieldLenStr += prop.path("fldlen").asText();
            fieldCodeStr += prop.path("encode").asText();

            //存入数据
            busiName.put(id, function);
            totalFieldName.put(id, fieldNameList);
            totalFieldLen.put(id, fldLenToArray(fieldLenStr));
            totalFieldCode.put(id, encodeToArray(fieldCodeStr));
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

    public static String getBusiName(String id){
        return busiName.get(id);
    }

    public static String[] getFiledName(String id){
        return (String[]) totalFieldName.get(id).toArray();
    }

    public static Integer[] getFiledLen(String id){
        return (Integer[]) totalFieldLen.get(id).toArray();
    }

    public static EncodeFormat[] getFiledCode(String id){
        return (EncodeFormat[]) totalFieldCode.get(id).toArray();
    }
}
