package team.hnuwt.netservice;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class SocketReadHandler implements Runnable {
    private SocketChannel socketChannel;
    public SocketReadHandler(Selector selector, SocketChannel socketChannel) throws IOException {
        this.socketChannel = socketChannel;
        socketChannel.configureBlocking(false);

        SelectionKey selectionKey = socketChannel.register(selector, 0);

        //将SelectionKey绑定为本Handler，下一步有事件触发时，调用本类的run方法
        //参考dispatch(SelectionKey key)
        selectionKey.attach(this);

        //同时将SelectionKey标记为刻度，以便读取
        selectionKey.interestOps(SelectionKey.OP_READ);
        selector.wakeup();

        System.out.println("调用了SocketReadHandler的构造函数");
    }

    /**
     *  处理读取数据
     */
    @Override
    public void run() {
        System.out.println("调用SocketReadHandler Run 方法");
        ByteBuffer inputBuffer = ByteBuffer.allocate(1024);
        inputBuffer.clear();
        try {
            int n = socketChannel.read(inputBuffer);
            if (n > 0) {
                byte[] data = inputBuffer.array();
                System.out.println("服务端收到信息:" + new String(data, 0, n));
                inputBuffer.flip();
                socketChannel.write(inputBuffer);
            } else {
                System.out.println("clinet is close");
                //key.cancel();
            }
            //激活线程池，处理这些request
            //requestHandle(new Request(socket, btt))
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
