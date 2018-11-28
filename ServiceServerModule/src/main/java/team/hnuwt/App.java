package team.hnuwt;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import team.hnuwt.service.MainReactor;
import team.hnuwt.util.ConsumerUtil;

public class App {

    public static void main(String[] args) throws IOException
    {
        new Thread(new MainReactor()).start();

        ConsumerUtil consumerUtil = new ConsumerUtil();
        consumerUtil.run();
    }

}
