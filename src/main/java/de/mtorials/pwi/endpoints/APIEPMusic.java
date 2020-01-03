package de.mtorials.pwi.endpoints;

import de.mtorials.pwi.exceptions.APIWrongParametersException;
import de.mtorials.pwi.httpapi.APIResponseObject;
import de.mtorials.pwi.httpapi.Endpoint;
import de.progen_bot.commands.music.CommandMusic;
import de.progen_bot.core.Main;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.music.Music;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.Map;

public class APIEPMusic extends Endpoint {

    public APIEPMusic() {
        super("music");
    }

    @Override
    public APIResponseObject execute(Map<String, String> params, Member member, GuildConfiguration guildConfiguration) {

        Music music = Main.getMusicManager().getMusicByChannel(member.getVoiceState().getChannel());
        if (!params.containsKey("action")) return new APIResponseObject(200, music);

        switch (params.get("action")) {
            case "pause":
                music.getPlayer().setPaused(true);
                break;
            case "play":
                music.getPlayer().setPaused(false);
                break;
            case "skip":
                music.skip();
                break;
            default:
                return new APIResponseObject(400, new APIWrongParametersException());
        }

        return new APIResponseObject(200, null);
    }
}
