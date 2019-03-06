package team.hnuwt.servicesoftware.server.message;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import team.hnuwt.servicesoftware.server.constant.FUN;
import team.hnuwt.servicesoftware.server.util.ByteBuilder;
import team.hnuwt.servicesoftware.server.util.DataProcessThreadUtil;

/**
 * TCP消息处理工具类
 */
public class TCPMessageHandler {

    private static Logger logger = LoggerFactory.getLogger(TCPMessageHandler.class);

    private static Map<SocketAddress, Remainder> map = new ConcurrentHashMap<>();

    /**
     * 读消息处理
     * @param sc
     * @param pkg
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

        remainder = translate(pkg, state, result, sc);

        if (!"".equals(remainder.getResult().toString()))
        {
            map.put(sa, remainder);
        }
    }

    /**
     * 将多条命令拆分，解决粘包问题，同时分离命令和心跳包
     * @param pkg
     * @param state
     * @param result
     * @param sc
     * @return
     */
    public static Remainder translate(ByteBuilder pkg, int state, ByteBuilder result, SocketChannel sc)
    {
        int len = pkg.length();
        for (int i = 0; i < len; i++)
        {
            byte c = pkg.getByte(i);
            if (state == 0)
            {
                if (c == 0x68)
                {
                    state = 1;
                    result.append(c);
                }
            } else if (state >= 1 && state <= 5)
            {
                state++;
                result.append(c);
            } else if (state < 0)
            {
                result.append(c);
                state++;
            }
            if (state == 6)
            {
                int firstLength = ((result.getInt(2)) << 6) | (result.getInt(1) >> 2);
                int secondLength = ((result.getInt(4)) << 6) | (result.getInt(3) >> 2);
                if (firstLength == secondLength)
                {
                    if (c == 0x68)
                    {
                        state = -(firstLength + 2) - 1;
                    } else
                    {
                        state = 0;
                        result = new ByteBuilder();
                    }
                } else
                {
                    if (c == 0x68)
                    {
                        state = 2;
                        result = new ByteBuilder((char) 0x68);
                    } else
                    {
                        state = 0;
                        result = new ByteBuilder();
                    }
                }
            }
            if (state == -1)
            {
                if (c == 0x16)
                {
                    logger.info("TCP receive : " + result.toString());
                    /* 获取功能码和数字单元标识 */
                    if (result.getByte(12) == (byte) 0x02 && result.BINToLong(14, 18) == FUN.HEARTBEAR)    /* 心跳 */
                    {
                        DataProcessThreadUtil.getExecutor().execute(new HeartBeatHandler(sc, result));
                    } else if (result.getByte(12) == (byte) 0x02 && result.BINToLong(14, 18) == FUN.LOGIN)      /* 登录 */
                    {
                        DataProcessThreadUtil.getExecutor().execute(new LoginHandler(sc, result));
                    } else      /* 其余数据包 */
                    {
                        DataProcessThreadUtil.getExecutor().execute(new OrderHandler(result.toString()));
                    }
                }
                result = new ByteBuilder();
                state = 0;
            }
        }

        return new Remainder(result, state);
    }

}
