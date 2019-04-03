package team.hnuwt.servicesoftware.collec.simu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.collec.util.ByteBuilder;

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
    private static String originAddr;
    private static int originPort;

    private static final String APPLICATION_FILE = "application.properties";
    private Logger logger = LoggerFactory.getLogger(Collector.class);
    private boolean isLogin = false;

    private String login;
    private String hearbeat;

    /* 加载类变量的静态代码块 */
    static {
        Properties prop = new Properties();
        try {
            prop.load(Collector.class.getClassLoader().getResourceAsStream(APPLICATION_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }
        originAddr = prop.getProperty("server.address");
        originPort = Integer.parseInt(prop.getProperty("server.port"));
    }

    public Collector(String login, String hearbeat){
        init();
        this.login = login;
        this.hearbeat = hearbeat;
    }

    private void init() {
        openNewLink();
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

    private void sendMsg(String msg){
        try {
            socketChannel.write(ByteBuffer.wrap(new ByteBuilder(msg).getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true){
            if (!isLogin){
                sendMsg(login);
                isLogin = true;
                logger.info("Login: " + login);
            } else {
                sendMsg(hearbeat);
                logger.info("HeartBeat: " + hearbeat);
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                logger.info("", e);
            }
        }
    }
}
