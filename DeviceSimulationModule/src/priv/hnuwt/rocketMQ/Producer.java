package priv.hnuwt.rocketMQ;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.exception.RemotingException;

public class Producer {
    public static void main(String[] args) throws MQClientException, InterruptedException {
        /**
         * 一个应用创建一个Producer，由应用来维护此对象
         */
        DefaultMQProducer producer = new DefaultMQProducer("ProducerGroup");
        producer.setNamesrvAddr("localhost:9876");
        producer.setInstanceName("Producer");

        /**
         * Producer使用前要调用start初始化，初始化一次即可，不可以每次发送消息都start
         */
        producer.start();

        for (int i = 0; i < 5; i++) {
            try {
                {
                    Message msg = new Message("TopicTest1",
                            "TagA",
                            "OrderID001",
                            ("Hello MetaQ").getBytes());
                    SendResult sendResult = producer.send(msg);
                    System.out.println(sendResult);
                }

                {
                    Message msg = new Message("TopicTest2",
                            "TagB",
                            "OrderID034",
                            ("Hello MetaQ").getBytes());
                    SendResult sendResult = producer.send(msg);
                    System.out.println(sendResult);
                }
            } catch (RemotingException | MQBrokerException e) {
                e.printStackTrace();
            }
        }

        producer.shutdown();
    }
}