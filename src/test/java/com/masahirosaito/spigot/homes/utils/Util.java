package com.masahirosaito.spigot.homes.utils;

import java.util.logging.*;

public class Util {
    private Util() {
    }

    public static final Logger logger = Logger.getLogger("Homes");

    static {
        logger.setUseParentHandlers(false);

        Handler handler = new ConsoleHandler();
        handler.setFormatter(new HomesTestLogFormatter());
        Handler[] handlers = logger.getHandlers();

        for (Handler h : handlers) logger.removeHandler(h);

        logger.addHandler(handler);
    }

    public static void log(Throwable t) {
        log(Level.WARNING, t.getLocalizedMessage(), t);
    }

    public static void log(Level level, Throwable t) {
        log(level, t.getLocalizedMessage(), t);
    }

    public static void log(String message, Throwable t) {
        log(Level.WARNING, message, t);
    }

    public static void log(Level level, String message, Throwable t) {
        LogRecord record = new LogRecord(level, message);
        record.setThrown(t);
        logger.log(record);
    }

    public static void log(String message) {
        log(Level.INFO, message);
    }

    public static void log(Level level, String message) {
        logger.log(level, message);
    }
}
