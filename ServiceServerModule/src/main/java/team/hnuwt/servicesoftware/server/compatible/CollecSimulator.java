package team.hnuwt.servicesoftware.server.compatible;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.server.util.ByteBuilder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Properties;

/**
 * 集中器模拟类，假设本机是集中器1021
 */
@Deprecated
public class CollecSimulator{

    private SocketChannel socketChannel;

    private static final String APPLICATION_FILE = "application.properties";
    private static Logger logger = LoggerFactory.getLogger(CollecSimulator.class);

    public CollecSimulator(){
        init();
    }

    private void init() {
        try {
            Properties prop = new Properties();
            prop.load(CollecSimulator.class.getClassLoader().getResourceAsStream(APPLICATION_FILE));
            String originAddr = prop.getProperty("frontend.origin.address");
            int originPort = Integer.parseInt(prop.getProperty("frontend.origin.port"));

            String login = "683500350068C90000FD0300027010000100125E16";
            socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress(originAddr, originPort));
            ByteBuilder b = new ByteBuilder(login);
            socketChannel.write(ByteBuffer.wrap(b.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String TOPIC, String msg){
        try {
            ByteBuilder b = new ByteBuilder(msg);
            socketChannel.write(ByteBuffer.wrap(b.getBytes()));
        } catch (IOException e) {
            logger.error("", e);
        }
        logger.info("SEND @#@TOPIC:" + TOPIC + " @#@MSG:" + msg);
    }

    public SocketChannel getSocketChannel(){
        return socketChannel;
    }
}
