package de.progen_bot.commands.moderator;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.core.PermissionCore;
import de.progen_bot.db.entities.config.GuildConfiguration;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

public class UnMute extends CommandHandler {
    public UnMute() {
        super("unmute", "unmute <user>", "unmute a muted user");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {
        if (PermissionCore.check(1, event)) return;

        if (event.getMember() == null)
            return;

        EmbedBuilder error = new EmbedBuilder().setColor(Color.RED).setTitle("Error");
        Guild guild = event.getGuild();

        if (!event.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            error.setDescription("Keine berechtigung");
            event.getTextChannel().sendMessage(error.build()).queue();
            return;
        }

        if (event.getMessage().getMentionedMembers().size() != 1) {
            error.setDescription("Du musst einen User mentionen");
            event.getTextChannel().sendMessage(error.build()).queue();
            return;
        }

        guild.removeRoleFromMember(event.getMessage().getMentionedMembers().get(0), guild.getRolesByName("progen-muted", false).get(0)).complete();

        event.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.green).setTitle("Erfolgreich").setDescription("User " + event.getMessage().getMentionedMembers().get(0).getAsMention() + " erfolgreich entmutet").build()).queue();

    }

    @Override
    public String help() {
        return null;
    }

}
