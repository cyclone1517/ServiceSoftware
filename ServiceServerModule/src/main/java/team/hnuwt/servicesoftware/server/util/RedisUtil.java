package team.hnuwt.servicesoftware.server.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Set;

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
            logger.info("redis has been set up");
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
        if (jedis != null) {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * 将数据放入到redis数据库中
     * @param key
     * @param data
     */
    public static void pushQueue(String key, String data)
    {
        Jedis jedis = getJedis();
        jedis.rpush(key, data);
        returnJedis(jedis);
    }

    /**
     * 将数据批量放入到redis数据库中
     * @param key
     * @param list
     */
    public static void pushQueue(String key, List<String> list)
    {
        Jedis jedis = getJedis();
        Pipeline pipe = jedis.pipelined();
        for (String s : list)
        {
            pipe.rpush(key, s);
        }
        pipe.sync();
        returnJedis(jedis);
    }

    /**
     * 从redis中获取数据
     * @param key
     * @return
     */
    public static String getData(String key)
    {
        String data = null;
        Jedis jedis = getJedis();
        List<String> datas = jedis.brpop(1, key);
        if (datas != null)
        {
            for (int i = 1, len = datas.size(); i < len; i += 2)
            {
                data = datas.get(i);
            }
        }
        returnJedis(jedis);
        return data;
    }

    /**
     * 通过订阅机制通知数据已经抵达
     * channel 用于指定推送的目的模块
     *      "notifier": 数据同步模块（默认）
     *      "agency": 兼容模块
     */
    public static void publishData(String dataType){
        Jedis jedis = getJedis();
        jedis.publish("notifier", dataType);
        returnJedis(jedis);
    }

    public static void publishData(String topic, String dataType){
        Jedis jedis = getJedis();
        jedis.publish(topic, dataType);
        returnJedis(jedis);
    }

    /**
     * Map相关，用于存心跳时间
     * @param key
     * @param data
     */
    public static void putHash(String key, String field, String data){
        Jedis jedis = getJedis();
        jedis.hset(key, field, data);
        returnJedis(jedis);
    }

    public static String getHash(String key, String field){
        String result;

        Jedis jedis = getJedis();
        result = jedis.hget(key, field);
        returnJedis(jedis);

        return result;
    }

    /**
     * Set相关
     */
    public static void putSet(String key, String data){
        Jedis jedis = getJedis();
        jedis.sadd(key, data);
        returnJedis(jedis);
    }

    public static Set<String> getSet(String key){
        Set<String> result;

        Jedis jedis = getJedis();
        result = jedis.smembers(key);
        returnJedis(jedis);

        return result;
    }
}
