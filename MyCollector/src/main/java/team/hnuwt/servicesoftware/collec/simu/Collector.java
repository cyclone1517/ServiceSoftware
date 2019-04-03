package team.hnuwt.servicesoftware.collec.simu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Properties;

/**
 * 代理集中器，转发消息到兼容模块
 */
public class Collector implements Runnable{

    private SocketChannel socketChannel;
    private String originAddr;
    private int originPort;

    private static final String APPLICATION_FILE = "application.properties";
    private Logger logger = LoggerFactory.getLogger(Collector.class);
    private ByteBuffer login;
    private ByteBuffer hearbeat;

    public Collector(String addr, int plus){
        init();
    }

    private void init() {
        try {
            Properties prop = new Properties();
            prop.load(Collector.class.getClassLoader().getResourceAsStream(APPLICATION_FILE));
            originAddr = prop.getProperty("server.address");
            originPort = Integer.parseInt(prop.getProperty("server.port"));

            // 仅连接
            openNewLink();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private SocketChannel openNewLink(){
        try {
            socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress(originAddr, originPort));
            //CompatibleUtil.registSocketRead(socketChannel);
            return socketChannel;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public SocketChannel getSocketChannel(){
        if (socketChannel.isConnected()) return socketChannel;
        else return openNewLink();
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

    private void sendMsg(){
        if (!socketChannel.isConnected()){
            //socketChannel.write()
        }
    }

    @Override
    public void run() {

    }
}
