package team.hnuwt.servicesoftware.distdevice.util;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * RocketMq生产者工具类
 */
public class InnerProduceUtil {
    private static DefaultMQProducer producer;

    private final static String UPSTREAM = "UPSTREAM";
    private final static String TAG = "Tag";
    private final static String ORDER = "Order";

    private final static String APPLICATION_FILE = "application.properties";
    private static Properties props;

    private static Logger logger = LoggerFactory.getLogger(InnerProduceUtil.class);

    static {
        // 加载消息队列生产
        try {
            props = new Properties();
            props.load(InnerProduceUtil.class.getClassLoader().getResourceAsStream(APPLICATION_FILE));
        } catch (IOException e) {
            logger.error("", e);
        }
        producer = new DefaultMQProducer(props.getProperty("rocketmq.innerProduce.producerGroup"));
        producer.setNamesrvAddr(props.getProperty("rocketmq.innerProduce.address"));
        producer.setInstanceName(props.getProperty("rocketmq.innerProduce.producerName"));
        producer.setVipChannelEnabled(false);
        try {
            producer.start();
        } catch (MQClientException e) {
            logger.error("", e);
        }
    }

    public static void addQueue(String topic, String tag, String data){
        try {
            Message message = new Message(topic, tag, ORDER, (data).getBytes());
            producer.send(message);
        } catch (MQClientException e) {
            logger.error("", e);
        } catch (RemotingException e) {
            logger.error("", e);
        } catch (MQBrokerException e) {
            logger.error("", e);
        } catch (InterruptedException e) {
            logger.error("", e);
        }
        logger.info("ADD ROCKETMQ @#@TOPIC:" + topic + " @#@TAG:" + tag + " @#@MSG:" + data);
    }
}
