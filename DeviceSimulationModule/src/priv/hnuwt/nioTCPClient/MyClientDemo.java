package priv.hnuwt.nioTCPClient;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

//不用UDP，统一用TCP，才能检测到是该条连接出了问题
//UDP无法接收并不能推导出TCP不能接收
public class MyClientDemo {
    
    public static void main(String[] args) throws IOException {
        RedisHelper redisHelper = RedisHelper.getInstance();
        Set<String> devices = redisHelper.getRegisteredDevices();
        Iterator<String> it = devices.iterator();
        int cnt=0;
        int n=5;       //最大测试数量
        while (it.hasNext() && n-->0){
            String device = it.next();
            //System.out.println("[MyClientDemo]"+device + "has been created");
            new Thread(new MyClient(device)).start();
            cnt++;
        }
        System.out.println(cnt + " devices has been connected");
    }
}
