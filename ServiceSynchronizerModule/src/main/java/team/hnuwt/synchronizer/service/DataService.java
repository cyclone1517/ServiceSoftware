package team.hnuwt.synchronizer.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import team.hnuwt.synchronizer.dao.DataDao;
import team.hnuwt.synchronizer.model.Data;

public class DataService implements Runnable {
    
    private static Logger logger = LoggerFactory.getLogger(DataService.class);
    
    private List<Data> list;
    
    public DataService(List<Data> list)
    {
        this.list = list;
    }

    @Override
    public void run()
    {
        new DataDao().insertBatch(list);
        logger.info("the number of data : " + list.size());
    }

}
