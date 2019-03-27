package team.hnuwt.servicesoftware.compatible.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.compatible.util.AgencyAcptUtil;
import team.hnuwt.servicesoftware.compatible.util.ByteBuilder;
import team.hnuwt.servicesoftware.compatible.util.CompatibleUtil;
import team.hnuwt.servicesoftware.compatible.util.DataProcessThreadUtil;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
     * 将主动登出的集中器汇报给数据库和中间服务
     * @param logoutList
     */
//    @Deprecated
//    public static void handleLogout(List<Logout> logoutList){
//        DataProcessThreadUtil.getExecutor().execute(new LogoutHandler(logoutList));
//    }

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
                    boolean upstream = CompatibleUtil.isUpstream(result.getByte(6));    /* 获取上下行 */
                    long id = result.BINToLong(7,12);

                    /*
                     * 注册代理
                     */
                    if (upstream){
                        AgencyAcptUtil.add(id, sc);
                    }

                    /*
                     * 透明转发
                     */
                    DataProcessThreadUtil.getExecutor().execute(new SendHandler(result.toString(), upstream));
                }
                result = new ByteBuilder();
                state = 0;
            }
        }

        return new Remainder(result, state);
    }

}
