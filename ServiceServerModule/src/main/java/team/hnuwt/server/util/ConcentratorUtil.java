package team.hnuwt.server.util;

import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

/**
 * �洢������id��SocketChannel�Ķ�Ӧ��ϵ
 */
public class ConcentratorUtil {
    private static Map<Long, SocketChannel> map = new HashMap<>();

    public static void add(Long id, SocketChannel sc)
    {
        map.put(id, sc);
    }

    public static SocketChannel get(Long id)
    {
        return map.get(id);
    }

    public static void remove(Long id)
    {
        map.remove(id);
    }
}
