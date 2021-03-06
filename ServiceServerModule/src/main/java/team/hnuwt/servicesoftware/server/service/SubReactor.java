package team.hnuwt.servicesoftware.server.service;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import team.hnuwt.servicesoftware.server.message.TCPMessageHandler;
import team.hnuwt.servicesoftware.server.util.ByteBuilder;
import team.hnuwt.servicesoftware.server.util.ConcentratorUtil;

public class SubReactor implements Runnable {
    private Selector selector;
    private int id;
    private boolean restart = false;

    private static Logger logger = LoggerFactory.getLogger(SubReactor.class);

    public SubReactor(Selector selector, int id)
    {
        this.selector = selector;
        this.id = id;
    }

    public void setRestart(boolean restart)
    {
        this.restart = restart;
    }

    @Override
    public void run()
    {
        logger.info("SubReactor-" + id + " has been set up");
        while (!Thread.interrupted())
        {
            while (!Thread.interrupted() && !restart)
            {
                try {
                    if (selector.select() == 0)
                    {
                        continue;
                    }
                } catch (IOException e) {
                    logger.error("", e);
                }
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while (it.hasNext())
                {
                    read(it.next());
                    it.remove();
                }
            }
        }
    }

    private void read(SelectionKey sk)
    {
        SocketChannel sc = (SocketChannel) sk.channel();
        ByteBuffer buffer = ByteBuffer.allocate(16384);
        ByteBuilder result = new ByteBuilder();
        try {
            int num = sc.read(buffer);
            while (num > 0)
            {
                buffer.flip();
                for (int i = 0; i < num; i++)
                    result.append(buffer.get());
                num = sc.read(buffer);
            }
            logger.info("read: " + sc.toString().substring(31) + result.toString());
            TCPMessageHandler.handler(sc, result);
            if (num == -1)
            {
                logger.info("CLOSE: " + sk.channel());
                SocketAddress saddr = ((SocketChannel) sk.channel()).getRemoteAddress();
                TCPMessageHandler.handleLogout(ConcentratorUtil.findLogout(saddr)); /* 正常关闭(远程通讯端申请关闭连接) */
                sk.cancel();
                if (sk.channel() != null)
                {
                    try {
                        sk.channel().close();
                    } catch (IOException e1) {
                        logger.error("", e1);
                    }
                }
            } else
                sk.interestOps(SelectionKey.OP_READ);
        } catch (IOException e) {
            logger.error("", e);
            logger.info("CLOSE: " + sk.channel());

            try {   // 离线通知数据库
                SocketAddress saddr = null;
                saddr = ((SocketChannel) sk.channel()).getRemoteAddress();
                TCPMessageHandler.handleLogout(ConcentratorUtil.findLogout(saddr)); /* 强迫关闭(直接断开连接) */
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            sk.cancel();
            if (sk.channel() != null)
            {
                try {
                    sk.channel().close();
                } catch (IOException e1) {
                    logger.error("", e1);
                }
            }
        }
    }

}
