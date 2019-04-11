package team.hnuwt.servicesoftware.server.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.server.constant.down.TAG;
import team.hnuwt.servicesoftware.server.constant.down.TOPIC;
import team.hnuwt.servicesoftware.server.util.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Calendar;

/**
 * 登录包处理类
 */
public class AutoUploadHandler implements Runnable {
    private ByteBuilder message;

    private SocketChannel sc;

    private static Logger logger = LoggerFactory.getLogger(AutoUploadHandler.class);

    public AutoUploadHandler(SocketChannel sc, ByteBuilder message)
    {
        this.message = message;
        this.sc = sc;
    }

    @Override
    public void run()
    {
        InnerProduceUtil.addQueue(TOPIC.PROTOCOL.getStr(), TAG.AUTO_UPLOAD.getStr(), message.toString());
    }

}
