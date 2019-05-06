package team.hnuwt.servicesoftware.synchronizer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.synchronizer.dao.DuplicateDao;
import team.hnuwt.servicesoftware.synchronizer.dao.HeartBeatDao;
import team.hnuwt.servicesoftware.synchronizer.dao.LoginDao;
import team.hnuwt.servicesoftware.synchronizer.model.Duplicate;
import team.hnuwt.servicesoftware.synchronizer.model.HeartBeat;

import java.util.List;

public class DuplicateService implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(DuplicateService.class);

    private List<Duplicate> dupList;

    public DuplicateService(List<Duplicate> dupList)
    {
        this.dupList = dupList;
    }

    /**
     * 将数据插入到Mysql数据库中
     */
    @Override
    public void run()
    {
        new DuplicateDao().insertBatch(dupList);
        logger.info(dupList.size() + " DUPLICATE PUT INTO MYSQL");
    }

}
