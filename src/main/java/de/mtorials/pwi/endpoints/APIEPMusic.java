package de.mtorials.pwi.endpoints;

import de.mtorials.entities.json.MusicInfo;
import de.mtorials.pwi.exceptions.APIWrongParametersException;
import de.mtorials.pwi.httpapi.APIResponseObject;
import de.mtorials.pwi.httpapi.Endpoint;
import de.progen_bot.commands.music.CommandMusic;
import de.progen_bot.core.Main;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.music.Music;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;

import javax.sound.midi.VoiceStatus;
import java.util.Map;

public class APIEPMusic extends Endpoint {

    public APIEPMusic() {
        super("music");
    }

    @Override
    public APIResponseObject execute(Map<String, String> params, Member member, GuildConfiguration guildConfiguration) {

        if (member.getVoiceState() == null) return new APIResponseObject(200, false);

        if (!member.getVoiceState().inVoiceChannel() || member.getVoiceState().getChannel() == null) return new APIResponseObject(200, false);

        Music music = Main.getMusicManager().getMusicByChannel(member.getVoiceState().getChannel());
        if (music == null) return new APIResponseObject(200, false);
        if (!params.containsKey("action")) return new APIResponseObject(200, new MusicInfo(music));
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
            case "stop":
                music.stop();
                break;
            case "queue":
                if (!params.containsKey("q")) return new APIResponseObject(400, new APIWrongParametersException());
                music.loadTrack(params.get("q"), member);
                break;
            default:
                return new APIResponseObject(400, new APIWrongParametersException());
        }

        return new APIResponseObject(200, null);
    }
}
