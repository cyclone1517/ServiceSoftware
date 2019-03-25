package team.hnuwt.servicesoftware.server.message;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import team.hnuwt.servicesoftware.server.util.ByteBuilder;
import team.hnuwt.servicesoftware.server.util.CompatibleUtil;
import team.hnuwt.servicesoftware.server.util.ConcentratorUtil;

/**
 * 消息发送类
 */
public class SendHandler implements Runnable {
    private String pkg;
    private boolean upstream;

    private static Logger logger = LoggerFactory.getLogger(SendHandler.class);

    public SendHandler(String pkg){
        this(pkg, false);
    }

    public SendHandler(String pkg, boolean upstream){
        this.pkg = pkg;
        this.upstream = upstream;
    }

    @Override
    public void run()
    {
        ByteBuilder b = new ByteBuilder(pkg);
        long id = b.BINToLong(7, 12);
        SocketChannel sc = null;
        if (upstream){
            sc = CompatibleUtil.get(id);
            if (sc == null || !sc.isConnected()){
                CompatibleUtil.addConn(id);      // 上行创建兼容链接
                sc = CompatibleUtil.get(id);
                CompatibleUtil.registSocketRead(sc);
            }
        } else {
            sc = ConcentratorUtil.get(id);       // 下行获取集中器链接
            if (sc == null || !sc.isConnected())
            {
                ConcentratorUtil.remove(id);
                return;
            }
        }

        logger.info("Send: " + sc + " " + b);
        try {
            sc.write(ByteBuffer.wrap(b.getBytes()));
        } catch (IOException e) {
            logger.error("", e);
        }
    }

}
