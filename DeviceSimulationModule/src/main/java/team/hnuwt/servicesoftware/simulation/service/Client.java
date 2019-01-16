package team.hnuwt.servicesoftware.simulation.service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Properties;

import team.hnuwt.servicesoftware.simulation.App;

public class Client {
    private final static String APPLICATION_FILE = "application.properties";

    private static Properties props;

    public SocketChannel sc = null;

    private long id;

    public Client(long id, Selector selector)
    {
        this.id = id;
        this.init(selector);
    }

    public void init(Selector selector)
    {
        try {
            props = new Properties();
            props.load(App.class.getClassLoader().getResourceAsStream(APPLICATION_FILE));

            InetSocketAddress isa = new InetSocketAddress(props.getProperty("socket.ip"),
                    Integer.parseInt(props.getProperty("socket.port")));
            sc = SocketChannel.open(isa);
            sc.configureBlocking(false);
            sc.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendHeartBeat()
    {
        byte b[] = new byte[]{0x68, 0x35, 0x00, 0x35, 0x00, 0x68, (byte) 0xc9, (byte) (id & 0xff),
                (byte) ((id >> 8) & 0xff), (byte) ((id >> 16) & 0xff), (byte) ((id >> 24) & 0xff),
                (byte) ((id >> 32) & 0xff), 0x02, 0x70, 0x10, 0x00, 0x04, 0x00, 0x22, 0x00, 0x16};
        try {
            sc.write(ByteBuffer.wrap(b));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close()
    {
        try {
            sc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
