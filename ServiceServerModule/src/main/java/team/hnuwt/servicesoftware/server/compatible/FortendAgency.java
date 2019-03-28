package team.hnuwt.servicesoftware.server.compatible;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.server.service.MainReactor;
import team.hnuwt.servicesoftware.server.util.ByteBuilder;
import team.hnuwt.servicesoftware.server.util.CompatibleUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Properties;

/**
 * 代理集中器，转发消息到兼容模块
 */
public class FortendAgency {

    private SocketChannel socketChannel;
    private String originAddr;
    private int originPort;

    private static final String APPLICATION_FILE = "application.properties";
    private static Logger logger = LoggerFactory.getLogger(FortendAgency.class);

    public FortendAgency(){
        init();
    }

    private void init() {
        try {
            Properties prop = new Properties();
            prop.load(FortendAgency.class.getClassLoader().getResourceAsStream(APPLICATION_FILE));
            originAddr = prop.getProperty("compatible.agency.address");
            originPort = Integer.parseInt(prop.getProperty("compatible.agency.port"));

            // 仅连接
            openNewAgency();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 如果现有连接有效则直接返回
     * 无效则新建再返回
     * @return
     */
    public SocketChannel getAlivedCompatibleLink(){
        if (socketChannel.isConnected()) return socketChannel;
        else {
            logger.info("UPDATED NEW AGENCY SOCKET");
            return openNewAgency();
        }
    }

    private SocketChannel openNewAgency(){
        try {
            socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress(originAddr, originPort));
            CompatibleUtil.registSocketRead(socketChannel);
            return socketChannel;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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
}
