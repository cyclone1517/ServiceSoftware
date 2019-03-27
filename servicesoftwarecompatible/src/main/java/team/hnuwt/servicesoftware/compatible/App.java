package team.hnuwt.servicesoftware.compatible;

import team.hnuwt.servicesoftware.compatible.server.MainReactor;
import team.hnuwt.servicesoftware.compatible.util.CompatibleUtil;

import java.io.IOException;

public class App {

    public static void main(String[] args) throws IOException
    {
        MainReactor mainReactor = new MainReactor();
        CompatibleUtil.setMainReactor(mainReactor);

        // 启动集中器连接和数据读取侦听
        new Thread(mainReactor).start();
    }

}
