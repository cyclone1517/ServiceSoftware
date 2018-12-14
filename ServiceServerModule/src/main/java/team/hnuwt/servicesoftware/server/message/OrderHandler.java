package team.hnuwt.servicesoftware.server.message;

import team.hnuwt.servicesoftware.server.util.ProduceUtil;

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
