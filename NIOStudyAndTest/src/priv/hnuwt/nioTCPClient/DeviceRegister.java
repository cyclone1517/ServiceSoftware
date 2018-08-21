package priv.hnuwt.nioTCPClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DeviceRegister {
    static Random random = new Random();

    public static String randomOx(){
        int temp = random.nextInt(100000000);
        return  temp + "";
    }

    public static void main(String[] args){
        //生成设备数据
        List<String> devices = new ArrayList<>();
        for(int i=0; i<100; i++){
            devices.add(randomOx());
        }
        System.out.println("finish devices creating");

        //初始化Redis工具
        RedisHelper redisHelper = RedisHelper.getInstance();
        redisHelper.registDevice(devices);
    }
}
