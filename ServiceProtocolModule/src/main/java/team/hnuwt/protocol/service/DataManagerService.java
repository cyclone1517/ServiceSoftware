package team.hnuwt.protocol.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.shade.com.alibaba.fastjson.JSON;

import team.hnuwt.plugin.util.RedisUtil;
import team.hnuwt.protocol.model.Data;
import team.hnuwt.protocol.util.DataProcessThreadUtil;

public class DataManagerService implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(DataManagerService.class);

    private final static String APPLICATION_FILE = "application.properties";
    private static Properties props;

    private static int batchNum;

    private final static String DATA = "Data";

    @Override
    public void run()
    {
        try {
            props = new Properties();
            props.load(DataManagerService.class.getClassLoader().getResourceAsStream(APPLICATION_FILE));
        } catch (IOException e) {
            logger.error("", e);
        }

        batchNum = Integer.parseInt(props.getProperty("db.batch"));
        DataProcessThreadUtil dptu = new DataProcessThreadUtil();
        while (true)
        {
            List<Data> list = new ArrayList<>();
            for (int i = 0; i < batchNum; i++)
            {
                String s = RedisUtil.getData(DATA);
                if (s != null)
                {
                    List<Data> data = JSON.parseArray(s, Data.class);
                    list.addAll(data);
                } else
                    break;
            }
            if (list.size() > 0)
            {
                dptu.getExecutor().execute(new DataService(list));
            }
        }
    }

}
