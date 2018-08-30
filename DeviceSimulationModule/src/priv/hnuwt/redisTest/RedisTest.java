package priv.hnuwt.redisTest;
import redis.clients.jedis.Jedis;

public class RedisTest {
    public static void main(String[] args){
        Jedis jedis = new Jedis("localhost");
        jedis.auth("runoob");
        System.out.println("连接成功");
        System.out.println("服务器正在运行" + jedis.ping());
    }
}
