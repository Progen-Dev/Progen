package de.progen_bot.commands.moderator.Blacklist;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.core.PermissionCore;
import de.progen_bot.db.entities.config.GuildConfiguration;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.time.Instant;
import java.util.List;

public class CommandKick extends CommandHandler {

    private static final String KICK = "Kick";
    private static final String EXECUTOR = "Executor";
    private static final String VICTIM = "Victim";
    private static final String REASON = "Reason";

    public CommandKick() {
        super("kick" , "kick <@user> <reason>" , "Kick a user from this server");
    }


    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand , MessageReceivedEvent event , GuildConfiguration configuration) {
        if (PermissionCore.check(2 , event)) return;

        final List<String> argsWithoutMention = parsedCommand.getArgsAsList().subList(1, parsedCommand.getArgsAsList().size());

        String reason;
        if (argsWithoutMention.isEmpty())
            reason = "No reason";
        else
            reason = String.join(" ", argsWithoutMention);

        event.getTextChannel().sendMessage(this.getKickEmbed(event, reason)).queue();
        event.getGuild().getTextChannelsByName("progenlog",true).get(0).sendMessage(this.getKickEmbed(event, reason)).queue();

        final String finalReason = reason;
        event.getMessage().getMentionedUsers().get(0).openPrivateChannel().queue(
                privateChannel -> privateChannel.sendMessage(this.getKickEmbed(event, finalReason)).queue()
        );
        final Member target = event.getMessage().getMentionedMembers().get(0);
        event.getGuild().kick(target).queue();

    }

    private MessageEmbed getKickEmbed(MessageReceivedEvent event, String reason) {
        return new EmbedBuilder()
                .setColor(Color.MAGENTA)
                .setTitle(KICK)
                .addField(VICTIM, event.getMessage().getMentionedMembers().get(0).getAsMention(), true)
                .addField(EXECUTOR, event.getMember().getAsMention(),true)
                .setDescription(event.getMessageId())
                .addField(REASON, reason, false)
                .setTimestamp(Instant.now()).build();
    }

    @Override
    public String help() {
        return null;

    }
}