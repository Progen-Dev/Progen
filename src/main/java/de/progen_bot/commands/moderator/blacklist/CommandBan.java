package de.progen_bot.commands.moderator.blacklist;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.permissions.AccessLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.time.Instant;
import java.util.List;

public class CommandBan extends CommandHandler
{
    private static final String BAN = "Ban";
    private static final String EXECUTOR = "Executor";
    private static final String VICTIM = "Victim";
    private static final String REASON = "Reason";

    public CommandBan()
    {
        super("ban", "ban <user> <reason>", "Ban a user from this Server");
    }

    private static MessageEmbed getBanEmbed(MessageReceivedEvent event, String reason)
    {
        return new EmbedBuilder()
                .setColor(Color.red)
                .setTitle(BAN)
                .addField(VICTIM, event.getMessage().getMentionedUsers().get(0).getAsMention(), true)
                .addField(EXECUTOR, event.getMember().getAsMention(), true)
                .setDescription(event.getMessageId())
                .addField(REASON, reason, false)
                .setTimestamp(Instant.now()).build();
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration)
    {
        Member selfMember = event.getMember();

        if (selfMember == null)
            return;

        final List<String> argsWithoutMention = parsedCommand.getArgsAsList().subList(1, parsedCommand.getArgsAsList().size());

        String reason;
        if (argsWithoutMention.isEmpty())
            reason = "No reason";
        else
            reason = String.join(" ", argsWithoutMention);

        final MessageEmbed eb = getBanEmbed(event, reason);
        if (eb == null)
            return;

        if (parsedCommand.getArgs().length > 0)
        {
            User user = event.getJDA().retrieveUserById(event.getMessage().getMentionedUsers().get(0).getId()).complete();

            if (user == null)
            {
                System.out.println("unknown user");
                return;
            }

            event.getGuild().ban(user, 7, reason).queue(v ->
            {
                user.openPrivateChannel().queue(
                        privateChannel -> privateChannel.sendMessage(eb).queue()
                );

                event.getChannel().sendMessage(eb).queue();
                final TextChannel channel = event.getGuild().getTextChannelsByName("progenlog", true).get(0);
                if (channel != null)
                    channel.sendMessage(eb).queue();
            }, throwable ->
                    event.getChannel().sendMessage("Failed to ban user " + user.getAsTag()).queue());
        }
    }

    @Override
    public AccessLevel getAccessLevel()
    {
        return AccessLevel.MODERATOR;
    }
}