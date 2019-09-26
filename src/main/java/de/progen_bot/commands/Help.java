package de.progen_bot.commands;

import de.mtorials.config.GuildConfiguration;
import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager.ParsedCommandString;
import de.progen_bot.core.Main;
import de.progen_bot.util.Settings;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.Collection;

public class Help extends CommandHandler {

    public Help() {
        super("help" , "help" , "get some help");
        // TODO Auto-generated constructor stub

    }

    @Override
    public void execute(ParsedCommandString parsedCommand , MessageReceivedEvent event , GuildConfiguration configuration) {
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
                                        "`warnlist`\n", true )
                        .addField("User",
                                "`tempchannel`\n"+
                                "`info`\n"+
                                "`register`\n"+
                                "`userinfo`\n"+
                                "`guildinfo`\n"+
                                "`ping`\n"+
                                "`say`\n"+
                                "`vote`\n", true)
                        .addField("etc",
                                "`help`", true)
                        .addField("music",
                                "`music`", true)
                        .addField("xp",
                        "`xp`\n"+
                                "`xpnotify`\n"+
                                "`xprank`\n", true)
                        .addField("fun",
                                "`cf`\n", true)
                        .setFooter("Discord Server: https://discord.gg/27zmyKu")


                .build()
        ).queue();

    }

    @Override
    public String help() {
        return null;
    }
}