package team.hnuwt.servicesoftware.server.compatible;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Properties;

public class CollecSimulator implements Runnable{

    private SocketChannel socketChannel;

    private static final String APPLICATION_FILE = "application.properties";
    private static Logger logger = LoggerFactory.getLogger(CollecSimulator.class);


    @Override
    public void run() {
        init();
    }

    private void init() {
        try {
            Properties prop = new Properties();
            prop.load(CollecSimulator.class.getClassLoader().getResourceAsStream(APPLICATION_FILE));
            String originAddr = prop.getProperty("frontend.origin.address");
            int originPort = Integer.parseInt(prop.getProperty("frontend.origin.port"));

            socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress(originAddr, originPort));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
