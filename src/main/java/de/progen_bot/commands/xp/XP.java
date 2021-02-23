package de.progen_bot.commands.xp;

import de.progen_bot.core.command.CommandHandler;
import de.progen_bot.core.command.CommandManager;
import de.progen_bot.database.entities.config.GuildConfiguration;
import de.progen_bot.database.entities.xp.UserData;
import de.progen_bot.utils.level.Level;
import de.progen_bot.utils.permission.AccessLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

public class XP extends CommandHandler
{
    public XP()
    {
        super("xp", "xp [<@user, username or user id>]", "Get the xp of the specified user. If none, it will display yours.");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString commandString, GuildMessageReceivedEvent event, GuildConfiguration configuration)
    {
        final String id;

        if (commandString.getArgs().length == 0 || event.getMessage().getMentionedMembers().isEmpty())
            id = event.getAuthor().getId();
        else if (!event.getMessage().getMentionedMembers().isEmpty())
            id = event.getMessage().getMentionedMembers().get(0).getId();
        else if (!event.getGuild().getMembersByEffectiveName(commandString.getArgs()[0], true).isEmpty())
            id = event.getGuild().getMembersByEffectiveName(commandString.getArgs()[0], true).get(0).getId();
        else
        {
            event.getChannel().sendMessage(this.getGenerator().generateErrorMsg("Failed to parse input. See command usage.")).queue();
            return;
        }

        final UserData data = UserData.fromId(id);
        final Member member = event.getGuild().getMemberById(id);

        if (data == null || member == null)
            return;

        final EmbedBuilder eb = new EmbedBuilder().setColor(Color.green).setFooter(member.getUser().getAsTag(), member.getUser().getEffectiveAvatarUrl());

        final long level = Math.round(100 - (double) Level.remainingXp(data.getTotalXp()) / (double) Level.xpToLevelUp(data.getLevel()) * 100);

        eb.setTitle(String.format("Level: %s (%s/%s) XP", data.getLevel(), Level.remainingXp(data.getTotalXp()), Level.xpToLevelUp(data.getLevel())))
                .setDescription(level + "% to the next level");

        event.getChannel().sendMessage(eb.build()).queue();
    }

    @Override
    public AccessLevel getAccessLevel()
    {
        return AccessLevel.USER;
    }
}
