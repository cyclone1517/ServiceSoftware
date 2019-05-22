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
public class AgencyLoginHandler implements Runnable {

    private SocketChannel sc;
    private long id;

    private static Logger logger = LoggerFactory.getLogger(AgencyLoginHandler.class);


    public AgencyLoginHandler(SocketChannel sc, long id){
        this.sc = sc;
        this.id = id;
    }

    @Override
    public void run()
    {
        try {
            byte[] result;
            if (ConcentratorUtil.get(id) != null) {
                result = FieldPacker.hexStringToByte("AA5858FF035B004F4B00");
                sc.write(ByteBuffer.wrap(result));
            }
            else {
                /* 注册失败报文回复 */
            }
        } catch (IOException e) {
            logger.error("REPLY AGENCY ERROR", e);
        }
    }

}
