package de.progen_bot.commands.moderator;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.db.entities.MuteData;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.permissions.AccessLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.Arrays;

public class Mute extends CommandHandler
{
    public Mute()
    {
        super("mute", "mute <user/id> [reason]", "Toggle mute state of mentioned user");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration)
    {
        if (parsedCommand.getArgs().length < 1)
        {
            event.getChannel().sendMessage(messageGenerators.generateErrorMsg("Please provide a mention or a user id to toggle the mute state of the specific user")).queue();
            return;
        }

        this.toggle(parsedCommand.getArgs(), event);
    }

    private void toggle(String[] args, MessageReceivedEvent event)
    {
        // TODO: 13.07.2020 ASYNC
        final User victim = event.getMessage().getMentionedUsers().isEmpty() ? event.getJDA().retrieveUserById(args[0]).complete() : event.getMessage().getMentionedUsers().get(0);
        if (victim == null)
        {
            event.getChannel().sendMessage(messageGenerators.generateErrorMsg("Please enter a valid mention or user ID!")).queue();
            return;
        }

        final String victimId = victim.getId();
        final MuteData data = MuteData.getMuteData(victimId);
        if (data == null)
        {
            String reason = "No reason.";
            if (args.length > 1)
                reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

            new MuteData(victimId, reason, event.getAuthor().getId(), event.getGuild().getId())
                    .save();

            event.getChannel().sendMessage(new EmbedBuilder().setColor(Color.orange).setDescription(String.format("%s muted %s.%n%nReason: `%s`", event.getAuthor().getAsMention(), victim.getAsMention(), reason)).build()).queue();
        }
        else
        {
            data.delete();

            event.getChannel().sendMessage(messageGenerators.generateRightMsg(String.format("%s unmuted %s.", event.getAuthor().getAsMention(), victim.getAsMention()))).queue();
        }
    }

    @Override
    public AccessLevel getAccessLevel()
    {
        return AccessLevel.MODERATOR;
    }
}
