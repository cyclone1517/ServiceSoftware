package team.hnuwt.servicesoftware.util;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.model.Data;
import team.hnuwt.servicesoftware.model.Meter;
import team.hnuwt.servicesoftware.packet.Packet;
import team.hnuwt.servicesoftware.packet.PacketAutoUpload;
import team.hnuwt.servicesoftware.packet.PacketHeartBeat;
import team.hnuwt.servicesoftware.plugin.util.RedisUtil;

import java.util.ArrayList;
import java.util.List;

public class DataUtil {

    private static final String DATA = "Data";
    private static final Logger logger = LoggerFactory.getLogger(DataUtil.class);

    public static void putDataToRedis(Packet pkg){
        if (pkg instanceof PacketAutoUpload){
            processAutoUpload((PacketAutoUpload) pkg);
        } else if (pkg instanceof PacketHeartBeat){
            processHeartBeat((PacketHeartBeat) pkg);
        }
    }

    private static void processAutoUpload(PacketAutoUpload p){
        List<Data> datas = new ArrayList<>();
        for (Meter meter : p.getMeter()) {
            Data data = new Data(p.getAddress(), meter.getId(), meter.getData(), meter.getState());
            datas.add(data);
        }
        //String finalData = JSON.toJSONString(datas);
        RedisUtil.pushQueue(DATA, JSON.toJSONString(datas));
        logger.info("the number of data : " + datas.size());
    }

    private static void processHeartBeat(PacketHeartBeat p){
        logger.warn("certain data to store should be settled...");
    }
}
