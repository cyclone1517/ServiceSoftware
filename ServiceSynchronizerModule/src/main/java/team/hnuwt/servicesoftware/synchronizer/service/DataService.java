package team.hnuwt.servicesoftware.synchronizer.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import team.hnuwt.servicesoftware.synchronizer.dao.DataDao;
import team.hnuwt.servicesoftware.synchronizer.model.Data;

public class DataService implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(DataService.class);

    private List<Data> list;

    public DataService(List<Data> list)
    {
        this.list = list;
    }

    /**
     * 将数据插入到Mysql数据库中
     */
    @Override
    public void run()
    {
        new DataDao().insertBatch(list);
        logger.info("UPLOADED " + list.size() + " data");
    }

}
