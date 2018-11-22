package team.hnuwt.netservice;

import com.alibaba.rocketmq.client.exception.MQClientException;
import team.hnuwt.data.heartBeat.RedisHelper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 *  反应器模式
 *  用于解决多用户访问并发问题
 *
 *  传统做法：一个客人，一个服务线程
 *  反应器做法：当客户需要的时候，再去处理客户消息
 */
public class Reactor implements Runnable{
    public final Selector selector;
    public final ServerSocketChannel serverSocketChannel;
    public final DatagramChannel datagramChannel;
    private final RedisHelper heatBeatRedisHelper;

    public Reactor(int tcpPort, int udpPort) throws IOException{
        //init heatBeatRedisHelper with singleton
        heatBeatRedisHelper = RedisHelper.getInstance();
        //init serverSocketChannel
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(tcpPort));
        serverSocketChannel.configureBlocking(false);
        //init DatagramChannel
        datagramChannel = DatagramChannel.open();
        datagramChannel.socket().bind(new InetSocketAddress(udpPort));
        datagramChannel.configureBlocking(false);

        //register this channel into selector
        selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        datagramChannel.register(selector,SelectionKey.OP_READ);

        //这里没有使用Acceptor
        //利用selectionKey的attach功能绑定Acceptor,如果有事情，触发Acceptor
        //selectionKey.attach(new Acceptor(this));
    }

    @Override
    public void run() {
        System.out.println("nio server has been set up!");
        try {
            while(!Thread.interrupted()){
                int readyChannels = selector.select();
                if(readyChannels == 0) continue;
                Iterator<SelectionKey> it= selector.selectedKeys().iterator();
                //Selector如果发现channel有OP_ACCEPT或READ事件发生，下列遍历就会进行。
                while(it.hasNext()){
                    //来一个事件 第一次触发一个accepter线程
                    //以后触发SocketReadHandler
                    SelectionKey selectionKey=it.next();
                    dispatch(selectionKey);
                    it.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dispatch(SelectionKey key) throws IOException {
        if (key.isAcceptable()) {
            handlerAccept(key);

        //区别可读数据的来源是TCP还是UDP
        } else if (key.isReadable()) {
            if(key.channel() instanceof SocketChannel) handlerTCPReader(key);
            if(key.channel() instanceof DatagramChannel) handlerUDPReader(key);
        }
}

    public void handlerAccept(SelectionKey key) throws IOException {
        ServerSocketChannel sever = (ServerSocketChannel) key.channel();
        SocketChannel channel = sever.accept();
        channel.configureBlocking(false);
        System.out.println("有客服端连接来了" + channel.toString());
        channel.register(this.selector, SelectionKey.OP_READ);
        //没有使用SocketReadHandler类
    }

    public void handlerTCPReader(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //不会阻塞
        int n = socketChannel.read(buffer);

        if (n > 20) {   //命令数据长度>20

            byte[] data = buffer.array();
            String pkgCode = new String(data, 0, n);
            try {
                ProduceTool producerTool = new ProduceTool("msgAcceptGroup",
                        "115.157.192.49:9876", "producer1");
                producerTool.addQueueMsg(pkgCode);
            } catch (MQClientException | InterruptedException e) {
                e.printStackTrace();
            }
            buffer.flip();
            socketChannel.write(buffer);

        } else if (n>0){    //心跳数据长度[0,20]

            byte[] data = buffer.array();
            String pkgCode = new String(data, 0, n);
            //System.out.println("服务端收到TCP消息:" + pkgCode);
            heatBeatRedisHelper.updateHeatBeat(pkgCode);
            buffer.flip();
            socketChannel.write(buffer);

        } else {

            System.out.println("clinet is close");
            key.cancel();

        }
    }

    public void handlerUDPReader(SelectionKey key) throws IOException {
        DatagramChannel datagramChannel = (DatagramChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(48);
        buffer.clear();
        datagramChannel.receive(buffer);
        byte[] data = buffer.array();
        System.out.println("服务端收到UDP数据报" + new String(data, 0, data.length));
    }
}