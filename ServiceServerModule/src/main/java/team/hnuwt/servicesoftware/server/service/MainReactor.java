package team.hnuwt.servicesoftware.server.service;

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

    private static Logger logger = LoggerFactory.getLogger(MainReactor.class);

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
       //监听读事件  Reactor 数量
        NUM = Integer.parseInt(props.getProperty("subReactorThread"));
        subReactors = new SubReactor[NUM];
        selectors = new Selector[NUM];
        //选择器打开
        selector = Selector.open();
        //非阻塞ServerSocket 打开
        server = ServerSocketChannel.open();
        server.socket().bind(new InetSocketAddress(Integer.parseInt(props.getProperty("socket.tcp.port"))));
        //注册Channel为非阻塞
        server.configureBlocking(false);
        server.register(selector, SelectionKey.OP_ACCEPT);
        logger.info("MainReactor has been set up");

        for (int i = 0; i < NUM; i++)
        {
            selectors[i] = Selector.open();
            subReactors[i] = new SubReactor(selectors[i], i);   //SubReactor管理selectors，selectors管理读事件
            new Thread(subReactors[i]).start();
        }
    }

    @Override
    public void run()
    {
        try {
            while (selector.select() > 0)   /* 查找连接请求 */
            {
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while (it.hasNext())    /* 遍历准备好的连接事件 */
                {
                    SelectionKey sk = it.next();
                    connect(sk);    /* 将连接事件信息传给connect() */
                    it.remove();
                }
            }
        } catch (IOException e) {
            logger.error("MainReactor Runtime Exception", e);
        }
    }

    /**
     * 连接操作处理，分配给SubReactor进行读操作
     * @param sk
     */
    private void connect(SelectionKey sk)
    {
        try {
            ServerSocketChannel ssc = (ServerSocketChannel) sk.channel();
            SocketChannel sc = ssc.accept();
            sc.configureBlocking(false);

            subReactors[selIndex].setRestart(true);
            selectors[selIndex].wakeup();
            sc.register(selectors[selIndex], SelectionKey.OP_READ); /* 把套接字读事件注册到SubReactor */
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
