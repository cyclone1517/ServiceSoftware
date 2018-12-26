package team.hnuwt.servicesoftware.server.message;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import team.hnuwt.servicesoftware.server.util.ByteBuilder;
import team.hnuwt.servicesoftware.server.util.ProduceUtil;

/**
 * 心跳包处理类
 */
public class HeartBeatHandler implements Runnable {
    private ByteBuilder heartBeat;

    private SocketChannel sc;

    private static Logger logger = LoggerFactory.getLogger(HeartBeatHandler.class);

    public HeartBeatHandler(SocketChannel sc, ByteBuilder heartBeat)
    {
        this.heartBeat = heartBeat;
        this.sc = sc;
    }

    @Override
    public void run()
    {
        ProduceUtil.addQueue(heartBeat.toString());

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
            sc.write(ByteBuffer.wrap(b));
        } catch (IOException e) {
            logger.error("", e);
        }
    }
}
