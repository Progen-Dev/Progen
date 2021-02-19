package de.progen_bot.misc;

import io.github.jdiscordbots.nightdream.logging.LogType;
import io.github.jdiscordbots.nightdream.logging.NDLogger;

public class Logger
{
    public static final NDLogger LOGGER = NDLogger.getLogger("Progen");

    private Logger()
    {
        /* Prevent instantiation */
    }

    public static void log(String message, int type)
    {
        if (type == 0)
            LOGGER.log(LogType.INFO, message);
        else if (type == 1)
            LOGGER.log(LogType.ERROR, message);
        else if (type == 2)
            LOGGER.log(LogType.FATAL, message);
        else
            LOGGER.log(LogType.QUESTION, message);
    }
}
