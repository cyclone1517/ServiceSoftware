package team.hnuwt.servicesoftware.synchronizer.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池管理类
 */
public class DataProcessThreadUtil {
    private static volatile ThreadPoolExecutor executor;

    private final static String APPLICATION_FILE = "application.properties";

    private static Properties props;

    private static Logger logger = LoggerFactory.getLogger(DataProcessThreadUtil.class);

//    public DataProcessThreadUtil()
//    {
//        try {
//            props = new Properties();
//            InputStream is = DataProcessThreadUtil.class.getClassLoader().getResourceAsStream(APPLICATION_FILE);
//            props.load(is);
//        } catch (IOException e) {
//            logger.error("", e);
//        }
//        executor = new ThreadPoolExecutor(Integer.parseInt(props.getProperty("thread.corePoolSize")),
//                Integer.parseInt(props.getProperty("thread.maximumPoolSize")),
//                Integer.parseInt(props.getProperty("thread.keepAliveTime")), TimeUnit.MICROSECONDS,
//                new ArrayBlockingQueue<>(10000));
//    }
//
//    public ThreadPoolExecutor getExecutor()
//    {
//        return executor;
//    }

    static {
        try {
            props = new Properties();
            props.load(DataProcessThreadUtil.class.getClassLoader().getResourceAsStream(APPLICATION_FILE));
        } catch (IOException e) {
            logger.error("", e);
        }
        executor = new ThreadPoolExecutor(Integer.parseInt(props.getProperty("thread.corePoolSize")),
                Integer.parseInt(props.getProperty("thread.maximumPoolSize")),
                Integer.parseInt(props.getProperty("thread.keepAliveTime")), TimeUnit.MICROSECONDS,
                new ArrayBlockingQueue<>(10000));
    }

    public static ThreadPoolExecutor getExecutor()
    {
        return executor;
    }

}
