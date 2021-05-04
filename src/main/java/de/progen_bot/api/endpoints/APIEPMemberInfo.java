package de.progen_bot.api.endpoints;

import de.progen_bot.api.entities.APIResponseObject;
import de.progen_bot.api.entities.MemberInfo;
import de.progen_bot.database.entities.config.GuildConfiguration;
import de.progen_bot.utils.logger.Logger;
import io.github.jdiscordbots.nightdream.logging.LogType;
import net.dv8tion.jda.api.entities.Member;

import java.util.Map;

public class APIEPMemberInfo extends Endpoint
{
    public APIEPMemberInfo()
    {
        super("getMemberInfo");
    }

    @Override
    public APIResponseObject execute(Map<String, String> params, Member member, GuildConfiguration guildConfiguration)
    {
        Logger.ND_LOGGER.log(LogType.DEBUG, "Command member info");

        return new APIResponseObject(200, new MemberInfo(member));
    }
}
