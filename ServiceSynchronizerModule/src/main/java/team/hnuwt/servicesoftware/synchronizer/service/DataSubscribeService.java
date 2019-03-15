package team.hnuwt.servicesoftware.synchronizer.service;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import team.hnuwt.servicesoftware.synchronizer.model.Data;
import team.hnuwt.servicesoftware.synchronizer.util.DataProcessThreadUtil;
import team.hnuwt.servicesoftware.synchronizer.util.RedisUtil;
import team.hnuwt.servicesoftware.synchronizer.util.Subscriber;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DataSubscribeService implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(DataSubscribeService.class);


    private Subscriber subscriber;
    private String channel;

    public DataSubscribeService(Subscriber subscriber, String channel){
        this.subscriber = subscriber;
        this.channel =channel;
    }

    /**
     * 从Redis数据库中获取数据
     */
    @Override
    public void run()
    {

        RedisUtil.subscribe(subscriber, channel);
        // 接下来的工作交给 Subscriber 对象来完成

    }

}
