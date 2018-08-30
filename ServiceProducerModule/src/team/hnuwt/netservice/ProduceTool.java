package team.hnuwt.netservice;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.exception.RemotingException;

public class ProduceTool {

    private DefaultMQProducer producer;

    public ProduceTool(String produceGroup, String namesrvAddr, String instantName) {
        /**
         * 一个应用创建一个Producer，由应用来维护此对象
         */
        producer = new DefaultMQProducer(produceGroup);
        producer.setNamesrvAddr(namesrvAddr);
        producer.setInstanceName(instantName);
    }

    public void addQueueMsg(String data) throws MQClientException, InterruptedException {
        /**
         * Producer使用前要调用start初始化，初始化一次即可，不可以每次发送消息都start
         */
        producer.start();

        try {
            Message msg = new Message("MeterData",
                    "TagA",
                    "OrderID001",
                    (data).getBytes());
            SendResult sendResult = producer.send(msg);
            System.out.println(sendResult);
        } catch (RemotingException | MQBrokerException e) {
            e.printStackTrace();
        }

        producer.shutdown();
    }
}