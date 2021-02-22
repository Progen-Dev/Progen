package de.progen_bot.api.endpoints;

import de.progen_bot.api.entities.APIResponseObject;
import de.progen_bot.database.dao.warnlist.WarnListDaoImpl;
import de.progen_bot.database.entities.config.GuildConfiguration;
import net.dv8tion.jda.api.entities.Member;

import java.util.List;
import java.util.Map;

public class APIEPGetWarns extends Endpoint
{
    public APIEPGetWarns()
    {
        super("getWarns");
    }

    @Override
    public APIResponseObject execute(Map<String, String> params, Member member, GuildConfiguration guildConfiguration)
    {
        final List<String> warns = new WarnListDaoImpl().loadWarnList(member);

        return new APIResponseObject(200, warns);
    }
}
