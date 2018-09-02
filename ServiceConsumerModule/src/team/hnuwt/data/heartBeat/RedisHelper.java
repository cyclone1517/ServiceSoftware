package team.hnuwt.data.heartBeat;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.text.SimpleDateFormat;
import java.util.*;

//单例模式
public class RedisHelper {
    private Jedis jedis;
    private static volatile RedisHelper instance = null;

    private RedisHelper(){
        init();
    }   //初始化的时候就建立连接
    private void init() {
        jedis = new Jedis("localhost");
        System.out.println("连接成功");
        System.out.println("服务器正在运行" + jedis.ping());
    }

    /**
     * 应用场景：更新UpdatedDevices中的心跳包时间
     * 具体操作：更新每台设备的心跳包最新接收时间，格式为Map<"设备号","系统秒数">
     * 备    注：使用明文（未转换编码），采用低位在前16进制编号
     * @param pkgCode
     */
    public void updateHeatBeat(String pkgCode) {
        String meterCode = pkgCode.substring(10);
        //System.out.println("[RedisHelper]" + meterCode);  //需要添0占位
        Map<String, String> heartBeatTimeMap = new HashMap<>();
        heartBeatTimeMap.put(meterCode, sysTimeToInt());
        jedis.hmset("UpdatedDevices", heartBeatTimeMap);
    }

    /**
     * 应用场景：批量注册设备到RegisteredDevices集合中
     * 具体操作：使用Redis事务功能
     * @return
     */
    public void registDevice(List<String> devices){
        Transaction trans = jedis.multi();
        int length = devices.size();
        for (int i=0; i<length; i++){
            trans.sadd("RegisteredDevices", devices.get(i));
        }
        trans.exec();
        jedis.save();
    }

    /**
     * 应用场景：获得所有注册过的Redis数据库的设备
     * 具体操作：提取出RegisteredDevices集合中的所有设备名
     * @return
     */
    public Set<String> getRegisteredDevices(){
        Set<String> devices = jedis.smembers("RegisteredDevices");
        return devices;
    }

    /**
     * 应用场景：在消费者中收到消息则立即存到Redis，再由线程池处理放入sql消息队列
     * 具体操作：收到的消息缓存到UpdatedData中，右边入队左边出队
     */
    public void pushUpdatedDataQueue(String data){
        System.out.println("[RedisHelper]111");
        try{
            jedis.rpush("UpdatedData", data);
        } catch(Exception e) {
            e.printStackTrace();
        }
        //jedis.sadd("random", (new Random().nextInt(1000))+"");
    }

    /**
     * 应用场景：取出Redis队列中的数据
     * 具体操作：阻塞状态只能按队列取
     * @return
     */
//    public String popUpdatedDataQueue(){
//        return jedis.blpop(0,"UpdatedData");
//    }
//    public List<String> popUpdatedDataQueueList(int consumeNum){
//        List<String> dataList = new ArrayList<>();
//        String temp;
//        while(consumeNum-->0){
//            temp = jedis.lpop("UpdatedData");
//            if ("nil".equals(temp))break;
//            else dataList.add(temp);
//        }
//        return dataList;
//    }
    public List<String> popUpdatedDataQueueList(int consumeNum){
        List<String> result = jedis.blpop(0,"UpdatedData");
        return result;
    }

    private String sysTimeToInt(){
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");//设置日期格式
        String sysTime = df.format(new Date()); //new Date()为获取当前系统时间
        String[] sfm = sysTime.split(":");
        int hour = Integer.parseInt(sfm[0]);
        int minute = Integer.parseInt(sfm[1]);
        int second = Integer.parseInt(sfm[2]);
        return ( hour * 3600 + minute * 60 + second ) + "";
    }

    public static RedisHelper getInstance(){
        if(instance == null){
            synchronized (RedisHelper.class) {
                if (instance == null){
                    instance = new RedisHelper();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args){
        for(int i=0; i<10; i++){
            RedisHelper.getInstance().pushUpdatedDataQueue("111");
        }

    }
}
