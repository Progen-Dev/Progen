package de.mtorials.pwi.endpoints;

import de.mtorials.pwi.exceptions.APIWrongParametersException;
import de.mtorials.pwi.httpapi.APIResponseObject;
import de.mtorials.pwi.httpapi.Endpoint;
import de.progen_bot.commands.music.CommandMusic;
import de.progen_bot.db.entities.config.GuildConfiguration;
import net.dv8tion.jda.api.entities.Member;

import java.util.Map;

public class APIEPMusic extends Endpoint {

    public APIEPMusic() {
        super("music");
    }

    @Override
    public APIResponseObject execute(Map<String, String> params, Member member, GuildConfiguration guildConfiguration) {
        CommandMusic commandMusic = new CommandMusic();
        if (!params.containsKey("action")) return new APIResponseObject(200, new GuildMusic(member.getGuild()));

        switch (params.get("action")) {
            case "pause":
                commandMusic.getPlayer(member.getGuild()).setPaused(true);
                break;
            case "play":
                commandMusic.getPlayer(member.getGuild()).setPaused(false);
                break;
            case "skip":
                commandMusic.skip(member.getGuild());
                break;
            default:
                return new APIResponseObject(400, new APIWrongParametersException());
        }

        return new APIResponseObject(200, null);
    }
}
