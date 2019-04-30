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
    private int loop;
    private int interval;
    private int delay;
    private boolean openLogger;
    private int currId;

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


    /**
     * 带不同延时的集中器
     * @param login
     * @param hearbeat
     * @param loop
     * @param delay 延时参数
     */
    public Collector(String login, String hearbeat, int loop, int interval, int delay, boolean openLogger, int currId){
        init();
        this.login = login;
        this.hearbeat = hearbeat;
        this.loop = loop;
        this.interval = interval;
        this.delay = delay;
        this.openLogger = openLogger;
        this.currId = currId;
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
            socketChannel.write(ByteBuffer.wrap(new ByteBuilder(msg, currId).getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (loop-->0){
            if (!isLogin){
                sendMsg(login);
                isLogin = true;
                if (openLogger)logger.info("Login: " + login);
            } else {
                sendMsg(hearbeat);
                if (openLogger)logger.info("HeartBeat: " + hearbeat);
//
//                sendMsg("685500550068880000EE03008C60100001070100010010080000861D16");
//                if (openLogger)logger.info("AutoUpload: " + hearbeat);
            }
            try {
                Thread.sleep(interval * 1000 + delay);
            } catch (InterruptedException e) {
                logger.info("", e);
            }
        }
        try {
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
