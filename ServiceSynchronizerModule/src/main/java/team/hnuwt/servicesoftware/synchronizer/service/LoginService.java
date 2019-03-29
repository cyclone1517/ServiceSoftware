package team.hnuwt.servicesoftware.synchronizer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.synchronizer.dao.HeartBeatDao;
import team.hnuwt.servicesoftware.synchronizer.dao.LoginDao;
import team.hnuwt.servicesoftware.synchronizer.model.HeartBeat;
import team.hnuwt.servicesoftware.synchronizer.model.Login;

import java.util.List;

public class LoginService implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(LoginService.class);

    private List<Login> addrList;

    public LoginService(List<Login> addrList)
    {
        this.addrList = addrList;
    }

    /**
     * 将数据插入到Mysql数据库中
     */
    @Override
    public void run()
    {
        new LoginDao().insertBatch(addrList);
        logger.info(addrList.size() + " collectors have LOGIN/OFFLINE state updated.");
    }

}
