package team.hnuwt.service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainReactor implements Runnable {
    private final static String APPLICATION_FILE = "application.properties";
    
    private Logger logger = LoggerFactory.getLogger(MainReactor.class);
    
    private final int NUM;
    private Selector selector = null;
    private ServerSocketChannel server = null;
    private SubReactor[] subReactors = null;
    private Selector[] selectors = null;
    private int selIndex = 0;
    
    public MainReactor() throws IOException
    {
        Properties props = new Properties();
        props.load(MainReactor.class.getClassLoader().getResourceAsStream(APPLICATION_FILE));
        
        NUM = Integer.parseInt(props.getProperty("subReactorThread"));
        subReactors = new SubReactor[NUM];
        selectors = new Selector[NUM];
        
        selector = Selector.open();
        server = ServerSocketChannel.open();
        server.socket().bind(new InetSocketAddress(Integer.parseInt(props.getProperty("socket.port"))));
        server.configureBlocking(false);
        server.register(selector, SelectionKey.OP_ACCEPT);
        logger.info("MainReactor has been set up");
        
        for (int i = 0; i < NUM; i++)
        {
            selectors[i] = Selector.open();
            subReactors[i] = new SubReactor(selectors[i], i);
            new Thread(subReactors[i]).start();
        }
    }
    
    @Override
    public void run() {
        try {
            while (selector.select() > 0)
            {
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while (it.hasNext())
                {
                    SelectionKey sk = it.next();
                    connect(sk);
                    it.remove();
                }
            }
        } catch (IOException e) {
            logger.error("", e);
        }
    }
    
    private void connect(SelectionKey sk)
    {
        try {
            ServerSocketChannel ssc = (ServerSocketChannel) sk.channel();
            SocketChannel sc = ssc.accept();
            sc.configureBlocking(false);
            
            subReactors[selIndex].setRestart(true);
            selectors[selIndex].wakeup();
            sc.register(selectors[selIndex], SelectionKey.OP_READ);
            selectors[selIndex].wakeup();
            subReactors[selIndex].setRestart(false);
            
            logger.info("Connect: " + sc);
            if (++selIndex == NUM)
            {
                selIndex = 0;
            }
        } catch (IOException e) {
            logger.error("", e);
        }
    }
}
