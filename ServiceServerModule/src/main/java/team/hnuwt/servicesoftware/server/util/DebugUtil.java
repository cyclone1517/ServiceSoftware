package team.hnuwt.servicesoftware.server.util;

import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Set;

/**
 * 服务器不支持远程调试，只能用打印信息调试了
 */
public class DebugUtil {

    public static String getMapKeys(Map<Long, SocketChannel> map){
        StringBuilder sb = new StringBuilder();
        Set<Long> keys = map.keySet();
        for (Long i: keys){
            sb.append("{" + i + ", realId: " + i +"}");
        }
        return sb.toString();
    }
}
