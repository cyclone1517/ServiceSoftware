package team.hnuwt.servicesoftware.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.server.message.UDPMessageHandler;
import team.hnuwt.servicesoftware.server.util.ByteBuilder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Properties;

public class UDPReactor implements Runnable {
    private final static String APPLICATION_FILE = "application.properties";

    private static Logger logger = LoggerFactory.getLogger(UDPReactor.class);

    private Selector selector = null;
    private DatagramChannel server = null;

    public UDPReactor() throws IOException
    {
        Properties props = new Properties();
        props.load(UDPReactor.class.getClassLoader().getResourceAsStream(APPLICATION_FILE));

        selector = Selector.open();
        server = DatagramChannel.open();
        server.socket().bind(new InetSocketAddress(Integer.parseInt(props.getProperty("socket.udp.port"))));
        server.configureBlocking(false);
        server.register(selector, SelectionKey.OP_READ);
        logger.info("UDPReactor has been set up");
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
                    if (sk.isReadable())
                    {
                        read(sk);
                    }
                    it.remove();
                }
            }
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    /**
     * 读操作
     * @param sk
     */
    private void read(SelectionKey sk) {
        DatagramChannel dc = (DatagramChannel) sk.channel();
        ByteBuffer buffer = ByteBuffer.allocate(16384);
        ByteBuilder result = new ByteBuilder();
        try {
            SocketAddress sa = dc.receive(buffer);
            buffer.flip();
            while (buffer.hasRemaining())
            {
                int num = buffer.remaining();
                for (int i = 0; i < num; i++)
                    result.append(buffer.get());
            }
            logger.info("Read: " + sa);
            UDPMessageHandler.handler(result);
        } catch (IOException e) {
            logger.error("", e);
            sk.cancel();
            try {
                if (sk.channel() != null)
                    sk.channel().close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
