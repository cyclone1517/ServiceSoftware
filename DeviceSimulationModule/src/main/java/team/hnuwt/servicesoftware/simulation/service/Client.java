package team.hnuwt.servicesoftware.simulation.service;

import java.io.IOException;
import java.net.InetSocketAddress;
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
                    Integer.parseInt(props.getProperty("socket.tcp.port")));
            sc = SocketChannel.open(isa);
            sc.configureBlocking(false);
            sc.register(selector, SelectionKey.OP_READ);
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
