package de.progen_bot.database.dao.mute;

import de.progen_bot.database.entities.mute.MuteData;

import java.util.List;

public interface MuteDao
{
    void deleteMute(MuteData data);

    void saveMute(MuteData data);

    MuteData getMute(String victimId);

    List<MuteData> getMutesByGuild(String guildId);
}
