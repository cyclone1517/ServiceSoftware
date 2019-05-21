package team.hnuwt.servicesoftware.prtcplugin.util;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.prtcplugin.constant.TAG;
import team.hnuwt.servicesoftware.prtcplugin.model.Data;
import team.hnuwt.servicesoftware.prtcplugin.model.LoginData;
import team.hnuwt.servicesoftware.prtcplugin.model.Meter;
import team.hnuwt.servicesoftware.prtcplugin.packet.*;

import java.util.ArrayList;
import java.util.List;

public class DataUtil {

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

        /*
         * 今后心跳包不再走Mysql
         * 而登录包虽然不走Mysql，登录详情仍然需要登录包的信息
         */
        else if (pkg instanceof PacketHeartBeat){
            processHeartBeat(TAG.HEARTBEAT.getStr(), (PacketHeartBeat) pkg);
            //RedisUtil.publishData(TAG.HEARTBEAT.getStr());
        }
        else if (pkg instanceof PacketLogin){
            processLogin(TAG.LOGIN.getStr(), (PacketLogin) pkg);
            RedisUtil.publishData(TAG.LOGIN_PIPE.getStr());
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

    /**
     * @deprecated
     * 旧的心跳解析，要把解析的JavaBean放入Mysql中
     * 但心跳包数量太大，出现性能问题
     */
//    private static void processHeartBeat(String key, PacketHeartBeat p){
//        HeartData data = new HeartData();
//        long addr = p.getAddress();
//        data.setAddr(addr);
//
//        String temp = JSON.toJSONString(data);
//        RedisUtil.pushQueue(key, temp);
//        logger.info("SEND HEARTBEAT FROM:" + addr);
//    }

    /**
     * 更新的心跳解析法，不再存入mysql，Redis便是心跳数据流动的终点
     */
    private static void processHeartBeat(String key, PacketHeartBeat p){
        long addr = p.getAddress();

        // 存入Redis的心跳和登录数据结构
        long currTime = System.currentTimeMillis();
        RedisUtil.putHash(key, addr+"", currTime+"");
        RedisUtil.putSet(TAG.LOGIN.getStr(), addr+"");  /* 有心跳也看作登录 */
        logger.info("STORED HEARTBEAT FROM:" + addr);
    }

    /**
     * 旧的登录处理
     */
//    private static void processLogin(String key, PacketLogin p){
//        LoginData data = new LoginData();
//        long addr = p.getAddress();
//        data.setAddr(addr);
//
//        String temp = JSON.toJSONString(data);
//        RedisUtil.pushQueue(key, temp);
//        logger.info("SEND LOGIN FROM:" + addr);
//    }

    /**
     * 新的登录处理，行为其实和心跳解析一样
     */
    private static void processLogin(String key, PacketLogin p){
        long addr = p.getAddress();

        // 存入Redis的心跳和登录数据结构
        long currTime = System.currentTimeMillis();
        RedisUtil.putHash(TAG.HEARTBEAT.getStr(), addr+"", currTime+"");
        RedisUtil.putSet(key, addr+"");  /* 有心跳也看作登录 */

        // 将登录的设备号推送到同步模块
        List<String> addrList = new ArrayList<>();      /* 虽然这里可以不用数组，但是为了让同步模块的登录登出消息解析能对称，才这么写 */
        addrList.add(addr+"");
        String toSend = JSON.toJSONString(addrList);
        RedisUtil.pushQueue(TAG.LOGIN_PIPE.getStr(), toSend);
        logger.info("STORED LOGIN FROM:" + addr);
    }
}