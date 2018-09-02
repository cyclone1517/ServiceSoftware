package team.hnuwt.data.perm;

import com.alibaba.rocketmq.client.exception.MQClientException;
import team.hnuwt.business.ConsumeTool;

/**
 * 数据流向：启动过程1和2
 * RocketMQ ->(过程1) -> Redis -> (过程2) -> Mysql
 */
public class ConsumeDemo {

    public static void main(String args[]) throws MQClientException {
        /**
         * 启动取出RcoketMQ的消费者进程
         * 被消费的消息将会被放入Redis队列中
         */
        ConsumeTool consumeTool = new ConsumeTool();
        consumeTool.start();

        /**
         * 运行数据工人管理器
         * 把Redis队列中的消息搬运到Mysql中
         */
        Thread t = new Thread(new DataWorkerManager());
        t.start();
    }
}
