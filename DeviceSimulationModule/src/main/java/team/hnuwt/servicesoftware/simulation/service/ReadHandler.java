package team.hnuwt.servicesoftware.simulation.service;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import team.hnuwt.servicesoftware.simulation.util.ByteBuilder;

public class ReadHandler implements Runnable {
    private Selector selector;

    public ReadHandler(Selector selector)
    {
        this.selector = selector;
    }

    @Override
    public void run()
    {
        try {
            while (selector.select() > 0)
            {
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while (it.hasNext())
                {
                    SelectionKey sk = it.next();
                    it.remove();
                    if (sk.isReadable())
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
                            System.out.println("Read: " + sk.channel());
                            System.out.println(result);
                            if (num == -1)
                            {
                                System.out.println("Close: " + sk.channel());
                                sk.cancel();
                                if (sk.channel() != null)
                                {
                                    try {
                                        sk.channel().close();
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            } else
                                sk.interestOps(SelectionKey.OP_READ);
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println("Close: " + sk.channel());
                            sk.cancel();
                            if (sk.channel() != null)
                            {
                                try {
                                    sk.channel().close();
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
