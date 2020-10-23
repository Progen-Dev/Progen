package de.progen_bot.util;

public class StarBoard {
    private String messageId;
    private String textChannelId;

    public StarBoard(String messageId, String textChannelId) {
        this.messageId = messageId;
        this.textChannelId = textChannelId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String guildId) {
        this.messageId = guildId;
    }

    public String getTextchannelId() {
        return textChannelId;
    }

    public void setTextchannelId(String textchannelId) {
        this.textChannelId = textchannelId;
    }

}
