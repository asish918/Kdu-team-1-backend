package com.kdu.ibebackend.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.LayoutBase;

/**
 * Layout file that has been customized and is being used
 * to present logs to logs/app.log.
 * I have implemented filters for the logging to file and
 * console along-with custom colors using the Jansi Library.
 * Do checkout the logback.xml in resources folder
 */
public class LoggerLayout extends LayoutBase<ILoggingEvent> {

    public String doLayout(ILoggingEvent event) {
        StringBuilder sbuf = new StringBuilder(128);
        sbuf.append(event.getTimeStamp());
        sbuf.append(" ");
        sbuf.append(event.getLevel());
        sbuf.append(" [");
        sbuf.append(event.getThreadName());
        sbuf.append("] ");
        sbuf.append(event.getLoggerName());
        sbuf.append(" - ");
        sbuf.append(event.getFormattedMessage());
        sbuf.append("\n");
        return sbuf.toString();
    }
}