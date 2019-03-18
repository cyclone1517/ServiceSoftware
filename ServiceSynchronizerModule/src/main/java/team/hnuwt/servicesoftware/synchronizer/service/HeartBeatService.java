package team.hnuwt.servicesoftware.synchronizer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.synchronizer.dao.HeartBeatDao;
import team.hnuwt.servicesoftware.synchronizer.model.HeartBeat;

import java.util.List;

public class HeartBeatService implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(HeartBeatService.class);

    private List<HeartBeat> addrList;

    public HeartBeatService(List<HeartBeat> addrList)
    {
        this.addrList = addrList;
    }

    /**
     * 将数据插入到Mysql数据库中
     */
    @Override
    public void run()
    {
        new HeartBeatDao().insertBatch(addrList);
        logger.info(addrList.size() + " HEARTBEAT PUT INTO MYSQL");
    }

}
