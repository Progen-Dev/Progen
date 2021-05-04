package de.progen_bot.utils.starboard;

public class Starboard
{
    private String messageId;
    private String textChannelId;

    public Starboard(String messageId, String textChannelId)
    {
        this.messageId = messageId;
        this.textChannelId = textChannelId;
    }

    public String getMessageId()
    {
        return messageId;
    }

    public void setMessageId(String messageId)
    {
        this.messageId = messageId;
    }

    public String getTextChannelId()
    {
        return textChannelId;
    }

    public void setTextChannelId(String textChannelId)
    {
        this.textChannelId = textChannelId;
    }
}
