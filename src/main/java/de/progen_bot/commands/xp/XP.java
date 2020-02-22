package de.progen_bot.commands.xp;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager.ParsedCommandString;
import de.progen_bot.db.entities.UserData;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.permissions.AccessLevel;
import de.progen_bot.util.Level;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

/**
 * The Class XP.
 */
public class XP extends CommandHandler {

    /**
     * Instantiates a new xp.
     */
    public XP() {
        super("xp", "xp <username>", "Get the xp");
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.progen_bot.command.CommandHandler#execute(de.progen_bot.command.CommandManager.ParsedCommandString,
     * net.dv8tion.jda.de.progen_bot.core.events.message.MessageReceivedEvent)
     */
    @Override
    public void execute(ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {
        String id = "";
        if (parsedCommand.getArgs().length == 0) {
            id = event.getAuthor().getId();
        } else if (event.getMessage().getMentionedMembers().size() > 0) {
            id = event.getMessage().getMentionedMembers().get(0).getUser().getId();
        } else if (event.getGuild().getMembersByEffectiveName(parsedCommand.getArgs()[0], true).size() > 0) {
            id = event.getGuild().getMembersByEffectiveName(parsedCommand.getArgs()[0], true).get(0).getUser().getId();
        } else {
            return;
        }

        UserData data = UserData.fromId(id);

        EmbedBuilder eb = new EmbedBuilder().setColor(Color.green).setFooter(
                event.getGuild().getMemberById(id).getUser().getName() + "#"
                        + event.getGuild().getMemberById(id).getUser().getDiscriminator(),
                event.getGuild().getMemberById(id).getUser().getAvatarUrl());

        if (data != null) {
            double percent = 100 - Double.valueOf(Level.remainingXp(data.getTotalXp()))
                    / Double.valueOf(Level.xpToLevelUp(data.getLevel())) * 100;

            eb.setTitle("Level: " + data.getLevel() + " (" + Level.remainingXp(data.getTotalXp()) + "/"
                    + Level.xpToLevelUp(data.getLevel()) + ")" + "XP")
                    .setDescription(percent + "% to the next Level.");
        } else {
            eb.setTitle("Level: 0 (0/0)XP").setDescription("You have not sent a message yet");
        }
        event.getTextChannel().sendMessage(eb.build()).queue();

    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.USER;
    }

}
