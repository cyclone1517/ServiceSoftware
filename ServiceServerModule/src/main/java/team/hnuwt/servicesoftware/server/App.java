package team.hnuwt.servicesoftware.server;

import java.io.IOException;

import team.hnuwt.servicesoftware.server.service.MainReactor;
import team.hnuwt.servicesoftware.server.util.ConsumerUtil;

public class App {

    public static void main(String[] args) throws IOException
    {
        new Thread(new MainReactor()).start();

        ConsumerUtil consumerUtil = new ConsumerUtil();
        consumerUtil.run();
    }

}
