package de.progen_bot.commands.user;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.util.Statics;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

public class CommandInfo extends CommandHandler {
    public CommandInfo() {
        super("info" , "info" , "all infos and links about Progen and Team");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand , MessageReceivedEvent event , GuildConfiguration configuration) {
        event.getTextChannel().sendMessage(

                new EmbedBuilder()

                        .setColor(Color.magenta)

                        .setThumbnail("https://github.com/ProgenBot/ProgenBot/blob/master/images/Progen.png")

                        .setDescription(":robot:   __**ProgenBot** JDA Discord Bot__")

                        .addField("Current Version" , Statics.Version , true)

                        .addField("Latest Version" , Statics.LastVersion , true)

                        .addField("Copyright" ,

                                "Coded by Progen Development Team.\n" +

                                        "© 2020 David Franzen" , false)

                        .addField("Information and Links" ,

                                "GitHub Repository: \n*https://github.progen-bot.de*\n\n" +

                                        "Readme: \n*https://github.com/ProgenBot/ProgenBot/blob/master/README.md*\n\n" +

                                        "Wiki : \n*https://github.com/ProgenBot/ProgenBot/wiki*\n\n" +

                                        "Webpage: \n*https://progen-bot.de*\n\n" +
                                        "Webinterface: \n*https://pwi.progen-bot.de*\n\n", false)

                        .addField("Libraries and Dependencies" ,

                                " -  JDA  *(https://github.com/DV8FromTheWorld/JDA)*\n" +

                                        " -  Toml4J  *(https://github.com/mwanji/toml4j)*\n" +

                                        " -  lavaplayer  *(https://github.com/sedmelluq/lavaplayer)*\n" +

                                        " -  Steam-Condenser  *(https://github.com/koraktor/steam-condenser-java)*" , false)

                        .addField("Bug Reporting / Idea Suggestion" ,

                                "If you got some bugs, please contact us here:\n" +

                                        " - **Please use this document to report a Bug or suggest an idea:  \n http://supp.progen-bot.de**\n\n" +

                                        " \n-  Discord:  https://discord.progen-bot.de" , false)
                        .setImage("https://top.gg/api/widget/495293590503817237.svg?usernamecolor=FFFFFF&topcolor=000000")
                        .build()
        ).queue();
    }

    @Override
    public String help() {
        return null;
    }
}
