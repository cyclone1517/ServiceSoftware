package team.hnuwt.servicesoftware.server;

import java.io.IOException;

import team.hnuwt.servicesoftware.server.message.TCPMessageHandler;
import team.hnuwt.servicesoftware.server.service.MainReactor;
import team.hnuwt.servicesoftware.server.service.UDPReactor;
import team.hnuwt.servicesoftware.server.util.ConsumerUtil;

public class App {

    public static void main(String[] args) throws IOException
    {
        // 开启前置机兼容模式
        TCPMessageHandler.openTCPCompatible();

        // 启动集中器连接和数据读取侦听
        new Thread(new MainReactor()).start();
        new Thread(new UDPReactor()).start();

        // 启动三方和内部消息命令侦听
        ConsumerUtil consumerUtil = new ConsumerUtil();
        consumerUtil.run();
    }

}
