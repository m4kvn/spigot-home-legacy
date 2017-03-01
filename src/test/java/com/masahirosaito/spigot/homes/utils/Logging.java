package com.masahirosaito.spigot.homes.utils;

import java.util.logging.Logger;

public class Logging {

    static final InterceptedLogger LOG = new InterceptedLogger(Logger.getLogger("Minecraft"));

    static class InterceptedLogger extends Logger {
        final Logger logger;

        InterceptedLogger(final Logger logger) {
            super(logger.getName(), logger.getResourceBundleName());
            this.logger = logger;
        }
    }

    public static Logger getLogger() {
        return LOG;
    }
}
