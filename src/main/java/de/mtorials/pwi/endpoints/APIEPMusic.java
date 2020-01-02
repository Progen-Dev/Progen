package de.mtorials.pwi.endpoints;

import de.mtorials.entities.GuildMusic;
import de.mtorials.pwi.exceptions.APIWrongParametersException;
import de.mtorials.pwi.httpapi.API;
import de.mtorials.pwi.httpapi.APIResponseObject;
import de.mtorials.pwi.httpapi.Endpoint;
import de.progen_bot.commands.music.Music;
import de.progen_bot.db.entities.config.GuildConfiguration;
import net.dv8tion.jda.api.entities.Member;

import java.util.Map;

public class APIEPMusic extends Endpoint {

    public APIEPMusic() {
        super("music");
    }

    @Override
    public APIResponseObject execute(Map<String, String> params, Member member, GuildConfiguration guildConfiguration) {
        Music music = new Music();
        if (!params.containsKey("action")) return new APIResponseObject(200, new GuildMusic(member.getGuild()));

        switch (params.get("action")) {
            case "pause":
                music.getPlayer(member.getGuild()).setPaused(true);
                break;
            case "play":
                music.getPlayer(member.getGuild()).setPaused(false);
                break;
            case "skip":
                music.skip(member.getGuild());
                break;
            default:
                return new APIResponseObject(400, new APIWrongParametersException());
        }

        return new APIResponseObject(200, null);
    }
}
