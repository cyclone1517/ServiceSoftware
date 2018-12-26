package team.hnuwt.servicesoftware.server.message;

import team.hnuwt.servicesoftware.server.util.ProduceUtil;

/**
 * 命令处理工具类
 */
public class OrderHandler implements Runnable {
    private String order;

    public OrderHandler(String order)
    {
        this.order = order;
    }

    @Override
    public void run()
    {
        ProduceUtil.addQueue(order);
    }
}
