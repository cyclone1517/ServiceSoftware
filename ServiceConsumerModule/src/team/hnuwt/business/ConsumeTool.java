package team.hnuwt.business;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.message.MessageExt;
import team.hnuwt.data.heartBeat.RedisHelper;
import team.hnuwt.data.heartBeat.RedisHelper2;

import java.util.List;

/**
 * 当前例子是PushConsumer用法，使用户感觉消息从RocketMQ服务器推到了客户端
 * 但实际Pushconsumer内部是使用长轮询Pull方式从服务器拉消息，然后回调用户Listener方法
 */
public class ConsumeTool {

    public void start() throws MQClientException {
        /**
         * 一个应用创建一个Consumer，由应用来维护此对象，可以设置为全局对象或单例
         */
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("ConsumerGroup");
        consumer.setNamesrvAddr("115.157.192.49:9876");
        consumer.setInstanceName("consumer01");

        /**
         * 订阅指定topic下tags分别等于tagA或tagB
         */
        consumer.subscribe("MeterData","TagA ||TagB");
        /**
         * 订阅指定topic下所有消息
         * 注意：一个从consumer对象可以订阅多个topic
         */
        //consumer.subscribe("TopicTest2","*");

        consumer.registerMessageListener(new MessageListenerConcurrently() {
            /**
             * 获取Redis工具
             */
            //RedisHelper redisHelper = RedisHelper.getInstance();
            /**
             * 默认msgs里只有一条消息，可以设置consumeMessageBatchMaxSize
             * @param msgs
             * @param consumeConcurrentlyContext
             * @return
             */
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                MessageExt msg = msgs.get(0);
                String msgBody = new String(msg.getBody());
                if (msg.getTopic().equals("MeterData")){
                    //执行MeterData主题的消费逻辑，放入Redis队列即可
                    if (msg.getTags()!=null && msg.getTags().equals("TagA")) {
                        System.out.println("Received: "+ new String(msgBody));
                        RedisHelper2.getJedis().rpush("UpdatedData", msgBody);
                        System.out.println("the new msg has been put into redis UpdatedData");
                    }
                    //else if ...
                }

                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        /**
         * Consumer对象在使用之前必须要调用start初始化，初始化一次即可
         */
        consumer.start();
        System.out.println("ConsumeTool Started");

    }

}
