package team.hnuwt.netservice;

import java.io.IOException;
import java.nio.channels.SocketChannel;

/**
 *  阻塞监听器Acceptor
 */
public class Acceptor implements Runnable{
    private Reactor reactor;
    public Acceptor(Reactor reactor){
        this.reactor = reactor;
        this.run();
    }

    @Override
    public void run() {
        try{
            SocketChannel socketChannel = reactor.serverSocketChannel.accept();
            if(socketChannel!=null)
                new SocketReadHandler(reactor.selector, socketChannel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
