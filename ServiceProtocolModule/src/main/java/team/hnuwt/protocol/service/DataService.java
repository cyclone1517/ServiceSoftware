package team.hnuwt.protocol.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import team.hnuwt.protocol.dao.DataDao;
import team.hnuwt.protocol.model.Data;

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