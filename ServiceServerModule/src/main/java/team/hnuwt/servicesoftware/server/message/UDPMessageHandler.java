package team.hnuwt.servicesoftware.server.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.server.util.ByteBuilder;
import team.hnuwt.servicesoftware.server.util.DataProcessThreadUtil;

/**
 * UDP消息处理工具类
 */
public class UDPMessageHandler {

    private static Logger logger = LoggerFactory.getLogger(UDPMessageHandler.class);

    public static void handler(ByteBuilder pkg)
    {
        if (pkg.length() <= 0)
        {
            return;
        }
        logger.info("UDP receive : " + pkg.toString());
        DataProcessThreadUtil.getExecutor().execute(new OrderHandler(pkg.toString()));
    }
}
