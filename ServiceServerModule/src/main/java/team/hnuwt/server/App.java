package team.hnuwt.server;

import java.io.IOException;

import team.hnuwt.server.service.MainReactor;
import team.hnuwt.server.util.ConsumerUtil;

public class App {

    public static void main(String[] args) throws IOException
    {
        new Thread(new MainReactor()).start();

        ConsumerUtil consumerUtil = new ConsumerUtil();
        consumerUtil.run();
    }

}
