package team.hnuwt.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import team.hnuwt.bean.ListImformation;
import team.hnuwt.bean.Meter;
import team.hnuwt.bean.Packet;
import team.hnuwt.dao.DataDao;
import team.hnuwt.util.ProtocolUtil;

public class DataCarryWorker implements Runnable {
    private List<String> list;
    
    private Logger logger = LoggerFactory.getLogger(DataCarryWorker.class);
    
    private static final String FIELD_NAME[] = new String[]{"firstStartChar", "firstLength", 
            "secondLength", "secondStartChar","control", 
            "address", "afn", "seq", "dataId", "number", 
            "id", "data", "valveState", "cs", "endChar"};
    private static final int LENGTH[] = new int[]{1, 2, 2, 1, 1, 5, 1, 1, 4, 2, 2, 4, 1, 1, 1};
    private static final ListImformation LIST_IMFORMATION[] = new ListImformation[]{new ListImformation(9, 13, "team.hnuwt.bean.Meter", "meter")};
    
    public DataCarryWorker(List<String> list)
    {
        this.list = list;
    }

    @Override
    public void run() {
        List<Meter> datas = new ArrayList<>();
        for (String s : list)
        {
        	Packet p = new Packet();
        	ProtocolUtil pu = new ProtocolUtil(FIELD_NAME, LENGTH, LIST_IMFORMATION);
        	pu.translate(s, p);
            datas.addAll(p.getMeter());
        }
        new DataDao().insertBatch(datas);
        logger.info("the number of data : " + String.valueOf(datas.size()));
    }

}
