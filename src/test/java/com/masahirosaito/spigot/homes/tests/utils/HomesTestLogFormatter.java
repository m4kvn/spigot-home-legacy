package com.masahirosaito.spigot.homes.tests.utils;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class HomesTestLogFormatter extends Formatter {
    private static final DateFormat df = new SimpleDateFormat("HH:mm:ss");

    public String format(LogRecord record) {
        StringBuilder ret = new StringBuilder();

        ret.append("[").append(df.format(record.getMillis())).append("] [")
                .append(record.getLoggerName()).append("] [")
                .append(record.getLevel().getLocalizedName()).append("] ");
        ret.append(record.getMessage());
        ret.append('\n');

        if (record.getThrown() != null) {
            StringWriter writer = new StringWriter();
            record.getThrown().printStackTrace(new PrintWriter(writer));
            ret.append(writer);
        }

        return ret.toString();
    }
}
