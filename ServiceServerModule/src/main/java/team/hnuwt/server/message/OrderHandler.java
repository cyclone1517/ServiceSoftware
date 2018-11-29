package team.hnuwt.server.message;

import team.hnuwt.server.util.ProduceUtil;

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
