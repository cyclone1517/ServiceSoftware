package team.hnuwt.servicesoftware.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import team.hnuwt.servicesoftware.model.ListImformation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yuanlong Chen
 * PkgExpUtil的帮助类
 */
public class PkgExpHelper {

    public static List<ListImformation> geneListImformations(JsonNode rptField){
        List<ListImformation> result = new ArrayList<>();
        if (rptField instanceof ArrayNode){
            for (JsonNode rpf: rptField){
                result.add(geneListInformation(rpf));
            }
        } else {
            result.add(geneListInformation(rptField));
        }
        return result;
    }

    private static ListImformation geneListInformation(JsonNode rpf){
        int start = rpf.get("start").asInt();
        int end = rpf.get("end").asInt();
        String className = rpf.get("className").asText();
        String field = rpf.get("classFld").asText();
        return  new ListImformation(start, end, className, field);
    }
}
