package de.mtorials.misc;

import io.github.jdiscordbots.nightdream.logging.LogType;
import io.github.jdiscordbots.nightdream.logging.NDLogger;

public class Logger {

    public static final NDLogger LOGGER = NDLogger.getLogger("Progen");

    private Logger() {
        /* Prevent instantiation */
    }

    public static void log(String msg, int type) {

        if (type == 0) {
            LOGGER.log(LogType.INFO, msg);
        } else if (type == 1) {
            LOGGER.log(LogType.ERROR, msg);
        } else if (type == 2) {
            LOGGER.log(LogType.FATAL, msg);
        }
    }

    public static void info(String msg) {
        log(msg, 0);
    }
}
