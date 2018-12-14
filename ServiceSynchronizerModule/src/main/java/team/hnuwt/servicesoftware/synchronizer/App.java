package team.hnuwt.servicesoftware.synchronizer;

import team.hnuwt.servicesoftware.synchronizer.service.DataManagerService;

public class App {

    public static void main(String[] args)
    {
        DataManagerService dms = new DataManagerService();
        dms.run();
    }

}
