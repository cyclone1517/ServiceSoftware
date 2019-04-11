package team.hnuwt.servicesoftware.compatible;

import team.hnuwt.servicesoftware.compatible.server.DataSubscribeService;
import team.hnuwt.servicesoftware.compatible.server.MainReactor;
import team.hnuwt.servicesoftware.compatible.util.CompatibleUtil;
import team.hnuwt.servicesoftware.compatible.util.Subscriber;

import java.io.IOException;

public class App {

    public static void main(String[] args) throws IOException
    {
        MainReactor mainReactor = new MainReactor();
        CompatibleUtil.setMainReactor(mainReactor);

        // 启动集中器连接和数据读取侦听
        new Thread(mainReactor).start();

        // 启动 redis 数据订阅服务
        Subscriber subscriber = new Subscriber();
        String myChanel = "agency";
        DataSubscribeService dss = new DataSubscribeService(subscriber, myChanel);
        dss.run();
    }

}
