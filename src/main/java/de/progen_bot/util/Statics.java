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

    public static Date lastRestart;
    public static int reconnectCount = 0;

    public static String guildJoinRole = "";


    public static String BOT_OWNER_ShowMeYourSkil = "402140322525872138";
    public static String BOT_OWNER_MTORIALS = "279271145205923847";
    public static String BOT_OWNER_EVOH = "225327305570910208";
}
