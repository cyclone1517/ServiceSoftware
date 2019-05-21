package team.hnuwt.servicesoftware.synchronizer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.synchronizer.constant.TAG;
import team.hnuwt.servicesoftware.synchronizer.dao.DataDao;
import team.hnuwt.servicesoftware.synchronizer.model.Data;
import team.hnuwt.servicesoftware.synchronizer.util.RedisUtil;

import java.util.List;

public class RedisLogoutService implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(RedisLogoutService.class);

    private List<String> list;

    public RedisLogoutService(List<String> list)
    {
        this.list = list;
    }

    /**
     * 将数据插入到Mysql数据库中
     */
    @Override
    public void run()
    {
        RedisUtil.removeSetBulk(TAG.LOGIN.getStr(), list);
        logger.info("Removed " + list.size() + " LOGOUT collectors in Redis Login Set");
    }

}
