package team.hnuwt.servicesoftware.synchronizer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.synchronizer.constant.TAG;
import team.hnuwt.servicesoftware.synchronizer.util.RedisUtil;

import java.util.List;

public class RedisLoginService implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(RedisLoginService.class);

    private List<String> list;

    public RedisLoginService(List<String> list)
    {
        this.list = list;
    }

    /**
     * 将数据插入到Mysql数据库中
     */
    @Override
    public void run()
    {
        RedisUtil.putSetBulk(TAG.LOGIN.getStr(), list);
        logger.info("LOGIN " + list.size() + " collectors in Redis Login Set");
    }

}
