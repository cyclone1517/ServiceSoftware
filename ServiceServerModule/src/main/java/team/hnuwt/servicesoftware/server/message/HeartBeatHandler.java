package team.hnuwt.servicesoftware.server.message;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Calendar;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import team.hnuwt.servicesoftware.server.constant.down.TAG;
import team.hnuwt.servicesoftware.server.constant.down.TOPIC;
import team.hnuwt.servicesoftware.server.util.ByteBuilder;
import team.hnuwt.servicesoftware.server.util.ConcentratorUtil;
import team.hnuwt.servicesoftware.server.util.FieldPacker;
import team.hnuwt.servicesoftware.server.util.InnerProduceUtil;

/**
 * 心跳包处理类
 */
public class HeartBeatHandler implements Runnable {
    private ByteBuilder heartBeat;

    private SocketChannel sc;

    private static Logger logger = LoggerFactory.getLogger(HeartBeatHandler.class);

    private static boolean reply;

    private static final String APPLICATION_FILE = "application.properties";
    private static Properties prop;

    /* 加载类变量的静态代码块 */
    static {
        prop = new Properties();
        try {
            prop.load(HeartBeatHandler.class.getClassLoader().getResourceAsStream(APPLICATION_FILE));
            reply = Boolean.parseBoolean(prop.getProperty("heartbeat.reply"));
            if (!reply){
                logger.info("YOU CLOSED HEARTBEAT REPLY IN NEW SYSTEM");
            }
        } catch (IOException e) {
            e.printStackTrace();
            reply = false;
        }
    }

    public HeartBeatHandler(SocketChannel sc, ByteBuilder heartBeat)
    {
        this.heartBeat = heartBeat;
        this.sc = sc;
    }

    @Override
    public void run() {
        long id = FieldPacker.getId(heartBeat);
        SocketChannel currSc = ConcentratorUtil.get(id);
        if (currSc == null) {
            return;       /* 若没有登录，不予理会 */
        } else if (ConcentratorUtil.diffPortInSameIP(currSc, sc)) {
            ConcentratorUtil.add(id, sc);     /* 若来自不同的端口（包括同IP不同端口和不同IP同端口），应更换 */
        }
        InnerProduceUtil.addQueue(TOPIC.PROTOCOL.getStr(), TAG.HEARTBEAT.getStr(), heartBeat.toString());

        // 收到报文，在配置回复心跳时发确认帧到集中器
        if (reply) {
            Calendar cas = Calendar.getInstance();
            int year = cas.get(Calendar.YEAR);
            int month = cas.get(Calendar.MONTH) + 1;
            int day = cas.get(Calendar.DAY_OF_MONTH);
            int hour = cas.get(Calendar.HOUR_OF_DAY);
            int minute = cas.get(Calendar.MINUTE);
            int second = cas.get(Calendar.SECOND);

            byte b[] = new byte[33];
            int cnt = 0;
            b[cnt++] = (byte) 0x68;
            b[cnt++] = (byte) 0x65;
            b[cnt++] = (byte) 0x00;
            b[cnt++] = (byte) 0x65;
            b[cnt++] = (byte) 0x00;
            b[cnt++] = (byte) 0x68;
            b[cnt++] = (byte) 0x10;
            for (int i = 7; i < 12; i++)
                b[cnt++] = heartBeat.getByte(i);
            b[cnt++] = (byte) 0x00;
            b[cnt++] = (byte) 0x60;
            b[cnt++] = (byte) 0x00;
            b[cnt++] = (byte) 0x00;
            b[cnt++] = (byte) 0x04;
            b[cnt++] = (byte) 0x00;
            b[cnt++] = heartBeat.getByte(12);
            b[cnt++] = (byte) 0x10;
            b[cnt++] = (byte) 0x00;
            b[cnt++] = (byte) 0x01;
            b[cnt++] = (byte) 0x00;
            b[cnt++] = (byte) 0x00;
            b[cnt++] = (byte) (year / 10 % 10 * 16 + year % 10);
            b[cnt++] = (byte) (year / 1000 % 10 * 16 + year / 100 % 10);
            b[cnt++] = (byte) (month / 10 % 10 * 16 + month % 10);
            b[cnt++] = (byte) (day / 10 % 10 * 16 + day % 10);
            b[cnt++] = (byte) (hour / 10 % 10 * 16 + hour % 10);
            b[cnt++] = (byte) (minute / 10 % 10 * 16 + minute % 10);
            b[cnt++] = (byte) (second / 10 % 10 * 16 + second % 10);
            b[cnt++] = 0;
            for (int i = 6; i < cnt - 1; i++)
                b[cnt - 1] += b[i];
            b[cnt++] = (byte) 0x16;
            try {
                ByteBuffer bb = ByteBuffer.wrap(b);
                while (bb.hasRemaining()) {
                    sc.write(bb);
                }
            } catch (IOException e) {
                logger.error("", e);
            }
        }
    }
}
