package team.hnuwt.servicesoftware.distdevice.test;

import com.alibaba.rocketmq.shade.com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import team.hnuwt.servicesoftware.disdevice.mode.AddrNumIds;
import team.hnuwt.servicesoftware.disdevice.mode.Archive;
import team.hnuwt.servicesoftware.disdevice.mode.Archive_download;

import java.util.List;

public class ProduceTestUtil {

    public static String geneReadMeterJson(String addr){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();

        //String addr = "3607000000";
        String num = "1";
        String[] ids = {"0008"};

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

    public static String geneArchive(String addr, int num, List<Archive> archives){
        Archive_download ad = new Archive_download();
        ad.setAddr(addr);
        ad.setNum(num + "");
        ad.setArchives(archives);
        return JSON.toJSONString(ad);
    }

    public static String geneCloseArchive(String addr, List<String> meterIds){
        AddrNumIds ad = new AddrNumIds();
        ad.setAddr(addr);
        ad.setNum(meterIds.size());
        ad.setId(meterIds);
        return JSON.toJSONString(ad);
    }


}
