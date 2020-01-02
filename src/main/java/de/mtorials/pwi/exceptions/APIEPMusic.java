package de.mtorials.pwi.exceptions;

import de.mtorials.entities.GuildMusic;
import de.mtorials.pwi.httpapi.APIResponseObject;
import de.mtorials.pwi.httpapi.Endpoint;
import de.progen_bot.commands.music.Music;
import de.progen_bot.core.Main;
import de.progen_bot.db.entities.config.GuildConfiguration;
import net.dv8tion.jda.api.entities.Member;

import javax.swing.*;
import java.util.Map;

public class APIEPMusic extends Endpoint {

    public APIEPMusic() {
        super("music");
    }

    @Override
    public APIResponseObject execute(Map<String, String> params, Member member, GuildConfiguration guildConfiguration) {
        Music music = new Music();
        if (params.size() == 1) return new APIResponseObject(200, new GuildMusic(member.getGuild()));
        return new APIResponseObject(404, null);
    }
}
