package team.hnuwt.servicesoftware.distribute;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.ditribute.util.ConsumerUtil;

import java.io.IOException;
import java.util.Properties;

/**
 * 功能：下行数据分发模块
 * 从消息队列接收下行报文，将其发送到集中器
 */
public class App {

    private final static String APPLICATION_FILE = "application.properties";
    private static Properties props;

    private static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args)
    {
        try {
            props = new Properties();
            props.load(App.class.getClassLoader().getResourceAsStream(APPLICATION_FILE));
        } catch (IOException e) {
            logger.error("", e);
        }

        ConsumerUtil consumerUtil = new ConsumerUtil();
        consumerUtil.run();

//        FileUtil fu = new FileUtil(props.getProperty("plugin.xmlPath"));
//        fu.setDelay(Integer.parseInt(props.getProperty("plugin.updateTime")));
//        fu.start();
    }

}
