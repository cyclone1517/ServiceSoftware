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

    /**
     * 1. 如果第一次登录（old == null）直接登录
     * 2. 如果新来自不同的端口（包括同IP不同Port，或不同IP相同Port）
     *      先切断旧连接，允许登录
     * 3. 来自新同一地址，同一IP不允许重复登录
     */
    public static void add(Long id, SocketChannel sc)
    {
        // 建立新连接前删去旧连接引用
        SocketChannel old = map.remove(id);
        if (old != null) {
            if (old.isConnected() && sc.isConnected() && diffPort(old, sc)) {    /* 如果两个连接都在连接中，且来自不同端口 */
                try {
                    old.close();    /* 切断旧连接 */
                } catch (IOException e) {
                    logger.error("DUPLICATE LINK FORM SAME COLLECTOR! AND CANNOT CLOSE", e);
                }
            }
        }
        map.put(id, sc);
    }

    /**
     * 获取正在登录的重复ID集中器
     * @return 已经登录，被重复的老集中器套接字
     */
    public static SocketChannel getOriginDuplicateSocket(Long id, SocketChannel sc){
        SocketChannel old = map.get(id);
        if (old != null && old.isConnected() && sc.isConnected() && diffAddress(old, sc)){  /* 如果两个链接都在连接中，且来自不同IP */
            return old;
        }
        return null;
    }

    /**
     *
     * 只要 RemoteAddress 不同，端口一定不同
     * @param old 老连接
     * @param req 新连接
     * @return 当不同时返回 true
     */
    private static boolean diffPort(SocketChannel old, SocketChannel req){
        try {
            return old.getRemoteAddress() != req.getRemoteAddress();
        } catch (IOException e) {
            logger.error("cannot get remote address", e);
            return false;
        }
    }

    private static boolean diffPortNo(SocketChannel old, SocketChannel req){
        try{
            int oldLink = ((InetSocketAddress)old.getRemoteAddress()).getPort();
            int reqLink = ((InetSocketAddress)req.getRemoteAddress()).getPort();
            return oldLink != reqLink;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean diffPortInSameIP(SocketChannel old, SocketChannel req){
        return old.isConnected() && req.isConnected()
                &&!diffAddress(old, req)       /* IP相同 */
                && diffPortNo(old, req);    /* Port数字不同 */
    }

    private static boolean diffAddress(SocketChannel old, SocketChannel req){
        try {
            InetAddress oldLink = ((InetSocketAddress)old.getRemoteAddress()).getAddress();
            InetAddress reqLink = ((InetSocketAddress)req.getRemoteAddress()).getAddress();
            return !oldLink.equals(reqLink);
        } catch (IOException e) {
            logger.error("cannot get remote address",e);
            return false;
        }
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
                logger.info("CLOSED AN INVALID SOCKET @#@ id: " + id/*  + "\n" + DebugUtil.getMapKeys(map)*/);
                return true;
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("FAILED CLOSED AN INVALID SOCKET! @#@ id: " + id);
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
