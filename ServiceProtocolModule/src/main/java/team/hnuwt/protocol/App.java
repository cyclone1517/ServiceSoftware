package team.hnuwt.protocol;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import team.hnuwt.protocol.service.DataManagerService;
import team.hnuwt.protocol.util.ConsumerUtil;
import team.hnuwt.protocol.util.FileUtil;

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

        FileUtil fu = new FileUtil(props.getProperty("plugin.xmlPath"));
        fu.setDelay(Integer.parseInt(props.getProperty("plugin.updateTime")));
        fu.start();
        
        DataManagerService dmc = new DataManagerService();
        dmc.run();
    }

}
