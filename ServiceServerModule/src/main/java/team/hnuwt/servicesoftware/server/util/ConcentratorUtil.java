package team.hnuwt.servicesoftware.server.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.server.model.Logout;

import java.io.IOException;
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
        // 建立新连接前删去旧连接
        SocketChannel old = map.remove(id);
        if (old != null && old.isConnected()) {
            try {
                old.close();
            } catch (IOException e) {
                logger.error("DUPLICATE LINK FORM SAME COLLECTOR! AND CANNOT CLOSE", e);
            }
        }
        map.put(id, sc);
    }

    public static SocketChannel get(Long id)
    {
        return map.get(id);
    }

    public static boolean remove(Long id)
    {
        SocketChannel sk = map.remove(id);
        try {
            if (sk != null && sk.isConnected()) {
                sk.close();
                logger.info("CLOSED AN INVALID SOCKET @#@ id: " + id );
                return true;
            }
            logger.info("NO INVALID SOCKET YET @#@ id: " + id);
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("FAILED CLOSED AN INVALID SOCKET! @#@ id: " +id);
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
                if (sAddr == entry.getValue().getRemoteAddress()){
                    result.add(new Logout(entry.getKey(), ((InetSocketAddress)sAddr).getPort()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
