package team.hnuwt.meter.util;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

public class RedisUtil {
    private final static String APPLICATION_FILE = "application.properties";
    private final static String METER_DATA = "MeterData";

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
        if (jedis != null)
        {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * �����ݰ����뵽redis���ݿ���
     * 
     * @param packageCode
     */
    public static void pushQueue(String packageCode)
    {
        Jedis jedis = getJedis();
        jedis.rpush(METER_DATA, packageCode);
        returnJedis(jedis);
    }

    /**
     * �����ݰ��������뵽redis���ݿ���
     * 
     * @param packageCodes
     */
    public static void pushQueue(List<String> packageCodes)
    {
        Jedis jedis = getJedis();
        Pipeline pipe = jedis.pipelined();
        for (String packageCode : packageCodes)
        {
            pipe.rpush(METER_DATA, packageCode);
        }
        pipe.sync();
        returnJedis(jedis);
    }

    /**
     * ��redis�л�ȡ����
     * 
     * @return
     */
    public static List<String> getData()
    {
        Jedis jedis = getJedis();
        List<String> data = jedis.brpop(1, METER_DATA);
        returnJedis(jedis);
        return data;
    }
}
