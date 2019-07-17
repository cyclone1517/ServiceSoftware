package team.hnuwt.servicesoftware.server.message;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import team.hnuwt.servicesoftware.server.compatible.FortendAgency;
import team.hnuwt.servicesoftware.server.constant.down.TAG;
import team.hnuwt.servicesoftware.server.model.Logout;
import team.hnuwt.servicesoftware.server.constant.up.FUNID;
import team.hnuwt.servicesoftware.server.util.*;

/**
 * TCP消息处理工具类
 */
public class TCPMessageHandler {

    private static Logger logger = LoggerFactory.getLogger(TCPMessageHandler.class);

    private static Map<SocketAddress, Remainder> map = new ConcurrentHashMap<>();

    private static boolean compatible = false;
    private static boolean agency = true;

    private static FortendAgency fortendAgency;

    public static void openTCPCompatible(){
        compatible = true;
        fortendAgency = new FortendAgency();
    }

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
        /*
         * 通知数据库和中间服务
         */
        DataProcessThreadUtil.getExecutor().execute(new LogoutHandler(logoutList));

        /*
         * 通知兼容模块
         */
        RedisUtil.publishData("agency", geneLogoutStr(logoutList));
    }

    private static String geneLogoutStr(List<Logout> logoutList){
        StringBuilder sb = new StringBuilder();

        int size = logoutList.size();
        if (size > 0) {
            sb.append(logoutList.get(0).getId());
        }
        if (size > 1){
            for (int i=1; i<size; i++){
                sb.append("||").append(logoutList.get(i).getId());
            }
        }
        return sb.toString();
    }

    /**
     * 将多条命令拆分，解决粘包问题，同时分离命令和心跳包
     */
    public static Remainder translate(ByteBuilder pkg, int state, ByteBuilder result, SocketChannel sc)
    {
        int len = pkg.length();
        for (int i = 0; i < len; i++)
        {
            /*
             * 添加代理连接
             * 后加的代理判断逻辑没法加半包处理，否则会导致逻辑混乱，所以一次处理15个字节，但是要做粘包
             */
            if (AgencyUtil.isAgencyRequest(pkg) ){

                long id = AgencyUtil.getId(pkg);

                logger.info("Agency: " + sc + result.toString() + " @#@id: " + id);
                AgencyUtil.add(id, sc);
                //TODO
               //如果集中在线才能注册成功
              // if  (ConcentratorUtil.get(id)!=null && ConcentratorUtil.get(id).isConnected() )
                   /* 回复报文 */
                   DataProcessThreadUtil.getExecutor().execute(new AgencyLoginHandler(sc, id));


                /* 解析完初始化参数 */
                i += 15;
                result = new ByteBuilder();
                state = 0;
                continue;
            }
            if ( AgencyUtil.isAgencyRequest2(pkg)) {

            long id = AgencyUtil.getId(pkg);

            logger.info("Agency2: " + sc + result.toString() + " @#@id: " + id);
            AgencyUtil.add(id, sc);
            //TODO
            //如果集中在线才能注册成功
            // if  (ConcentratorUtil.get(id)!=null && ConcentratorUtil.get(id).isConnected() )
            /* 回复报文 */
            DataProcessThreadUtil.getExecutor().execute(new AgencyLoginHandler(sc, id));


            /* 解析完初始化参数 */
            i += 13;
            result = new ByteBuilder();
            state = 0;
            continue;
        }

            /*
             * 原先的通讯协议报文处理
             */
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
            if (state == -1)        /* 这里逻辑复杂，详细说明参考 resource/processLogic.png */
            {
                if (c == 0x16)
                {
                    /* 初始化伪登录标志和集中器id */
                    boolean fake = false;
                    long id = FieldPacker.getId(result);

                    /* 初始化集中器冲突信息 */
                    //SocketChannel oldSc = ConcentratorUtil.getOriginDuplicateSocket(id, sc);    /* 获取和重复ID相同且正在登录的集中器，只有在IP不同时才返回 */

                     //if (CompatibleUtil.isUpstream(result.getByte(6)) && oldSc != null){     /* 如果报文上行且重复 */
                    //    DataProcessThreadUtil.getExecutor().execute(new DuplicateHandler(id, oldSc, sc));   /* 通知数据库，有重复ID的集中器 */
                    //    fake = true;   /* 本地需要做心跳和登录处理，忽略其他业务，并且不做透明转发 */
                    // }


                    /*
                     * 兼容老系统
                     */
                    if (compatible){

                        // 如果上行，往兼容模块发
                        if (CompatibleUtil.isUpstream(result.getByte(6))) {

                            // 如果集中器ID未冲突，往上转发
                            if (!fake) {
                                if (fortendAgency.getAlivedCompatibleLink() != null) {
                                    DataProcessThreadUtil.getExecutor().execute(new SendHandler(result.toString(), true, fortendAgency.getAlivedCompatibleLink()));
                                } else {
                                    logger.warn("AGENCY WARN: No available agency link!!");
                                }
                            }
                        }

                        // 如果下行，往集中器发
                        else {
                            DataProcessThreadUtil.getExecutor().execute(new SendHandler(result.toString(), false));

                            result = new ByteBuilder();
                            state = 0;
                            continue;       /* 本地不必解析下行报文，但是可能粘包，则继续处理 */
                        }
                    }

                    /*
                     * 兼容代理
                     *
                     * 20190612  by lizhimin  屏蔽心跳报文
                     */
                    if (agency){
                        // 如果上行，往代理发
                        if (CompatibleUtil.isUpstream(result.getByte(6))) {
                            Set<SocketChannel> agencies = AgencyUtil.getAgency(id);
                            //过滤心跳包
                              if ( (agencies != null)  &&  (result.getByte(12) != (byte) 0x02 && result.BINToLong(14, 18) != FUNID.HEARTBEAR ) ) {

                                for (SocketChannel agency: agencies){
                                    DataProcessThreadUtil.getExecutor().execute(new SendHandler(result.toString(), true, agency));
                                }
                            }

                        }

                        // 如果下行，往集中器发
                        else {
                            DataProcessThreadUtil.getExecutor().execute(new SendHandler(result.toString(), false));
                            result = new ByteBuilder();
                            state = 0;
                            continue;       /* 本地不必解析下行报文，但是可能粘包，则继续处理 */
                        }
                    }

                    /* 心跳：走协议栈单线 */
                    if (result.getByte(12) == (byte) 0x02 && result.BINToLong(14, 18) == FUNID.HEARTBEAR )   /* 获取功能码和数字单元标识 */
                    {
                        logger.info(((fake)?"FAKE ":"") + "HEARTBEAT: " + result.toString() + " @#@id: " + id);
                        DataProcessThreadUtil.getExecutor().execute(new HeartBeatHandler(sc, result));
                    }

                    /* 登录：走协议栈单线 */
                    else if (result.getByte(12) == (byte) 0x02 && result.BINToLong(14, 18) == FUNID.LOGIN  )
                    {
                        if (fake){
                            logger.info("FAKE LOGIN: " + sc + result.toString() + " @#@id: " + id);
                            DataProcessThreadUtil.getExecutor().execute(new LoginHandler(sc, result, fake));    /* 冗余ID集中器伪登录 */
                        } else {
                            logger.info("LOGIN: " + sc + result.toString());
                            DataProcessThreadUtil.getExecutor().execute(new LoginHandler(sc, result));  /* 正常登录 */
                        }
                    }

                    /*
                     * 伪登录只有登录和心跳权限，continue 忽略所有其他业务
                     * 注意不能将 else if 改为 if, 会让上下两段变为并行逻辑
                     */
                    else if (fake) continue;

                    else {

                        /*
                         * 但凡有数据响应，就通知数据库更新响应时间，由于没有真实心跳报文，直接通过Redis通知数据库即可
                         */
                        DataProcessThreadUtil.getExecutor().execute(new SubHeartBeatHandler(id));

                        /* 抄表：中间服务单线 */
                        if (result.getByte(12) == (byte) 0x8C && result.BINToLong(14, 18) == FUNID.READ_METER
                                && CompatibleUtil.isUpstream(result.getByte(6))) {       /* 下行的AFN和数据单元标识一样造成冲突 */
                            logger.info("READ_METER: " + result.toString());
                            DataProcessThreadUtil.getExecutor().execute(new ReadMeterReHandler(sc, result));
                        }

                        /* 自动上传：走协议栈 + 中间服务双线 */
                        else if (result.getByte(12) == (byte) 0x8C && result.BINToLong(14, 18) == FUNID.AUTOUPLOAD) {
                            logger.info("AUTO_UPLOAD: " + result.toString());
                            DataProcessThreadUtil.getExecutor().execute(new AutoUploadHandler(sc, result));
                        }

                        /* 开关阀操作反馈：走中间服务单线 */
                        else if (result.getByte(12) == (byte) 0x85) {
                            if (result.BINToLong(14, 18) == FUNID.GATE_SUCCESS) {
                                logger.info("DEVICE_ON/OFF SUCCESS: " + result.toString());
                                DataProcessThreadUtil.getExecutor().execute(new StateReHandler(result, TAG.CTRL_ONOFF, true));
                            }
                            if (result.BINToLong(14, 18) == FUNID.GATE_FAIL) {
                                logger.info("DEVICE_ON/OFF FAIL: " + result.toString());
                                DataProcessThreadUtil.getExecutor().execute(new StateReHandler(result, TAG.CTRL_ONOFF, false));
                            }
                        }

                        /* 下载档案操作反馈：走中间服务单线 */
                        else if (result.getByte(12) == (byte) 0x00 || result.getByte(12) == (byte) 0x84) {   /* 未按功能码回复 */
                            if (result.BINToLong(14, 18) == FUNID.ARCHIVE_DOWNLOAD_SUCCESS) {
                                logger.info("ARCHIVE_DOWNLOAD SUCCESS: " + result.toString());
                                DataProcessThreadUtil.getExecutor().execute(new StateReHandler(result, TAG.ARCHIVE_DOWNLOAD, true));
                            }
                            if (result.BINToLong(14, 18) == FUNID.ARCHIVE_DOWNLOAD_FAIL) {
                                logger.info("ARCHIVE_DOWNLOAD FAIL: " + result.toString());
                                DataProcessThreadUtil.getExecutor().execute(new StateReHandler(result, TAG.ARCHIVE_DOWNLOAD, false));
                            }
                        }

                        /* 其余数据包：设置自动上报回复0x84还没处理 */
                        else {
                            /* 转发的心跳回复不打印 */
                            if (result.getByte(12) == (byte) 0x00 && result.BINToLong(14, 18) == FUNID.HEART_LOGIN_RE) {
                                // do nothing
                            } else {
                                logger.info("OTHERS: " + result.toString());
                            }
                        }

                        /*
                         * 登出没有报文，见上文 handleLogout 方法
                         */
                    }
                }
                result = new ByteBuilder();
                state = 0;
            }
        }

        return new Remainder(result, state);
    }
}
