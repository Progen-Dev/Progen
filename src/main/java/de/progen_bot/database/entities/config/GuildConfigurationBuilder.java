package de.progen_bot.database.entities.config;

public class GuildConfigurationBuilder
{
    private String prefix = "pb!";
    private long logChannelId;
    private long tempChannelCategoryId;
    private long autorole;

    public GuildConfigurationBuilder setPrefix(String prefix)
    {
        this.prefix = prefix;
        return this;
    }

    public GuildConfigurationBuilder setLogChannelId(long logChannelId)
    {
        this.logChannelId = logChannelId;
        return this;
    }

    public GuildConfigurationBuilder setTempChannelCategoryId(long tempChannelCategoryId)
    {
        this.tempChannelCategoryId = tempChannelCategoryId;
        return this;
    }

    public GuildConfigurationBuilder setAutorole(long autorole)
    {
        this.autorole = autorole;

        return this;
    }

    public GuildConfiguration build()
    {
        return new GuildConfiguration(this.prefix, this.logChannelId, this.tempChannelCategoryId, this.autorole);
    }
}
