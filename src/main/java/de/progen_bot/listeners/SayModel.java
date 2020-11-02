package de.progen_bot.listeners;

import net.dv8tion.jda.api.EmbedBuilder;

public class SayModel {
    private String textchannelId;
    private int stage;
    private EmbedBuilder embedBuilder;

    public SayModel(String textchannelId, EmbedBuilder embedBuilder) {
        this.textchannelId = textchannelId;
        this.stage = 0;
        this.embedBuilder = embedBuilder;
    }

    public String getTextchannelId() {
        return textchannelId;
    }

    public void setTextchannelId(String textchannelId) {
        this.textchannelId = textchannelId;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public EmbedBuilder getEmbedBuilder() {
        return embedBuilder;
    }

    public void setEmbedBuilder(EmbedBuilder embedBuilder) {
        this.embedBuilder = embedBuilder;
    }
}
