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

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    private static MainReactor mainReactor;

    private static Logger logger = LoggerFactory.getLogger(CompatibleUtil.class);

    public static void setMainReactor(MainReactor mr){
        mainReactor = mr;
    }

    public static boolean isUpstream(byte ctrl){
        int v = ctrl & 0xFF;
        int high = Integer.parseInt(hexArray[v>>>4]+"", 16);   /* 取高位 */
        return high > 7;
        // high > 7 来自启动站(D7为1)，否则来自终端
    }


    public static void registSocketRead(SocketChannel sc){
        mainReactor.registerRead(sc);
    }
}
