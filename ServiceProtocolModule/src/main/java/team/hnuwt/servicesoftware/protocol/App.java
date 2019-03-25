package team.hnuwt.servicesoftware.protocol;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import team.hnuwt.servicesoftware.plugin.service.PluginService;
import team.hnuwt.servicesoftware.plugin.util.ByteBuilder;
import team.hnuwt.servicesoftware.protocol.util.ConsumerUtil;
import team.hnuwt.servicesoftware.protocol.util.FileUtil;
import team.hnuwt.servicesoftware.protocol.util.PluginUtil;

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

        try {
            PluginService ps = PluginUtil.getInstance(Long.valueOf("30081548684"));
//            ps.translate(new ByteBuilder("683500350068C9000000000002701000040012BC16"));
            Thread.sleep(500);  /* 获得类实例，提前做类静态化 */
        } catch (Exception e){
            logger.info("Initiating");
        }

        ConsumerUtil consumerUtil = new ConsumerUtil();
        consumerUtil.run();

        FileUtil fu = new FileUtil(props.getProperty("plugin.xmlPath"));
        fu.setDelay(Integer.parseInt(props.getProperty("plugin.updateTime")));
        fu.start();
    }

}
