package de.progen_bot.misc;

import de.progen_bot.utils.statics.Settings;
import io.github.jdiscordbots.nightdream.logging.LogType;
import io.github.jdiscordbots.nightdream.logging.NDLogger;

public class Logger
{
    public static final NDLogger ND_LOGGER = NDLogger.getLogger("Progen");

    static
    {
        /* Enable debug logging */
        if (Settings.DEBUG)
            ND_LOGGER.setMinimum(LogType.DEBUG);
    }

    private Logger()
    {
        /* Prevent instantiation */
    }

    public static void log(String message, int type)
    {
        if (type == 0)
            ND_LOGGER.log(LogType.INFO, message);
        else if (type == 1)
            ND_LOGGER.log(LogType.ERROR, message);
        else if (type == 2)
            ND_LOGGER.log(LogType.FATAL, message);
        else
            ND_LOGGER.log(LogType.QUESTION, message);
    }

    public static void info(String message)
    {
        log(message, 0);
    }

    public static void debug(String message)
    {
        ND_LOGGER.log(LogType.DEBUG, message);
    }
}
