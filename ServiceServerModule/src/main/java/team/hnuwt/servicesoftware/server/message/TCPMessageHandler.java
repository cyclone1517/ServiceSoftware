package team.hnuwt.servicesoftware.server.message;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import com.sun.corba.se.impl.ior.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import team.hnuwt.servicesoftware.server.compatible.FortendAgency;
import team.hnuwt.servicesoftware.server.model.Logout;
import team.hnuwt.servicesoftware.server.constant.up.FUNID;
import team.hnuwt.servicesoftware.server.service.MainReactor;
import team.hnuwt.servicesoftware.server.util.ByteBuilder;
import team.hnuwt.servicesoftware.server.util.CompatibleUtil;
import team.hnuwt.servicesoftware.server.util.ConcentratorUtil;
import team.hnuwt.servicesoftware.server.util.DataProcessThreadUtil;

/**
 * TCP消息处理工具类
 */
public class TCPMessageHandler {

    private static Logger logger = LoggerFactory.getLogger(TCPMessageHandler.class);

    private static Map<SocketAddress, Remainder> map = new ConcurrentHashMap<>();

    private static boolean compatible = false;

    public static void openTCPCompatible(){
        compatible = true;
    }

//    public static void closeTCPCompatible(){
//        compatible = false;
//    }

//    private static void registAgency(){
//        for (FortendAgency agency: agencyList){
//            mRec.registerRead(agency.getSocketChannel());
//        }
//    }

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
    public static void handleLogout(List<Logout> logoutList){
        DataProcessThreadUtil.getExecutor().execute(new LogoutHandler(logoutList));
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
                    if (compatible){
                        if (CompatibleUtil.isUpstream(result.getByte(6))){  // 如果上行，往老系统发
                            DataProcessThreadUtil.getExecutor().execute(new SendHandler(result.toString(), true));
                        } else {    // 如果下行，往集中器发
                            DataProcessThreadUtil.getExecutor().execute(new SendHandler(result.toString(), false));
                        }
                    }

                    /* 心跳：走协议栈单线 */
                    if (result.getByte(12) == (byte) 0x02 && result.BINToLong(14, 18) == FUNID.HEARTBEAR)   /* 获取功能码和数字单元标识 */
                    {
                        logger.info("HEARTBEAT: " + result.toString());
                        DataProcessThreadUtil.getExecutor().execute(new HeartBeatHandler(sc, result));
                    }

                    /* 登录：走协议栈单线 */
                    else if (result.getByte(12) == (byte) 0x02 && result.BINToLong(14, 18) == FUNID.LOGIN)
                    {
                        logger.info("LOGIN: " + result.toString());
                        DataProcessThreadUtil.getExecutor().execute(new LoginHandler(sc, result));
                    }

                    /* 抄表：中间服务单线 */
                    else if (result.getByte(12) == (byte) 0x8C && result.BINToLong(14, 18) == FUNID.READ_METER){
                        logger.info("READ_METER: " + result.toString());
                        DataProcessThreadUtil.getExecutor().execute(new ReadMeterReHandler(sc, result));
                    }

                    /* 自动上传：走协议栈单线 */
                    else if (result.getByte(12) == (byte) 0x8C && result.BINToLong(14, 18) == FUNID.AUTOUPLOAD){
                        logger.info("AUTO_UPLOAD: " + result.toString());
                        DataProcessThreadUtil.getExecutor().execute(new AutoUploadHandler(sc, result));
                    }

                    /* 操作确认：走中间服务单线 */
                    else if (result.getByte(12) == (byte) 0x85 && result.BINToLong(14, 18) == FUNID.SUCCESS){
                        logger.info("DEVICE ON/OFF SUCCESS: " + result.toString());
                        DataProcessThreadUtil.getExecutor().execute(new StateReHandler(sc, result, true));
                    }

                    /* 操作失败：走中间服务单线 */
                    else if (result.getByte(12) == (byte) 0x85 && result.BINToLong(14, 18) == FUNID.FAIL){
                        logger.info("DEVICE ON/OFF FAIL: " + result.toString());
                        DataProcessThreadUtil.getExecutor().execute(new StateReHandler(sc, result, false));
                    }

                    /* 其余数据包：设置自动上报回复0x84还没处理 */
                    else {
                        logger.info("OTHERS: " + result.toString());
//                        DataProcessThreadUtil.getExecutor().execute(new OrderHandler(result.toString()));
                    }

                    /*
                     * 登出没有报文，见上文 handleLogout 方法
                     */
                }
                result = new ByteBuilder();
                state = 0;
            }
        }

        return new Remainder(result, state);
    }

}
