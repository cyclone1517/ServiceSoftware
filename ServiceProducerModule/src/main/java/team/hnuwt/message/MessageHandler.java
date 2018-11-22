package team.hnuwt.message;

import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageHandler {
    
    private static Logger logger = LoggerFactory.getLogger(MessageHandler.class);
    
    private static Map<SocketAddress, Remainder> map = new ConcurrentHashMap<>();
    
    /**
     * �����������֣����ճ�����⣬ͬʱ���������������
     * @param packageCode
     */
    public static void handler(SocketAddress sa,StringBuilder pkg)
    {
        int state = 0;
        StringBuilder result = new StringBuilder();
        
        Remainder remainder = map.get(sa);
        if (remainder != null) 
        {
            state = remainder.getState();
            result = remainder.getResult();
            map.remove(sa);
        }
        
        remainder = Protocol.normalProtocol(pkg, state, result);
        
        if (!"".equals(remainder.getResult().toString())) 
        {
            map.put(sa, remainder);
        }
    }

}
