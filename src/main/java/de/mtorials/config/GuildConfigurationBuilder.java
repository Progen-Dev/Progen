package de.mtorials.config;

public class GuildConfigurationBuilder {

    private String preifx;
    private String logChannelID;
    private String tempChannelCatergoryID;

    public GuildConfigurationBuilder setPrefix(String prefix) {
        this.preifx = prefix;
        return this;
    }

    public GuildConfigurationBuilder setLogChannelID(String logChannelID) {
        this.logChannelID = logChannelID;
        return this;
    }

    public GuildConfigurationBuilder setTempChannelCatergoryID(String tempChannelCatergoryID) {
        this.tempChannelCatergoryID = tempChannelCatergoryID;
        return this;
    }

    public GuildConfiguration build() {
        return new GuildConfiguration(this.preifx, this.logChannelID, this.tempChannelCatergoryID);
    }
}
