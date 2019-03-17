package team.hnuwt.servicesoftware.prtcplugin.util;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.prtcplugin.model.Data;
import team.hnuwt.servicesoftware.prtcplugin.model.HeartData;
import team.hnuwt.servicesoftware.prtcplugin.model.Meter;
import team.hnuwt.servicesoftware.prtcplugin.packet.Packet;
import team.hnuwt.servicesoftware.prtcplugin.packet.PacketAutoUpload;
import team.hnuwt.servicesoftware.prtcplugin.packet.PacketHeartBeat;

import java.util.ArrayList;
import java.util.List;

public class DataUtil {

    //private static final String DATA = "Data";
    private static final Logger logger = LoggerFactory.getLogger(DataUtil.class);

    public static void putDataToRedis(Packet pkg){
        if (pkg instanceof PacketAutoUpload){
            processAutoUpload("Data", (PacketAutoUpload) pkg);
            RedisUtil.publishData("data");  /* 这里是推送消息，但由于未知原因无法添加新方法 */
        } else if (pkg instanceof PacketHeartBeat){
            processHeartBeat("HeartBeat", (PacketHeartBeat) pkg);
            RedisUtil.publishData("heartbeat");
        }
    }

    private static void processAutoUpload(String key, PacketAutoUpload p){
        List<Data> datas = new ArrayList<>();
        for (Meter meter : p.getMeter()) {
            Data data = new Data(p.getAddress(), meter.getId(), meter.getData(), meter.getState());
            datas.add(data);
        }
        RedisUtil.pushQueue(key, JSON.toJSONString(datas));
        logger.info("AUTOUPLOADED NUM: " + datas.size());
    }

    private static void processHeartBeat(String key, PacketHeartBeat p){
        HeartData data = new HeartData();
        long addr = p.getAddress();
        data.setAddr(addr);

        String temp = JSON.toJSONString(data);
        RedisUtil.pushQueue(key, temp);
        logger.info("SEND HEARTBEAT FROM:" + addr);
    }
}
