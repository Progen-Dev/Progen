package de.progen_bot.commands.Moderator;

import de.mtorials.config.GuildConfiguration;
import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.core.PermissionCore;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

public class Mute extends CommandHandler {

    public Mute() {
        super("mute", "mute <user>", "mute a user");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration guildConfiguration) {

        if (PermissionCore.check(1, event)) return;

        EmbedBuilder error = new EmbedBuilder().setColor(Color.RED).setTitle("Error");
        EmbedBuilder ok = new EmbedBuilder().setColor(Color.green).setTitle("Erfolgreich");
        if (!event.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            error.setDescription("Du hast keine Rechte dafür");
            event.getTextChannel().sendMessage(error.build()).queue();
            return;
        }
        if (event.getMessage().getMentionedMembers().size() != 1) {
            error.setDescription("Du musst einen User auswählen");
            event.getTextChannel().sendMessage(error.build()).queue();
            return;
        }
        if (!(event.getGuild().getRolesByName("progen-muted", false).size() > 0)) {
            event.getGuild().createRole().setName("progen-muted").complete();
            Role muted = event.getGuild().getRolesByName("progen-muted", false).get(0);
            event.getGuild().getTextChannels().forEach(tchan -> tchan.createPermissionOverride(muted).setDeny(Permission.MESSAGE_WRITE).complete());
            event.getGuild().getVoiceChannels().forEach(vchan -> vchan.createPermissionOverride(muted).setDeny(Permission.VOICE_SPEAK).complete());
        }
        event.getGuild().addRoleToMember(event.getMessage().getMentionedMembers().get(0),
                event.getGuild().getRolesByName("progen-muted", false).get(0)).complete();
        ok.setDescription("User " + event.getMessage().getMentionedMembers().get(0).getAsMention() + " Wurde erfolgreich gemuted");
        event.getTextChannel().sendMessage(ok.build()).queue();
    }


    @Override
    public String help() {
        return null;
    }
}
