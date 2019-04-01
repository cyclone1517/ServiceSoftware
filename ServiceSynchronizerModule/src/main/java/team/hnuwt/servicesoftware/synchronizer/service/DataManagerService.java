package team.hnuwt.servicesoftware.synchronizer.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import team.hnuwt.servicesoftware.synchronizer.model.Data;
import team.hnuwt.servicesoftware.synchronizer.util.DataProcessThreadUtil;
import team.hnuwt.servicesoftware.synchronizer.util.RedisUtil;

@Deprecated
public class DataManagerService implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(DataManagerService.class);

    private final static String APPLICATION_FILE = "application.properties";
    private static Properties props;

    private static int batchNum;

    private final static String DATA = "Data";

    /**
     * 从Redis数据库中获取数据
     */
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
        while (true)
        {
            List<Data> list = new ArrayList<>();
            for (int i = 0; i < batchNum; i++)      /* 连续取batchNum条 */
            {
                String s = RedisUtil.getData(DATA);
                if (s != null)
                {
                    List<Data> data = JSON.parseArray(s, Data.class);
                    list.addAll(data);
                } else                              /* 数据为空结束 */
                    break;
            }
            if (list.size() > 0)
            {
                DataProcessThreadUtil.getExecutor().execute(new DataService(list));
            }
        }
    }

}
