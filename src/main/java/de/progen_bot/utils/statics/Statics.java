package de.progen_bot.utils.statics;

import java.util.Date;

public class Statics
{
    public static final int XP_GAIN = 2;
    public static final int XP_GAIN_VOTE = 5;
    public static final String VERSION = "1.0.9";
    public static final String PREVIOUS_VERSION = "1.0.8";
    public static final String GUILD_JOIN_ROLE = "";
    public static final String UNIVERSAL_INFO_CMD_WITHOUT_PREFIX = "info";
    public static final Date LAST_RESTART = new Date();
    private static int reconnectCount;

    private Statics()
    {
        /* Prevent instantiation */
    }

    public static int getReconnectCount()
    {
        return reconnectCount;
    }

    public static void increaseReconnectCount()
    {
        Statics.reconnectCount++;
    }
}
