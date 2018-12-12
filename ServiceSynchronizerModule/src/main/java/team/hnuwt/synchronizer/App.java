package team.hnuwt.synchronizer;

import team.hnuwt.synchronizer.service.DataManagerService;

public class App {

    public static void main(String[] args)
    {
        DataManagerService dms = new DataManagerService();
        dms.run();
    }

}
