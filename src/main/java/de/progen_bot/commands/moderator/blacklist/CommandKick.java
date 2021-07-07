package de.progen_bot.commands.moderator.blacklist;

import java.awt.Color;
import java.time.Instant;
import java.util.List;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.core.Main;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.permissions.AccessLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

public class CommandKick extends CommandHandler {
    private static final String KICK = "Kick";
    private static final String EXECUTOR = "Executor";
    private static final String VICTIM = "Victim";
    private static final String REASON = "Reason";
    private static final String CHANNEL = "Channel";

    public CommandKick() {
        super("kick", "kick <@user> <reason>", "Kick a user from this server");
    }

    private MessageEmbed getKickEmbed(MessageReceivedEvent event, String reason) {
        if (event.getMember() == null)
            return null;

        return new EmbedBuilder().setColor(Color.MAGENTA).setTitle(KICK)
                .addField(VICTIM, event.getMessage().getMentionedMembers().get(0).getAsMention(), true)
                .addField(EXECUTOR, event.getMember().getAsMention(), true).setDescription(event.getMessageId())
                .addField(CHANNEL, event.getTextChannel().getAsMention(), false)
                .addField(REASON, reason, false).setTimestamp(Instant.now()).build();
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event,
                        GuildConfiguration configuration) {
        final List<String> argsWithoutMention = parsedCommand.getArgsAsList().subList(1,
                parsedCommand.getArgsAsList().size());

        String reason;
        if (argsWithoutMention.isEmpty())
            reason = "No reason";
        else
            reason = String.join(" ", argsWithoutMention);

        final MessageEmbed eb = this.getKickEmbed(event, reason);
        if (eb == null)
            return;

        event.getTextChannel().sendMessage(eb).queue();
        Main.getJda().getTextChannelById(configuration.getLogChannelID()).sendMessage(eb).queue();

        final Member target = event.getMessage().getMentionedMembers().get(0);
        try {
            event.getGuild().kick(target).queue(v -> {
                event.getMessage().getMentionedUsers().get(0).openPrivateChannel()
                        .queue(privateChannel -> privateChannel.sendMessage(eb).queue());
            });
        } catch (InsufficientPermissionException e) {
            event.getChannel().sendMessage("Failed to kick user " + target.getUser().getAsTag()).queue();
        }
    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.MODERATOR;
    }
}