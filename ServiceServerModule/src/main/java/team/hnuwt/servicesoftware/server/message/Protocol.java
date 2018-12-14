package team.hnuwt.servicesoftware.server.message;

import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import team.hnuwt.servicesoftware.server.util.ByteBuilder;
import team.hnuwt.servicesoftware.server.util.DataProcessThreadUtil;

public class Protocol {
    private static Logger logger = LoggerFactory.getLogger(Protocol.class);

    public static Remainder normalProtocol(ByteBuilder pkg, int state, ByteBuilder result, SocketChannel sc)
    {
        int len = pkg.length();
        for (int i = 0; i < len; i++)
        {
            byte c = pkg.getByte(i);
            if (state == 0)
            {
                if (c == 0x68)
                {
                    state = 1;
                    result.append(c);
                }
            } else if (state >= 1 && state <= 5)
            {
                state++;
                result.append(c);
            } else if (state < 0)
            {
                result.append(c);
                state++;
            }
            if (state == 6)
            {
                int firstLength = ((result.getInt(2)) << 6) | (result.getInt(1) >> 2);
                int secondLength = ((result.getInt(4)) << 6) | (result.getInt(3) >> 2);
                if (firstLength == secondLength)
                {
                    if (c == 0x68)
                    {
                        state = -(firstLength + 2) - 1;
                    } else
                    {
                        state = 0;
                        result = new ByteBuilder();
                    }
                } else
                {
                    if (c == 0x68)
                    {
                        state = 2;
                        result = new ByteBuilder((char) 0x68);
                    } else
                    {
                        state = 0;
                        result = new ByteBuilder();
                    }
                }
            }
            if (state == -1)
            {
                if (c == 0x16)
                {
                    logger.info(result.toString());
                    if (result.getByte(12) == (byte) 0x02 && result.BINToLong(14, 18) == 262160)
                    {
                        DataProcessThreadUtil.getExecutor().execute(new HeartBeatHandler(sc, result));
                    } else if (result.getByte(12) == (byte) 0x02 && result.BINToLong(14, 18) == 65552)
                    {
                        DataProcessThreadUtil.getExecutor().execute(new LoginHandler(sc, result));
                    } else
                    {
                        DataProcessThreadUtil.getExecutor().execute(new OrderHandler(result.toString()));
                    }
                }
                result = new ByteBuilder();
                state = 0;
            }
        }

        return new Remainder(result, state);
    }
}
