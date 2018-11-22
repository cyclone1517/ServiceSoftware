package team.hnuwt.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import team.hnuwt.util.DataProcessThreadUtil;
import team.hnuwt.util.RedisUtil;

public class DataCarryManager implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(DataCarryManager.class);

    private final static String APPLICATION_FILE = "application.properties";
    private static Properties props;
    
    private static int batchNum;
    
    @Override
    public void run() {
        try {
            props = new Properties();
            props.load(DataCarryManager.class.getClassLoader().getResourceAsStream(APPLICATION_FILE));
        } catch (IOException e) {
            logger.error("", e);
        }
        batchNum = Integer.parseInt(props.getProperty("db.batch"));
        
        DataProcessThreadUtil dptu = new DataProcessThreadUtil();
        
        while (true)
        {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < batchNum; i++)
            {
                List<String> datas = RedisUtil.getData();
                if (datas.size() > 0)
                {
                    for (int j = 0, len = datas.size(); j < len; j += 2)
                    {
                        list.add(datas.get(j + 1));
                    }
                } else break;
            }
            if (list.size() > 0)
            {
                dptu.getExecutor().execute(new DataCarryWorker(list));
            }
        }
    }

}
