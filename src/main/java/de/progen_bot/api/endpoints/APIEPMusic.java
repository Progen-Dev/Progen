package de.progen_bot.api.endpoints;

import de.progen_bot.api.entities.APIResponseObject;
import de.progen_bot.api.entities.MusicInfo;
import de.progen_bot.api.exceptions.APIWrongParametersException;
import de.progen_bot.core.Main;
import de.progen_bot.database.entities.config.GuildConfiguration;
import de.progen_bot.music.Music;
import net.dv8tion.jda.api.entities.Member;

import java.util.Map;

public class APIEPMusic extends Endpoint
{
    public APIEPMusic()
    {
        super("music");
    }

    @Override
    public APIResponseObject execute(Map<String, String> params, Member member, GuildConfiguration guildConfiguration)
    {
        if (member.getVoiceState() == null)
            return new APIResponseObject(200, false);
        if (!member.getVoiceState().inVoiceChannel() || member.getVoiceState().getChannel() == null)
            return new APIResponseObject(200, false);

        final Music music = Main.getMusicManager().getMusicByChannel(member.getVoiceState().getChannel());

        if (music == null)
            return new APIResponseObject(200, false);
        if (!params.containsKey("action"))
            return new APIResponseObject(200, new MusicInfo(music));

        switch (params.get("action"))
        {
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
                if (!params.containsKey("q"))
                    return new APIResponseObject(400, new APIWrongParametersException());
                music.loadTrack(params.get("q"), member);
                break;
            default:
                return new APIResponseObject(400, new APIWrongParametersException());
        }

        return new APIResponseObject(200, null);
    }
}
