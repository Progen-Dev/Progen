package de.progen_bot.util;

import java.util.Date;

public class Statics {

    private Statics() {
        /* Prevent instantiation */
    }

    public static final int XP_GAIN = 2;
    public static final int XP_GAIN_VOTE = 5;
    public static final String VERSION = "1.0.0.4";
    public static final String LAST_VERSION = "1.0.0.3";
    public static final String BOT_OWNER_ShowMeYourSkil = "402140322525872138";
    public static final String BOT_OWNER_MTORIALS = "279271145205923847";
    public static final String BOT_OWNER_EVOH = "225327305570910208";
    public static final String GUILD_JOIN_ROLE = "";

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
