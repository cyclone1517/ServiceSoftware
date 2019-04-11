package team.hnuwt.servicesoftware.compatible.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.compatible.util.RedisUtil;
import team.hnuwt.servicesoftware.compatible.util.Subscriber;

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
