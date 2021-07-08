package de.progen_bot.db.entities.channel;

public class ChannelData {
    private String logChannelId;
    private String starBoardChannelId;
    private String voiceLogChannelId;

    public ChannelData(String logChannelId , String starBoardChannelId , String voiceLogChannelId){
        this.logChannelId = logChannelId;
        this.starBoardChannelId = starBoardChannelId;
        this.voiceLogChannelId = voiceLogChannelId;
    }

    public String getLogChannelId(){return logChannelId;}
    public void setLogChannelId(String id){this.logChannelId = logChannelId;}

    public String getStarBoardChannelId(){return starBoardChannelId;}
    public void setStarBoardChannelId(){this.starBoardChannelId = starBoardChannelId;}

    public String getVoiceLogChannelId(){return voiceLogChannelId;}
    public void setVoiceLogChannelId(){this.voiceLogChannelId = voiceLogChannelId;}
}
