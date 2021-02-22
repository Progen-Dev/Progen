package de.progen_bot.database.entities.mute;

import de.progen_bot.database.dao.mute.MuteDaoImpl;

// FIXME: 22.02.2021 how about guild in get method?
public class MuteData
{
    private final String victimId;
    private final String reason;
    private final String executorId;
    private final String guildId;

    public MuteData(String victimId, String reason, String executorId, String guildId)
    {
        this.victimId = victimId;
        this.reason = reason;
        this.executorId = executorId;
        this.guildId = guildId;
    }

    public static MuteData getMuteData(String victimId)
    {
        return new MuteDaoImpl().getMute(victimId);
    }

    public String getVictimId()
    {
        return victimId;
    }

    public String getReason()
    {
        return reason;
    }

    public String getExecutorId()
    {
        return executorId;
    }

    public String getGuildId()
    {
        return guildId;
    }

    public void delete()
    {
        new MuteDaoImpl().deleteMute(this);
    }

    public void save()
    {
        new MuteDaoImpl().saveMute(this);
    }
}

