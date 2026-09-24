package com.skd.mysticalrealmsmodpackfixes.log;

import com.skd.mysticalrealmsmodpackfixes.config.MRMFConfig;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.ParameterizedMessage;

import java.util.List;

public class BenignLogFilter extends AbstractFilter {

    public BenignLogFilter() {
        super(Result.NEUTRAL, Result.NEUTRAL);
    }

    private Result check(String pattern, Object[] params, String loggerName) {
        if (params != null && params.length > 0 && pattern != null && pattern.contains("{}")) {
            return check(ParameterizedMessage.format(pattern, params), loggerName);
        }
        return check(pattern, loggerName);
    }

    private Result check(String formattedMessage, String loggerName) {
        if (formattedMessage == null) {
            return Result.NEUTRAL;
        }
        if (!MRMFConfig.enabled()) {
            return Result.NEUTRAL;
        }
        if (loggerName != null && loggerName.startsWith("com.skd.mysticalrealmsmodpackfixes")) {
            return Result.NEUTRAL;
        }
        List<String> patterns = MRMFConfig.patterns();
        for (String pattern : patterns) {
            if (formattedMessage.contains(pattern)) {
                return Result.DENY;
            }
        }
        return Result.NEUTRAL;
    }

    @Override
    public Result filter(LogEvent event) {
        return check(event.getMessage().getFormattedMessage(), event.getLoggerName());
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
        return check(msg, params, logger.getName());
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object p0) {
        return check(msg, new Object[] {p0}, logger.getName());
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1) {
        return check(msg, new Object[] {p0, p1}, logger.getName());
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2) {
        return check(msg, new Object[] {p0, p1, p2}, logger.getName());
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3) {
        return check(msg, new Object[] {p0, p1, p2, p3}, logger.getName());
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4) {
        return check(msg, new Object[] {p0, p1, p2, p3, p4}, logger.getName());
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
        return check(msg, new Object[] {p0, p1, p2, p3, p4, p5}, logger.getName());
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
        return check(msg, new Object[] {p0, p1, p2, p3, p4, p5, p6}, logger.getName());
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
        return check(msg, new Object[] {p0, p1, p2, p3, p4, p5, p6, p7}, logger.getName());
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
        return check(msg, new Object[] {p0, p1, p2, p3, p4, p5, p6, p7, p8}, logger.getName());
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
        return check(msg, new Object[] {p0, p1, p2, p3, p4, p5, p6, p7, p8, p9}, logger.getName());
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Object obj, Throwable t) {
        if (obj instanceof Message) {
            return check(((Message) obj).getFormattedMessage(), logger.getName());
        }
        return check(String.valueOf(obj), logger.getName());
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
        return check(msg.getFormattedMessage(), logger.getName());
    }
}
