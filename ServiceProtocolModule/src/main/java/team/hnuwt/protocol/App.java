package team.hnuwt.protocol;

import team.hnuwt.protocol.service.DataCarryManager;
import team.hnuwt.protocol.util.ConsumerUtil;

public class App {

    public static void main(String[] args)
    {

        ConsumerUtil consumerUtil = new ConsumerUtil();
        consumerUtil.run();

        new DataCarryManager().run();
    }

}
