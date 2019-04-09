package team.hnuwt.servicesoftware.server.message;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import team.hnuwt.servicesoftware.server.constant.down.TAG;
import team.hnuwt.servicesoftware.server.constant.down.TOPIC;
import team.hnuwt.servicesoftware.server.util.*;

/**
 * 登录包处理类
 */
public class LoginHandler implements Runnable {
    private ByteBuilder message;

    private SocketChannel sc;

    private static Logger logger = LoggerFactory.getLogger(LoginHandler.class);

    public LoginHandler(SocketChannel sc, ByteBuilder message)
    {
        this.message = message;
        this.sc = sc;
    }

    @Override
    public void run()
    {
        long id = message.BINToLong(7, 12);

//        // 重复集中器ID判断相关，暂不启用
//        if (ConcentratorUtil.containsDuplicate(id, sc)){
//            try {
//                logger.info("REFUSE CONN for duplicate link from @#@ id:" + id + " at " + sc);
//                sc.close();
//            } catch (IOException e) {
//                logger.error("Failed to close duplicate link from different collectors" ,e);
//            }
//            return;
//        }
        ConcentratorUtil.add(id, sc);
        InnerProduceUtil.addQueue(TOPIC.PROTOCOL.getStr(), TAG.LOGIN.getStr(), message.toString());

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
            b[cnt++] = message.getByte(i);
        b[cnt++] = (byte) 0x00;     /* 这里文档是02，应该是00 */
        b[cnt++] = (byte) 0x60;
        b[cnt++] = (byte) 0x00;
        b[cnt++] = (byte) 0x00;
        b[cnt++] = (byte) 0x04;
        b[cnt++] = (byte) 0x00;
        b[cnt++] = message.getByte(12);
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
            logger.info("reply:" + PkgPackUtil.bytes2hex(b));
        } catch (IOException e) {
            logger.error("", e);
        }
    }

}
