package de.progen_bot.database.entities.config;

public class GuildConfiguration
{
    private String prefix;
    private long logChannelId;
    private long tempChannelCategoryId;
    private long autorole;

    private GuildConfiguration(String prefix, long logChannelId, long tempChannelCategoryId, long autorole)
    {
        this.prefix = prefix;
        this.logChannelId = logChannelId;
        this.tempChannelCategoryId = tempChannelCategoryId;
        this.autorole = autorole;
    }

    public static class Builder
    {
        private String prefix = "pb!";
        private long logChannelId;
        private long tempChannelCategoryId;
        private long autorole;

        public Builder setPrefix(String prefix)
        {
            this.prefix = prefix;
            return this;
        }

        public Builder setLogChannelId(long logChannelId)
        {
            this.logChannelId = logChannelId;
            return this;
        }

        public Builder setTempChannelCategoryId(long tempChannelCategoryId)
        {
            this.tempChannelCategoryId = tempChannelCategoryId;
            return this;
        }

        public Builder setAutorole(long autorole)
        {
            this.autorole = autorole;

            return this;
        }

        public GuildConfiguration build()
        {
            return new GuildConfiguration(this.prefix, this.logChannelId, this.tempChannelCategoryId, this.autorole);
        }
    }

    public String getPrefix()
    {
        return prefix;
    }

    public void setPrefix(String prefix)
    {
        this.prefix = prefix;
    }

    public long getLogChannelId()
    {
        return logChannelId;
    }

    public void setLogChannelId(long logChannelId)
    {
        this.logChannelId = logChannelId;
    }

    public long getTempChannelCategoryId()
    {
        return tempChannelCategoryId;
    }

    public void setTempChannelCategoryId(long tempChannelCategoryId)
    {
        this.tempChannelCategoryId = tempChannelCategoryId;
    }

    public long getAutorole()
    {
        return autorole;
    }

    public void setAutoRole(long autoRole)
    {
        this.autorole = autoRole;
    }
}
