package team.hnuwt.netservice;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
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

    public Reactor(int port) throws IOException{
        //init serverSocketChannel
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);

        //register this channel into selector
        selector = Selector.open();
        SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //利用selectionKey的attach功能绑定Acceptor,如果有事情，触发Acceptor
        selectionKey.attach(new Acceptor(this));
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

    /**
     *  运行Acceptor或SocketReadHandler
     */
//    public void dispatch(SelectionKey key) throws IOException {
//        Runnable r = (Runnable)(key.attachment());  //执行attach过的线程
//        if (r != null){
//            r.run();
//        }
//    }

    public void dispatch(SelectionKey key) throws IOException {
        if (key.isAcceptable()) {
            handlerAccept(key);
        } else if (key.isReadable()) {
            handlerReader(key);
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

    public void handlerReader(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //不会阻塞
        int n = socketChannel.read(buffer);
        System.out.println(n);
        if (n > 0) {
            byte[] data = buffer.array();
            System.out.println("服务端收到信息:" + new String(data, 0, n));
            buffer.flip();
            socketChannel.write(buffer);
        } else {
            System.out.println("clinet is close");
            key.cancel();
        }
    }
}