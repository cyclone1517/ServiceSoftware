package team.hnuwt.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import team.hnuwt.util.DataProcessThreadUtil;
import team.hnuwt.util.StringUtil;

public class Protocol {
    private static Logger logger = LoggerFactory.getLogger(Protocol.class);
    
    public static Remainder normalProtocol(StringBuilder pkg, int state, StringBuilder result) 
    {
        int len = pkg.length();
        for (int i = 0; i < len; i++)
        {
            char c = pkg.charAt(i);
            if (state == 0)
            {
                if (c == '6') 
                {
                    state = 1;
                }
            } else if (state == 1)
            {
                if (c == '8')
                {
                    state = 2;
                    result.append("68");
                }
            } else if (state >= 2 && state <= 11)
            {
                state++;
                result.append(c);
            } else if (state < 0)
            {
                result.append(c);
                state++;
            }
            if (state == 12)
            {
                int firstLength = StringUtil.toInt(result.substring(2, 6));
                int secondLength = StringUtil.toInt(result.substring(6, 10));
                if (firstLength == secondLength)
                {
                    if (result.toString().endsWith("68"))
                    {
                        state = -(firstLength + 2) * 2 - 1;
                    } else 
                    {
                        state = 0;
                        result = new StringBuilder();
                    }
                } else 
                {
                    if (result.toString().endsWith("68"))
                    {
                        state = 2;
                        result = new StringBuilder("68");
                    } else 
                    {
                        state = 0;
                        result = new StringBuilder();
                    }
                }
            }
            if (state == -1)
            {
                if (result.toString().endsWith("16"))
                {
                    //System.out.println(result);
                    //logger.info(String.valueOf(result.length()));
                    DataProcessThreadUtil.getExecutor().execute(new OrderHandler(result.toString()));
                }
                result = new StringBuilder();
                state = 0;
            }
        }
        
        return new Remainder(result, state);
    }
}
