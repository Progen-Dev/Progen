package de.progen_bot.commands;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.core.Main;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.util.Settings;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

public class Help extends CommandHandler {
    public Help() {
        super("help", "help", "get some help");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {
        EmbedBuilder builder = new EmbedBuilder();

        if (parsedCommand.getArgs().length == 0) {
            event.getTextChannel().sendMessage(
                    new EmbedBuilder()
                            .setColor(Color.cyan)
                            .setAuthor("Progen Development Team")
                            .setTitle("Commandlist")
                            .setDescription("For more information about commands use <prefix>help <command>\n")
                            .addField("Owner",
                                    "`Stop` ", true)
                            .addField("Moderator\n",
                                    "`clear`\n" +
                                            "`kick`\n" +
                                            "`pc`\n" +
                                            "`unmute`\n" +
                                            "`warn`\n" +
                                            "`warndelete`\n" +
                                            "`warnlist`\n", true)
                            .addField("User",
                                    "`tempchannel`\n" +
                                            "`info`\n" +
                                            "`register`\n" +
                                            "`userinfo`\n" +
                                            "`guildinfo`\n" +
                                            "`ping`\n" +
                                            "`say`\n" +
                                            "`vote`\n", true)
                            .addField("etc",
                                    "`help`", true)
                            .addField("music",
                                    "`music`", true)
                            .addField("xp",
                                    "`xp`\n" +
                                            "`xpnotify`\n" +
                                            "`xprank`\n", true)
                            .addField("fun",
                                    "`cf`\n", true)
                            .setFooter("Discord Server: https://discord.gg/27zmyKu")
                            .build()
            ).queue();
        } else {
            CommandHandler handler = Main.getCommandManager().getCommandHandler(parsedCommand.getArgs()[0]);

            if (handler == null) {
                builder.setColor(Color.red);

                builder.setTitle(":warning: Invalid command");

                builder.setDescription("There is no command named `" + parsedCommand.getArgs()[0] + "`. Use `"

                        + Settings.PREFIX + parsedCommand.getCommand() + "` to get a full command list.");
            } else {
                builder.setColor(Color.green);

                builder.setTitle("Command Infos");

                builder.setDescription(handler.getDescription());

                builder.addField("Commands:", "`" + handler.getCommandUsage() + "`", true);
            }
            event.getChannel().sendMessage(builder.build()).queue();
        }
    }

    @Override
    public String help() {
        return null;
    }
}
