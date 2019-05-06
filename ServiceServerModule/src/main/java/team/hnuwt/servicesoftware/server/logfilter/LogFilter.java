package team.hnuwt.servicesoftware.server.logfilter;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * 日志过滤器，可以屏蔽不想要的日志类型
 */
public class LogFilter extends Filter<ILoggingEvent> {

    @Override
    public FilterReply decide(ILoggingEvent event) {
        String formattedMessage = event.getFormattedMessage();
        if (formattedMessage.contains("PROTOCOL")               /* 忽略协议栈日志 */
            ||formattedMessage.contains("supplementary")) {     /* 忽略补偿心跳日志 */
            return FilterReply.DENY;
        }
        else {
            return FilterReply.NEUTRAL;
        }
    }
}
