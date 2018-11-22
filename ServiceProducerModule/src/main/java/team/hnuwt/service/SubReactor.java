package team.hnuwt.service;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import team.hnuwt.message.MessageHandler;

public class SubReactor implements Runnable {
    private Selector selector;
    private int id;
    private boolean restart = false;
    
    private Logger logger = LoggerFactory.getLogger(SubReactor.class);
    
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
                    e.printStackTrace();
                }
                Iterator<SelectionKey>  it = selector.selectedKeys().iterator();
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
        SocketChannel sc = (SocketChannel)sk.channel();
        ByteBuffer buffer = ByteBuffer.allocate(16384);
        StringBuilder result = new StringBuilder();
        try {
            int num = sc.read(buffer);
            while (num > 0)
            {
                buffer.flip();
                byte[] data = buffer.array();
                String temp = new String(data, 0, num);
                result.append(temp);
                num = sc.read(buffer);
            }
            logger.info("Read: " + sk.channel());
            MessageHandler.handler(sc.getRemoteAddress(), result);
            if (num == -1)
            {
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
            } else sk.interestOps(SelectionKey.OP_READ);
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
