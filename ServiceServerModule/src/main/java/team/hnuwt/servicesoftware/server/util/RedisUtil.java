package team.hnuwt.servicesoftware.server.util;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;

/**
 * Redis操作工具类
 */
public class RedisUtil {
    private final static String APPLICATION_FILE = "application.properties";

    private static JedisPool jedisPool = null;

    private static Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    static {
        try {
            Properties props = new Properties();
            props.load(RedisUtil.class.getClassLoader().getResourceAsStream(APPLICATION_FILE));
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(Integer.valueOf(props.getProperty("jedis.pool.maxActive")));
            config.setMaxIdle(Integer.valueOf(props.getProperty("jedis.pool.maxIdle")));
            config.setMaxWaitMillis(Long.valueOf(props.getProperty("jedis.pool.maxWait")));
            config.setTestOnBorrow(Boolean.valueOf(props.getProperty("jedis.pool.testOnBorrow")));
            config.setTestOnReturn(Boolean.valueOf(props.getProperty("jedis.pool.testOnReturn")));
            jedisPool = new JedisPool(config, props.getProperty("redis.ip"),
                    Integer.valueOf(props.getProperty("redis.port")),
                    Integer.valueOf(props.getProperty("redis.timeout")));
            logger.info("Redis has been set up");
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    private static Jedis getJedis()
    {
        return jedisPool.getResource();
    }

    private static void returnJedis(Jedis jedis)
    {
        if (jedis != null)
        {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * 更新心跳包
     * 
     * @param device
     */
    public static void updateHeatBeat(String device)
    {
        Jedis jedis = getJedis();
        jedis.hset("DeviceHeartBeat", device, String.valueOf(System.currentTimeMillis()));
        returnJedis(jedis);
    }

    /**
     * 注册多个设备
     * 
     * @param devices
     */
    public static void registDevice(List<String> devices)
    {
        try {
            Jedis jedis = getJedis();
            Transaction trans = jedis.multi();
            for (int i = 0, len = devices.size(); i < len; i++)
            {
                trans.sadd("RegisteredDevices", devices.get(i));
            }
            trans.exec();
            jedis.save();
            trans.close();
            returnJedis(jedis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前已经注册的设备
     * 
     * @return
     */
    public static Set<String> getRegisteredDevices()
    {
        Jedis jedis = getJedis();
        Set<String> devices = jedis.smembers("RegisteredDevices");
        returnJedis(jedis);
        return devices;
    }
}
