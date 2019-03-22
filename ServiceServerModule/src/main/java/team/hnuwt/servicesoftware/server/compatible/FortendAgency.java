package team.hnuwt.servicesoftware.server.compatible;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.server.service.MainReactor;
import team.hnuwt.servicesoftware.server.util.ByteBuilder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Properties;

/**
 * 代理集中器，转发消息
 */
public class FortendAgency {

    private SocketChannel socketChannel;

    private static final String APPLICATION_FILE = "application.properties";
    private static Logger logger = LoggerFactory.getLogger(FortendAgency.class);

    public FortendAgency(){
        init();
    }

    private void init() {
        try {
            Properties prop = new Properties();
            prop.load(FortendAgency.class.getClassLoader().getResourceAsStream(APPLICATION_FILE));
            String originAddr = prop.getProperty("frontend.origin.address");
            int originPort = Integer.parseInt(prop.getProperty("frontend.origin.port"));

            // 仅连接
            socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress(originAddr, originPort));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void directSend(String TOPIC, String msg){
        try {
            ByteBuilder b = new ByteBuilder(msg);
            socketChannel.write(ByteBuffer.wrap(b.getBytes()));
        } catch (IOException e) {
            logger.error("", e);
        }
        logger.info("SEND @#@TOPIC:" + TOPIC + " @#@MSG:" + msg);
    }

    public void close(){
        if (socketChannel.isConnected()){
            try {
                socketChannel.close();
                logger.info("CLOSED COMPATIBLE LINK SUCCESSFULLY");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public SocketChannel getSocketChannel(){
        return socketChannel;
    }
}
