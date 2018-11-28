package team.hnuwt.message;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import team.hnuwt.util.ByteBuilder;

public class MessageHandler {

    private static Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    private static Map<SocketAddress, Remainder> map = new ConcurrentHashMap<>();

    /**
     * 将多条命令拆分，解决粘包问题，同时分离命令和心跳包
     * 
     * @param packageCode
     */
    public static void handler(SocketChannel sc, ByteBuilder pkg)
    {
        SocketAddress sa = null;
        try {
            sa = sc.getRemoteAddress();
        } catch (IOException e) {
            logger.error("", e);
        }

        int state = 0;
        ByteBuilder result = new ByteBuilder();

        Remainder remainder = map.get(sa);
        if (remainder != null)
        {
            state = remainder.getState();
            result = remainder.getResult();
            map.remove(sa);
        }

        remainder = Protocol.normalProtocol(pkg, state, result, sc);

        if (!"".equals(remainder.getResult().toString()))
        {
            map.put(sa, remainder);
        }
    }

}
