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
        if (event.getFormattedMessage().contains("PROTOCOL")) {
            return FilterReply.DENY;
        }
        else {
            return FilterReply.NEUTRAL;
        }
    }
}
