package team.hnuwt.servicesoftware.compatible.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPubSub;
import team.hnuwt.servicesoftware.compatible.message.CancelAgentLinkHandler;

import java.io.IOException;
import java.util.Properties;

public class Subscriber extends JedisPubSub {

    DataProcessThreadUtil dptu = new DataProcessThreadUtil();
    private int batchNum;

    private final static String APPLICATION_FILE = "application.properties";
    private static Properties props;
    private static Logger logger = LoggerFactory.getLogger(Subscriber.class);

    public Subscriber(){
        try {
            props = new Properties();
            props.load(Subscriber.class.getClassLoader().getResourceAsStream(APPLICATION_FILE));
        } catch (IOException e) {
            logger.error("", e);
        }
        batchNum = Integer.parseInt(props.getProperty("db.batch"));
    }

    @Override
    public void onMessage(String channel, String message) {       //收到消息会调用
        String[] cancelList = message.split("\\|\\|");
        DataProcessThreadUtil.getExecutor().execute(new CancelAgentLinkHandler(cancelList));
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {    //订阅了频道会调用
        System.out.println(String.format("subscribe redis channel success, channel @#@ %s, subscribedChannels @#@ %d",
                channel, subscribedChannels));
    }
    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {   //取消订阅 会调用
        System.out.println(String.format("unsubscribe redis channel, channel %s, subscribedChannels %d",
                channel, subscribedChannels));

    }
}
