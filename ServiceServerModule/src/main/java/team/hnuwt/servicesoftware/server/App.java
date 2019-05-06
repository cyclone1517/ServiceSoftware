package team.hnuwt.servicesoftware.server;

import java.io.IOException;

import team.hnuwt.servicesoftware.server.message.TCPMessageHandler;
import team.hnuwt.servicesoftware.server.service.MainReactor;
import team.hnuwt.servicesoftware.server.service.UDPReactor;
import team.hnuwt.servicesoftware.server.util.CompatibleUtil;
import team.hnuwt.servicesoftware.server.util.ConsumerUtil;

public class App {

    public static void main(String[] args) throws IOException
    {
        MainReactor mainReactor = new MainReactor();    /* 启动主反应器：管理连接 */
        CompatibleUtil.setMainReactor(mainReactor);     /* 注册与兼容模块的通信连接 */
        TCPMessageHandler.openTCPCompatible();          /* 打开本地兼容模式开关 */

        // 启动集中器连接和数据读取侦听
        new Thread(mainReactor).start();                /* 启动主反应器 */
//        new Thread(new UDPReactor()).start();

        // 启动三方和内部消息命令侦听
        ConsumerUtil consumerUtil = new ConsumerUtil(); /* 消息队列：负责中间服务命令接受 */
        consumerUtil.run();
    }

}
