package team.hnuwt.servicesoftware.synchronizer.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

public class HandlerUtil {

    private static ObjectMapper mapper = new ObjectMapper();

    public static String geneMsg(List<String> loginIds, int state){
        ObjectNode result = mapper.createObjectNode();
        ArrayNode addrs = mapper.createArrayNode();

        loginIds.forEach(addrs::add);
        result.set("addr", addrs);
        result.put("state", state);
        return result.toString();
    }

    public static String packAddrToHex(long cdId){
        String cdIdStr = Long.toHexString(cdId);
        int len = 10 - cdIdStr.length();
        StringBuilder zeros = new StringBuilder();
        while (len-- > 0){
            zeros.append(0);
        }
        return zeros + cdIdStr;
    }
}
