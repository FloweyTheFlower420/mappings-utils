package org.slf4j.impl;

import org.slf4j.Logger;
import org.slf4j.Marker;
import com.floweytf.utils.logger.BetterLogger;
import org.slf4j.helpers.MessageFormatter;

public class BetterLoggerSLF4J extends BetterLogger implements Logger {
    public BetterLoggerSLF4J(String name) {
        super(name);
    }

    @Override
    public boolean isTraceEnabled() {
        return false;
    }
    @Override
    public boolean isTraceEnabled(Marker marker) {
        return false;
    }
    @Override
    public boolean isDebugEnabled(Marker marker) {
        return true;
    }
    @Override
    public boolean isInfoEnabled() {
        return true;
    }
    @Override
    public boolean isInfoEnabled(Marker marker) {
        return true;
    }
    @Override
    public boolean isWarnEnabled() {
        return true;
    }
    @Override
    public boolean isWarnEnabled(Marker marker) {
        return true;
    }
    @Override
    public boolean isErrorEnabled() {
        return true;
    }
    @Override
    public boolean isErrorEnabled(Marker marker) {
        return true;
    }

    @Override
    public void trace(String msg) {

    }
    @Override
    public void trace(String format, Object arg) {

    }
    @Override
    public void trace(String format, Object arg1, Object arg2) {

    }
    @Override
    public void trace(String format, Object... arguments) {

    }
    @Override
    public void trace(String msg, Throwable t) {

    }
    @Override
    public void trace(Marker marker, String msg) {

    }
    @Override
    public void trace(Marker marker, String format, Object arg) {

    }
    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {

    }
    @Override
    public void trace(Marker marker, String format, Object... argArray) {

    }
    @Override
    public void trace(Marker marker, String msg, Throwable t) {

    }

    @Override
    public void debug(String format, Object arg) {
        writeLnTransport(MessageFormatter.format(format, arg).getMessage(), DEBUG);
    }
    @Override
    public void debug(String format, Object arg1, Object arg2) {
        writeLnTransport(MessageFormatter.format(format, arg1, arg2).getMessage(), DEBUG);
    }
    @Override
    public void debug(Marker marker, String format, Object arg) {
        debug(format, arg);
    }
    @Override
    public void debug(Marker marker, String msg) {
        debug(msg);
    }
    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        debug(format, arg1, arg2);
    }
    @Override
    public void debug(Marker marker, String format, Object... arguments) {
        debug(format, arguments);
    }
    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        super.debug(msg, t);
    }

    @Override
    public void info(String format, Object arg) {
        writeLnTransport(MessageFormatter.format(format, arg).toString(), INFO);
    }
    @Override
    public void info(String format, Object arg1, Object arg2) {
        writeLnTransport(MessageFormatter.format(format, arg1, arg2).toString(), INFO);
    }
    @Override
    public void info(Marker marker, String msg) {
        info(msg);
    }
    @Override
    public void info(Marker marker, String format, Object arg) {
        info(format, arg);
    }
    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        info(format, arg1, arg2);
    }
    @Override
    public void info(Marker marker, String format, Object... arguments) {
        info(format, arguments);
    }
    @Override
    public void info(Marker marker, String msg, Throwable t) {
        super.info(t);
    }

    @Override
    public void warn(String format, Object arg) {
        writeLnTransport(MessageFormatter.format(format, arg).toString(), WARN);
    }
    @Override
    public void warn(String format, Object arg1, Object arg2) {
        writeLnTransport(MessageFormatter.format(format, arg1, arg2).toString(), WARN);
    }
    @Override
    public void warn(Marker marker, String msg) {
        warn(msg);
    }
    @Override
    public void warn(Marker marker, String format, Object arg) {
        warn(format, arg);
    }
    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        warn(format, arg1, arg2);
    }
    @Override
    public void warn(Marker marker, String format, Object... arguments) {
        warn(format, arguments);
    }
    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        super.warn(msg, t);
    }

    @Override
    public void error(String format, Object arg) {
        writeLnTransport(MessageFormatter.format(format, arg).toString(), ERROR);
    }
    @Override
    public void error(String format, Object arg1, Object arg2) {
        writeLnTransport(MessageFormatter.format(format, arg1, arg2).toString(), ERROR);
    }
    @Override
    public void error(String msg, Throwable t) {
        super.error(msg, t);
    }
    @Override
    public void error(Marker marker, String msg) {
        error(msg);
    }
    @Override
    public void error(Marker marker, String format, Object arg) {
        error(format, arg);
    }
    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        error(format, arg1, arg2);
    }
    @Override
    public void error(Marker marker, String format, Object... arguments) {
        error(format, arguments);
    }
    @Override
    public void error(Marker marker, String msg, Throwable t) {
        error(msg, t);
    }
}
