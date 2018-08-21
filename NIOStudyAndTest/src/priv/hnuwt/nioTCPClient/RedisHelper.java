package priv.hnuwt.nioTCPClient;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.Set;

//单例模式
public class RedisHelper {
    private Jedis jedis;
    private static volatile RedisHelper instance = null;
    private int expireSecond;

    private RedisHelper(){
        init();
    }
    private void init() {
        this.expireSecond = 10;
        jedis = new Jedis("localhost");
        //jedis.auth("123456");

        System.out.println("连接成功");
        System.out.println("服务器正在运行" + jedis.ping());
    }

    /**
     * 应用场景：在收到心跳包时使用的方法
     * 具体操作：更新每台设备的心跳包最新接收时间
     * 备    注：使用明文（未转换编码），采用低位在前16进制编号
     * @param pkgCode
     */
    public void updateHeatBeat(String pkgCode) {
        String meterCode = pkgCode.substring(10);
        System.out.println(meterCode);
        jedis.sadd("survivalMeters");
        jedis.expire(meterCode, expireSecond);
        jedis.ttl(meterCode);
    }

    /**
     * 应用场景：批量注册设备到Redis数据库
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
}
