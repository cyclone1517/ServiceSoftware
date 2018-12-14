package team.hnuwt.servicesoftware.server.message;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import team.hnuwt.servicesoftware.server.util.ByteBuilder;
import team.hnuwt.servicesoftware.server.util.ConcentratorUtil;

public class SendHandler implements Runnable {
    private String pkg;

    private static Logger logger = LoggerFactory.getLogger(SendHandler.class);

    public SendHandler(String pkg)
    {
        this.pkg = pkg;
    }

    @Override
    public void run()
    {
        ByteBuilder b = new ByteBuilder(pkg);
        long id = b.BINToLong(7, 12);
        SocketChannel sc = ConcentratorUtil.get(id);
        if (sc == null || !sc.isConnected())
        {
            ConcentratorUtil.remove(id);
            return;
        }
        logger.info("Send: " + sc + " " + b);
        try {
            sc.write(ByteBuffer.wrap(b.getBytes()));
        } catch (IOException e) {
            logger.error("", e);
        }
    }

}
