package team.hnuwt.servicesoftware.simulation;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SendMsg2Redis {

    @Test
    public void sendHeartBeat(){
        int rd = 5;
        while (rd-->0) {
            RedisUtil.pushQueue("HeartBeat", "679318");
        }
        RedisUtil.publishData("HEARTBEAT");
    }

    @Test
    public void sendLogin(){
        int rd = 5;
        while (rd-->0) {
            RedisUtil.pushQueue("Login", "679318");
        }
        RedisUtil.publishData("LOGIN");
    }
    @Test
    public void sendData(){
        RedisUtil.pushQueue("Login", "679318");
        RedisUtil.publishData("DATA");
    }

    @Test
    public void sendOfflineOK(){
        List<String> data = new ArrayList<>();
        data.add("679318");
        data.add("679319");
        String jsonData = JSON.toJSONString(data);
        RedisUtil.pushQueue("OFFLINE_RE", jsonData);
        RedisUtil.publishData("OFFLINE_RE");
    }

}
