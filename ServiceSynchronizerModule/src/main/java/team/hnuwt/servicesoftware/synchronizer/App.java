package team.hnuwt.servicesoftware.synchronizer;

import team.hnuwt.servicesoftware.synchronizer.service.DataSubscribeService;
import team.hnuwt.servicesoftware.synchronizer.util.Subscriber;

public class App {

    public static void main(String[] args)
    {
//        DataManagerService dms = new DataManagerService();
//        dms.run();
        Subscriber subscriber = new Subscriber();
        String myChanel = "notifier";
        DataSubscribeService dss = new DataSubscribeService(subscriber, myChanel);
        dss.run();
    }

}
