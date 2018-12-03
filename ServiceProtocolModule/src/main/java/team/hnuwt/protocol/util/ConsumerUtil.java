package team.hnuwt.protocol.util;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.message.MessageExt;

import team.hnuwt.protocol.service.ProtocolService;

public class ConsumerUtil implements Runnable {
    private DefaultMQPushConsumer consumer;

    private final static String APPLICATION_FILE = "application.properties";
    private static Properties props;

    private final static String UPSTREAM = "Upstream";
    private final static String TAG = "Tag";

    private static Logger logger = LoggerFactory.getLogger(ConsumerUtil.class);

    public ConsumerUtil()
    {
        try {
            props = new Properties();
            props.load(ConsumerUtil.class.getClassLoader().getResourceAsStream(APPLICATION_FILE));
        } catch (IOException e) {
            logger.error("", e);
        }
        try {
            consumer = new DefaultMQPushConsumer(props.getProperty("rocketmq.consumer.consumerGroup"));
            consumer.setNamesrvAddr(props.getProperty("rocketmq.consumer.address"));
            consumer.setInstanceName(props.getProperty("rocketmq.consumer.consumerName"));
            consumer.setVipChannelEnabled(false);
            consumer.subscribe(UPSTREAM, TAG);
            consumer.registerMessageListener(new MessageListenerConcurrently() {

                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                        ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                    for (MessageExt msg : msgs)
                    {
                        String msgBody = new String(msg.getBody());
                        DataProcessThreadUtil.getExecutor().execute(new ProtocolService(msgBody));
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });
        } catch (MQClientException e) {
            logger.error("", e);
        }
    }

    @Override
    public void run() {
        try {
            consumer.start();
        } catch (MQClientException e) {
            logger.error("", e);
        }
    }

}
