package team.hnuwt.servicesoftware.compatible.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.compatible.message.TCPMessageHandler;
import team.hnuwt.servicesoftware.compatible.util.ByteBuilder;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

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
            logger.info("READ: " + sk.channel() + " " + result.toString());
            TCPMessageHandler.handler(sc, result);
            if (num == -1)      /* 代理或老系统断开连接 */
            {
                logger.info("CLOSE: " + sk.channel());
//                SocketAddress saddr = ((SocketChannel) sk.channel()).getRemoteAddress();
//                TCPMessageHandler.handleLogout();
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
            logger.info("Close: " + sk.channel());
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
