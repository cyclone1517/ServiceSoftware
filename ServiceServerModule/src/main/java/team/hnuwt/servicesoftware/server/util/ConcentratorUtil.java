package team.hnuwt.servicesoftware.server.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.server.model.Logout;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 存储集中器id与SocketChannel的对应关系
 */
public class ConcentratorUtil {
    private static ConcurrentHashMap<Long, SocketChannel> map = new ConcurrentHashMap<>();
    private static Logger logger = LoggerFactory.getLogger(ConcentratorUtil.class);

    public static void add(Long id, SocketChannel sc)
    {
        // 建立新连接前删去旧连接引用
        SocketChannel old = map.remove(id);
        if (old != null && old.isConnected()) {
            try {
                old.close();    /* 切断旧连接 */
            } catch (IOException e) {
                logger.error("DUPLICATE LINK FORM SAME COLLECTOR! AND CANNOT CLOSE", e);
            }
        }
        map.put(id, sc);
    }

    /**
     * 检测不同IP的集中器是否配置了相同ID，暂不启用
     */
    public static boolean containsDuplicate(Long id, SocketChannel sc){
        SocketChannel old = map.get(id);
        if (old == null || !old.isConnected()) return false;  /* 不存在该ID的连接 */
        else {
            try {
                InetAddress newAddr = ((InetSocketAddress) sc.getRemoteAddress()).getAddress();
                InetAddress oldAddr = ((InetSocketAddress) old.getRemoteAddress()).getAddress();
                return newAddr != oldAddr;
            } catch (IOException e) {
                logger.error("CANNOT GET DUPLICATE JUDGEMENT", e);
                return false;
            }
        }
    }

    public static SocketChannel get(Long id)
    {
        return map.get(id);
    }

    public static boolean remove(Long id)
    {
        SocketChannel sk = map.remove(id);
        int realId = Integer.parseInt(Long.toHexString(id),16);
        try {
            if (sk != null && sk.isConnected()) {
                sk.close();
                logger.info("CLOSED AN INVALID SOCKET @#@ id: " + realId );
                return true;
            }
            logger.info("NO INVALID SOCKET @#@ id: " + realId);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("FAILED CLOSED AN INVALID SOCKET! @#@ id: " + realId);
            return false;
        }
    }

    public static boolean removeAndOK(Long id)
    {
        return remove(id);
    }

    public static List<Logout> findLogout(SocketAddress sAddr){
        List<Logout> result = new ArrayList<>();
        for (Map.Entry<Long, SocketChannel> entry: map.entrySet())
        {
            try {
                if (entry.getValue().isConnected() && sAddr == entry.getValue().getRemoteAddress()){
                    result.add(new Logout(entry.getKey(), ((InetSocketAddress)sAddr).getPort()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
