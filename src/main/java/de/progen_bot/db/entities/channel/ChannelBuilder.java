package de.progen_bot.db.entities.channel;

public class ChannelBuilder {
    private String logChannelId;
    private String starBoardChannelId;
    private String voiceLogChannelId;

    public ChannelBuilder setLogChannelId(String logChannelId){
        this.logChannelId = logChannelId;
        return this;
    }

    public ChannelBuilder setStarBoardChannelId(String starBoardChannelId){
        this.starBoardChannelId = starBoardChannelId;
        return this;
    }

    public ChannelBuilder setVoiceLogChannelId(String voiceLogChannelId){
        this.voiceLogChannelId = voiceLogChannelId;
        return this;
    }

    public ChannelData build(){
        return new ChannelData(this.logChannelId, this.starBoardChannelId, this.voiceLogChannelId);
    }
}
