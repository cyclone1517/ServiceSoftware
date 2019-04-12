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

        /*
         *  登录的同时更新心跳时间，以免在登陆后第一次心跳上传之前的心跳空窗期被检查程序剔除
         */
        new HeartBeatDao().update2LoginTime(addrList);
        logger.info(addrList.size() + " collectors have " + ((login)?"LOGIN":"OFFLINE") + " state updated.");
    }

}
