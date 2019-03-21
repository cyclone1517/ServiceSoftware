package team.hnuwt.servicesoftware.distdevice.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ProduceTestUtil {

    public static String geneReadMeterJson(){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();

        //String addr = "3607000000";
        String addr = "0000FD0300";
        String num = "2";
        String[] ids = {"0008","0008"};

        root.put("addr", addr);
        root.put("num", num);

        ArrayNode idArr = mapper.createArrayNode();
        for (String id: ids) idArr.add(id);

        root.set("id", idArr);
        return root.toString();
    }

    public static String geneCtrlOnOffJson(String onOffID){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();

        String addr = "0000FD0300";
        String id = onOffID;

        root.put("addr", addr);
        root.put("id", onOffID);

        return root.toString();
    }

    public static String geneUploadOnOffJson(String onOffID){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();

        String addr = "1021";
        String id = onOffID;

        root.put("addr", addr);
        root.put("id", id);

        return root.toString();
    }

    public static String geneReadUploadJson(String onOffID){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();

        String addr = "1021";
        String id = onOffID;

        root.put("addr", addr);
        root.put("id", id);

        return root.toString();
    }
}
