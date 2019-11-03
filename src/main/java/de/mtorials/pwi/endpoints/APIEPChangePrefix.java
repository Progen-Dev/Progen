package de.mtorials.pwi.endpoints;

import de.mtorials.config.GuildConfiguration;
import de.mtorials.config.GuildConfigurationBuilder;
import de.mtorials.pwi.httpapi.APICommand;
import de.mtorials.pwi.httpapi.APIResponseObject;
import de.progen_bot.core.Main;
import net.dv8tion.jda.api.entities.Member;

import java.util.Map;

public class APIEPChangePrefix extends APICommand {

    public APIEPChangePrefix() {
        super("changePrefix");
    }

    @Override
    public APIResponseObject execute(Map<String, String> params, Member member) {

        if (!params.containsKey("newPrefix")) return new APIResponseObject(400, null);
        if (!member.isOwner()) return new APIResponseObject(401, null);

        GuildConfiguration oldconfig = Main.getConfiguration().getGuildConfiguration(member.getGuild());
        Main.getConfiguration().writeGuildConfiguration(member.getGuild(), new GuildConfigurationBuilder().setGuildConfig(oldconfig).setPrefix(params.get("newPrefix")).build());

        return new APIResponseObject(200, null);
    }
}
