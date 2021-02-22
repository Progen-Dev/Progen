package de.progen_bot.database.entities.connectfour;

public class GameData
{
    private String messageId;
    private String opponentId;
    private String challengerId;
    private String channelId;

    private int height;
    private int width;

    public String getMessageId()
    {
        return messageId;
    }

    public void setMessageId(String messageId)
    {
        this.messageId = messageId;
    }

    public String getOpponentId()
    {
        return opponentId;
    }

    public void setOpponentId(String opponentId)
    {
        this.opponentId = opponentId;
    }

    public String getChallengerId()
    {
        return challengerId;
    }

    public void setChallengerId(String challengerId)
    {
        this.challengerId = challengerId;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public void setChannel(String id)
    {
        this.channelId = id;
    }

    public String getChannelId()
    {
        return channelId;
    }

}
