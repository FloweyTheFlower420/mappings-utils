package com.floweytf.utils.logger;

import org.eclipse.jetty.util.log.Logger;
import org.slf4j.helpers.MessageFormatter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.floweytf.utils.logger.JavaChalk.*;

public class BetterLogger implements Logger {
    public static final int FATAL = 0;
    public static final int ERROR = 1;
    public static final int WARN = 2;
    public static final int INFO = 3;
    public static final int DEBUG = 4;
    public static final Transport stdout = new ConsoleTransport(INFO);

    public String loggerName;
    private final List<Transport> out;

    // Internal utilities
    protected String prefix(String level) {
        return String.format("[%s] [%s] [%s]: ", level, DateTimeFormatter.ofPattern("MM-dd-yyyy hh:mm:ss").format(ZonedDateTime.now()), this.loggerName);
    }

    protected void writeLnTransport(String s, int level) {
        for (Transport t : out) {
            switch (level) {
                case FATAL:
                    t.write(prefix(red("FATAL")) + s + '\n', FATAL);
                    break;
                case ERROR:
                    t.write(prefix(red("ERROR")) + s + '\n', ERROR);
                    break;
                case WARN:
                    t.write(prefix(yellow("WARN")) + s + '\n', WARN);
                    break;
                case INFO:
                    t.write(prefix(cyan("INFO")) + s + '\n', INFO);
                    break;
                case DEBUG:
                    t.write(prefix(gray("DEBUG")) + s + '\n', DEBUG);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + level);
            }
        }
    }

    protected String format(String s, Object... o) {
        return MessageFormatter.arrayFormat(s, o).getMessage();
    }

    // other functions

    public BetterLogger(String name) {
        this.loggerName = name;
        out = new ArrayList<>();
    }

    public BetterLogger(BetterLogger b, String name) {
        this.loggerName = name;
        this.out = b.out;
    }

    public BetterLogger(Class<?> C) {
        this.loggerName = C.getName();
        out = new ArrayList<>();
    }

    public BetterLogger(BetterLogger b, Class<?> C) {
        this.loggerName = C.getName();
        this.out = b.out;
    }

    public BetterLogger(BetterLogger rhs) {
        this.out = rhs.out;
        this.loggerName = rhs.loggerName;
    }

    public BetterLogger addTransport(Transport t) {
        out.add(t);
        return this;
    }

    @Override
    public Logger getLogger(String name) {
        return this;
    }

    @Override
    public void ignore(Throwable ignored) {
        writeLnTransport("ignored: " + ignored.toString(), INFO);
    }

    @Override
    public boolean isDebugEnabled() {
        return true;
    }

    @Override
    public void setDebugEnabled(boolean enabled) {

    }

    @Override
    public String getName() {
        return loggerName;
    }

    // ------------------------------- DEBUG ------------------------------- //
    public void debug(String msg) {
        writeLnTransport(msg, DEBUG);
    }

    public void debug(Object... msg) {
        StringBuilder b = new StringBuilder();
        for (Object o : msg) {
            b.append(o.toString());
        }
        writeLnTransport(b.toString(), DEBUG);
    }

    public void debug(String msg, Object... obj) {
        writeLnTransport(format(msg, obj), DEBUG);
    }

    @Override
    public void debug(String msg, long value) {
        writeLnTransport(msg, DEBUG);
    }

    @Override
    public void debug(Throwable thrown) {
        writeLnTransport(thrown.toString(), DEBUG);
    }

    @Override
    public void debug(String msg, Throwable thrown) {
        writeLnTransport(msg + '\n' + thrown.toString(), DEBUG);
    }

    // ------------------------------- INFO ------------------------------- //
    public void info(String msg) {
        writeLnTransport(msg, INFO);
    }

    public void info(Object... msg) {
        StringBuilder b = new StringBuilder();
        for (Object o : msg) {
            b.append(o.toString());
        }
        writeLnTransport(b.toString(), INFO);
    }

    public void info(String msg, Object... obj) {
        writeLnTransport(format(msg, obj), INFO);
    }

    @Override
    public void info(Throwable thrown) {
        writeLnTransport(thrown.toString(), INFO);
    }

    @Override
    public void info(String msg, Throwable thrown) {
        writeLnTransport(msg + thrown.toString(), INFO);
    }

    public void info(String msg, Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        writeLnTransport(msg + '\n' + sw.toString(), INFO);
    }

    // warn
    public void warn(String msg) {
        writeLnTransport(msg, WARN);
    }

    public void warn(Object... msg) {
        StringBuilder b = new StringBuilder();
        for (Object o : msg) {
            b.append(o.toString());
        }
        writeLnTransport(b.toString(), WARN);
    }

    public void warn(String msg, Object... obj) {
        writeLnTransport(format(msg, obj), WARN);
    }

    @Override
    public void warn(Throwable thrown) {
        writeLnTransport(thrown.toString(), WARN);
    }

    @Override
    public void warn(String msg, Throwable thrown) {
        writeLnTransport(msg + thrown.toString(), WARN);
    }

    public void warn(String msg, Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        writeLnTransport(msg + '\n' + sw.toString(), WARN);
    }

    // error
    public void error(String msg) {
        writeLnTransport(msg, ERROR);
    }

    public void error(Object... msg) {
        StringBuilder b = new StringBuilder();
        for (Object o : msg) {
            b.append(o.toString());
        }
        writeLnTransport(b.toString(), ERROR);
    }

    public void error(String msg, Object... obj) {
        writeLnTransport(format(msg, obj), ERROR);
    }

    public void error(String msg, Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        writeLnTransport(msg + '\n' + sw.toString(), ERROR);
    }

    // fatal
    public void fatal(int ret, String msg) {
        writeLnTransport(msg, FATAL);
        System.exit(ret);
    }

    public void fatal(int ret, Object... msg) {
        StringBuilder b = new StringBuilder();
        for (Object o : msg) {
            b.append(o.toString());
        }
        writeLnTransport(b.toString(), FATAL);
        System.exit(ret);
    }

    public void fatal(int ret, String msg, Object... obj) {
        writeLnTransport(format(msg, obj), FATAL);
        System.exit(ret);
    }

    public void fatal(int ret, String msg, Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        writeLnTransport(msg + '\n' + sw.toString(), FATAL);
        System.exit(ret);
    }
}