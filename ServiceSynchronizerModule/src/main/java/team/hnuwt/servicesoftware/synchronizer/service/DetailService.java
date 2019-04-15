package team.hnuwt.servicesoftware.synchronizer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.synchronizer.dao.DetailDao;
import team.hnuwt.servicesoftware.synchronizer.dao.LoginDao;
import team.hnuwt.servicesoftware.synchronizer.model.Login;

import java.util.List;

/**
 * 操作登录详情表
 * 有字段（自增id, CollectorId, loginTime, logoutTime）便于分析集中器每天登录时间、次数、每次登录时长
 * @author yuanlong Chen
 */
public class DetailService implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(DetailService.class);

    private List<Login> addrList;
    private boolean login;

    /**
     * 登录服务
     * @param addrList 登录的集中器编号
     */
    public DetailService(List<Login> addrList, boolean login)
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
        // 如果登录，添加新详情
        if (login) {
            new DetailDao().insertBatch(addrList);
        }

        // 如果离线或登出，为该集中器最大id号的数据添加
        else {
            new DetailDao().fillLogout(addrList);
        }
    }

}
