package team.hnuwt.servicesoftware.server.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.applet.Main;
import team.hnuwt.servicesoftware.server.compatible.FortendAgency;
import team.hnuwt.servicesoftware.server.model.Logout;
import team.hnuwt.servicesoftware.server.service.MainReactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 存储集中器id与指向老系统的兼容连接的对应关系
 */
public class CompatibleUtil {
    private static ConcurrentHashMap<Long, SocketChannel> map = new ConcurrentHashMap<>();

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    private final static String APPLICATION_FILE = "application.properties";
    private static String originAddr;
    private static int originPort;

    private static MainReactor mainReactor;

    private static Logger logger = LoggerFactory.getLogger(CompatibleUtil.class);


    static {
        try {
            Properties prop = new Properties();
            prop.load(CompatibleUtil.class.getClassLoader().getResourceAsStream(APPLICATION_FILE));
            originAddr = prop.getProperty("frontend.origin.address");
            originPort = Integer.parseInt(prop.getProperty("frontend.origin.port"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void setMainReactor(MainReactor mr){
        mainReactor = mr;
    }

    public static boolean isUpstream(byte ctrl){
        int v = ctrl & 0xFF;
        int high = Integer.parseInt(hexArray[v>>>4]+"", 16);   /* 取高位 */
        return high > 7;
        // high > 7 来自启动站(D7为1)，否则来自终端
    }

    public static void addConn(Long id)
    {
        // 建立新连接前删去旧连接
        SocketChannel old = map.remove(id);
        if (old != null && old.isConnected()) {
            try {
                old.close();
            } catch (IOException e) {
                logger.error("DUPLICATE COMPATIBLE LINK TO OLD SYSTEM! AND CANNOT CLOSE", e);
            }
        }

        SocketChannel sc = geneNewCompSocket();
        if (sc != null) {
            map.put(id, sc);
        }
    }

    private static SocketChannel geneNewCompSocket(){
        try {
            SocketChannel result = SocketChannel.open();
            result.connect(new InetSocketAddress(originAddr, originPort));
            return result;
        } catch (IOException e) {
            logger.error("Create socket channel failed!", e);
        }
        return null;
    }

    public static SocketChannel get(Long id)
    {
        return map.get(id);
    }

    public static boolean remove(Long id) {
        SocketChannel sk = map.remove(id);
        try {
            if (sk != null && sk.isConnected()) {
                sk.close();
                logger.info("CLOSED AN INVALID SOCKET @#@ id: " + id);
                return true;
            }
            logger.info("NO INVALID SOCKET YET @#@ id: " + id);
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("FAILED CLOSED AN INVALID SOCKET! @#@ id: " + id);
            return false;
        }
    }

    public static void registSocketRead(SocketChannel sc){
        mainReactor.registerRead(sc);
    }
}
