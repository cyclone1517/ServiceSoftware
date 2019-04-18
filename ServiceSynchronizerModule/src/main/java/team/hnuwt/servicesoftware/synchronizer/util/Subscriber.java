package team.hnuwt.servicesoftware.synchronizer.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPubSub;
import team.hnuwt.servicesoftware.synchronizer.constant.MESSAGE;
import team.hnuwt.servicesoftware.synchronizer.handler.*;

import java.io.IOException;
import java.util.Properties;

public class Subscriber extends JedisPubSub {

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
        System.out.println(String.format("receive redis published message, @#@ channel: %s, @#@ message: %s", channel, message));
        MESSAGE msg = MESSAGE.getMSG(message.toUpperCase());
        if (msg == MESSAGE.DATA){
            DataProcessThreadUtil.getExecutor().execute(new DataHandler(batchNum));
        }
        else if (msg == MESSAGE.AUTOUPLOAD){
            DataProcessThreadUtil.getExecutor().execute(new DataHandler(batchNum));
        }
        else if (msg == MESSAGE.HEARTBEAT) {
            DataProcessThreadUtil.getExecutor().execute(new HeartBeatHandler(batchNum));
        }
        else if (msg == MESSAGE.LOGIN) {
            DataProcessThreadUtil.getExecutor().execute(new LoginHandler(batchNum, 1));  // state: 1-登录 0-掉线 2-登出
        }
        else if (msg == MESSAGE.LOGOUT){
            DataProcessThreadUtil.getExecutor().execute(new LoginHandler(batchNum, 2));
        }
        else if (msg == MESSAGE.OFFLINE_RE) {
            DataProcessThreadUtil.getExecutor().execute(new OfflineReHandler(batchNum));
        }
        else if (msg == MESSAGE.DUPLICATE) {
            DataProcessThreadUtil.getExecutor().execute(new DuplicateHandler(batchNum));
        }
    }
    @Override
    public void onSubscribe(String channel, int subscribedChannels) {    //订阅了频道会调用
        System.out.println(String.format("subscribe redis channel success, channel %s, subscribedChannels %d",
                channel, subscribedChannels));
    }
    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {   //取消订阅 会调用
        System.out.println(String.format("unsubscribe redis channel, channel %s, subscribedChannels %d",
                channel, subscribedChannels));

    }
}
