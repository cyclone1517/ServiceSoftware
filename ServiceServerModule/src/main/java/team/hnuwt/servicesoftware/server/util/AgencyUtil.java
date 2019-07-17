package team.hnuwt.servicesoftware.server.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.server.model.Logout;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 存储集中器id与SocketChannel的对应关系
 */
public class AgencyUtil {
    private static ConcurrentHashMap<Long, Set<SocketChannel>> map = new ConcurrentHashMap<>();
    private static Logger logger = LoggerFactory.getLogger(AgencyUtil.class);

    /**
     * 判断是否为兼容请求（12表号代理软件）
     * @param request
     * @return
     */
    public static boolean isAgencyRequest(ByteBuilder request){
        return request.toString().startsWith("AA585808005A00") && FieldPacker.isCorrectAgencyCs(request);
    }

    /**
     * 判断是否为兼容请求 （14代理工具）
     * @param request
     * @return
     */
    public static boolean isAgencyRequest2(ByteBuilder request){


        return request.toString().startsWith("AA585808005A00")  && request.toString().endsWith("000000FF");
    }


    public static long getId(ByteBuilder request){
        return request.BINToLong(7, 9);
    }

    /**
     * 添加代理集合
     */
    public static void add(Long id, SocketChannel sc)
    {
        // 建立新连接前删去旧连接引用
        if (map.get(id) == null){
            Set<SocketChannel> scSet = new HashSet<>();
            scSet.add(sc);
            map.put(id, scSet);
        } else {
            map.get(id).add(sc);
        }

    }

    public static Set<SocketChannel> getAgency(Long id)
    {
        return map.get(id);
    }

}
