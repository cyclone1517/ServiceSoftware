package team.hnuwt.servicesoftware.synchronizer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.synchronizer.dao.CheckDao;
import team.hnuwt.servicesoftware.synchronizer.dao.LoginDao;
import team.hnuwt.servicesoftware.synchronizer.model.Login;

import java.util.List;

public class OfflineReService implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(OfflineReService.class);

    private List<String> offlineOKList;

    public OfflineReService(List<String> offlineOKList)
    {
        this.offlineOKList = offlineOKList;
    }

    /**
     * 将数据插入到Mysql数据库中
     */
    @Override
    public void run()
    {
        new CheckDao().resetOffline(offlineOKList);
        logger.info(offlineOKList.size() + " collectors have LOGOUT STATE CHANGED!");
    }

}
