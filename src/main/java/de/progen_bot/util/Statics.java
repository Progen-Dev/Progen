package de.progen_bot.util;

import java.util.Date;

public class Statics {

    private Statics() {
        /* Prevent instantiation */
    }

    public static final int XP_GAIN = 2;
    public static final int XP_GAIN_VOTE = 5;
    public static final String VERSION = "1.0.0.6";
    public static final String LAST_VERSION = "1.0.0.5";
    public static final String GUILD_JOIN_ROLE = "";
    public static final String UNIVERSAL_INFO_CMD_WITHOUT_PREFIX = "info";

    private static Date lastRestart;
    private static int reconnectCount = 0;

    /**
     * Gets the date of restart
     *
     * @return {@link Date} of latest restart
     */
    public static Date getLastRestart() {
        return lastRestart;
    }

    /**
     * Sets the date of latest restart
     *
     * @param lastRestart {@link Date} of new restart
     */
    public static void setLastRestart(Date lastRestart) {
        Statics.lastRestart = lastRestart;
    }

    /**
     * Gets the reconnect count since latest restart
     *
     * @return {@link Integer reconnect count}
     */
    public static int getReconnectCount() {
        return reconnectCount;
    }

    /**
     * Increases the reconnect count by one
     */
    public static void increaseReconnectCount() {
        Statics.reconnectCount++;
    }
}
