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
    private boolean login;

    /**
     * 登录服务
     * @param addrList 登录的集中器编号
     * @param login 因为登录登出用同一个服务，为方便打印日志，true时为登录，false时为登出
     */
    public LoginService(List<Login> addrList, boolean login)
    {
        this.addrList = addrList;
        this.login = login;
    }

    /**
     * 将数据插入到Mysql数据库中
     */
    @Override
    public void run()
    {
        new LoginDao().insertBatch(addrList);
        logger.info(addrList.size() + " collectors have " + ((login)?"LOGIN":"OFFLINE") + " state updated.");
    }

}
