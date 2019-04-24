package team.hnuwt.servicesoftware.prtcplugin.util;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.prtcplugin.constant.TAG;
import team.hnuwt.servicesoftware.prtcplugin.model.Data;
import team.hnuwt.servicesoftware.prtcplugin.model.HeartData;
import team.hnuwt.servicesoftware.prtcplugin.model.LoginData;
import team.hnuwt.servicesoftware.prtcplugin.model.Meter;
import team.hnuwt.servicesoftware.prtcplugin.packet.*;

import java.util.ArrayList;
import java.util.List;

public class DataUtil {

    //private static final String DATA = "Data";
    private static final Logger logger = LoggerFactory.getLogger(DataUtil.class);

    public static void distributeData(Packet pkg){
        if (pkg instanceof PacketAutoUpload){
            processAutoUpload(TAG.AUTO_UPLOAD.getStr(), (PacketAutoUpload) pkg);    /* 存入Redis */
            RedisUtil.publishData(TAG.AUTO_UPLOAD.getStr());            /* 通知Redis */
            PublishUtil.publishAutoUpload((PacketAutoUpload) pkg);      /* 通知中间服务 */
        }
        else if (pkg instanceof PacketReadMeter){
            processReadMeter(TAG.READ_METER.getStr(), (PacketReadMeter) pkg);
            RedisUtil.publishData(TAG.READ_METER.getStr());
        }
        else if (pkg instanceof PacketHeartBeat){
            processHeartBeat(TAG.HEARTBEAT.getStr(), (PacketHeartBeat) pkg);
            RedisUtil.publishData(TAG.HEARTBEAT.getStr());
        }
        else if (pkg instanceof PacketLogin){
            processLogin(TAG.LOGIN.getStr(), (PacketLogin) pkg);
            RedisUtil.publishData(TAG.LOGIN.getStr());
        }
    }

    private static void processAutoUpload(String key, PacketAutoUpload p){
        List<Data> datas = new ArrayList<>();
        for (Meter meter : p.getMeter()) {
            Data data = new Data(p.getAddress(), meter.getId(), meter.getData(), meter.getState());
            datas.add(data);
        }
        RedisUtil.pushQueue(key, JSON.toJSONString(datas));
        logger.info("UPLOADED METER NUM: " + datas.size());
    }

    private static void processReadMeter(String key, PacketReadMeter p){
        List<Data> datas = new ArrayList<>();
        for (Meter meter : p.getMeter()) {
            Data data = new Data(p.getAddress(), meter.getId(), meter.getData(), meter.getState());
            datas.add(data);
        }
        RedisUtil.pushQueue(key, JSON.toJSONString(datas));
        logger.info("UPLOADED NUM: " + datas.size());
    }

    private static void processHeartBeat(String key, PacketHeartBeat p){
        HeartData data = new HeartData();
        long addr = p.getAddress();
        data.setAddr(addr);

        String temp = JSON.toJSONString(data);
        RedisUtil.pushQueue(key, temp);
        logger.info("SEND HEARTBEAT FROM:" + addr);
    }

    private static void processLogin(String key, PacketLogin p){
        LoginData data = new LoginData();
        long addr = p.getAddress();
        data.setAddr(addr);

        String temp = JSON.toJSONString(data);
        RedisUtil.pushQueue(key, temp);
        logger.info("SEND LOGIN FROM:" + addr);
    }
}
