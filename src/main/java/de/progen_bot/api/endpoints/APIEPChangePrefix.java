package de.progen_bot.api.endpoints;

import de.progen_bot.api.entities.APIResponseObject;
import de.progen_bot.database.dao.config.ConfigDaoImpl;
import de.progen_bot.database.entities.config.GuildConfiguration;
import net.dv8tion.jda.api.entities.Member;

import java.util.Map;

public class APIEPChangePrefix extends Endpoint
{
    public APIEPChangePrefix()
    {
        super("changePrefix");
    }

    @Override
    public APIResponseObject execute(Map<String, String> params, Member member, GuildConfiguration guildConfiguration)
    {
        if (!params.containsKey("newPrefix"))
            return new APIResponseObject(400, null);
        if (!member.isOwner())
            return new APIResponseObject(401, null);

        guildConfiguration.setPrefix(params.get("newPrefix"));
        new ConfigDaoImpl().writeConfig(guildConfiguration, member.getGuild().getIdLong());

        return new APIResponseObject(200, null);
    }
}
