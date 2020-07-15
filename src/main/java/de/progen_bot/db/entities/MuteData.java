package de.progen_bot.db.entities;

import de.progen_bot.db.dao.mute.MuteDao;

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
        new MuteDao().deleteMute(this);
    }

    public void save()
    {
        new MuteDao().saveMute(this);
    }

    public static MuteData getMuteData(String victimId)
    {
        return new MuteDao().getMute(victimId);
    }
}
