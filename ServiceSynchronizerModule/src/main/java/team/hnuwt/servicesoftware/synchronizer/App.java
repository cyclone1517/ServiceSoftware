package team.hnuwt.servicesoftware.synchronizer;

import team.hnuwt.servicesoftware.synchronizer.offlinecheck.CheckTask;
import team.hnuwt.servicesoftware.synchronizer.service.DataSubscribeService;
import team.hnuwt.servicesoftware.synchronizer.util.Subscriber;

import java.util.Timer;

public class App {

    public static void main(String[] args)
    {
        // 启动 redis 数据订阅服务
        Subscriber subscriber = new Subscriber();
        String myChanel = "notifier";
        DataSubscribeService dss = new DataSubscribeService(subscriber, myChanel);
        dss.run();

        // 启动 Timer 定时检测任务
        Timer timer = new Timer();
        int checkSecond = 200;   /* 过期时间200分 */
        int period = 5000;       /* 扫描时间5秒 */
        timer.schedule(new CheckTask(checkSecond), 0,period);
    }

}
