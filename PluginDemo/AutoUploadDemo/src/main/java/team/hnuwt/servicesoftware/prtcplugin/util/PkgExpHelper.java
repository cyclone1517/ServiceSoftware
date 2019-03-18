package team.hnuwt.servicesoftware.prtcplugin.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import team.hnuwt.servicesoftware.prtcplugin.model.ListInformation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yuanlong Chen
 * PkgExpUtil的帮助类
 */
public class PkgExpHelper {

    public static List<ListInformation> geneListImformations(JsonNode rptField, String MODEL_PATH){
        List<ListInformation> result = new ArrayList<>();
        if (rptField instanceof ArrayNode){
            for (JsonNode rpf: rptField){
                result.add(geneListInformation(rpf, MODEL_PATH));
            }
        } else {
            result.add(geneListInformation(rptField, MODEL_PATH));
        }
        return result;
    }

    private static ListInformation geneListInformation(JsonNode rpf, String MODEL_PATH){
        int start = rpf.get("start").asInt();
        int end = rpf.get("end").asInt();
        String className = MODEL_PATH + "." + rpf.get("className").asText();
        String field = rpf.get("classFld").asText();
        return  new ListInformation(start, end, className, field);
    }
}
