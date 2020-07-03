package de.progen_bot.commands.user;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.permissions.AccessLevel;
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

                        .setDescription(":robot:   __**ProgenBot** JDA Discord Bot__")

                        .addField("Current Version" , Statics.VERSION , true)

                        .addField("Latest Version" , Statics.LAST_VERSION , true)

                        .addField("Copyright" ,

                                "Coded by Progen Development Team.\n" +

                                        "© 2020 David Franzen" , false)

                        .addField("Information and Links" ,

                                "GitHub Repositorys: \n*https://github.com/Progen-Dev*\n\n" +

                                        "Readme: \n*https://github.com/Progen-Dev/Progen/blob/master/README.md*\n\n" +

                                        "License: \n*https://github.com/Progen-Dev/Progen/blob/master/LICENSE*\n\n" +

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

                                        " - **Please use this document to report a Bug or suggest an idea:  \n https://github.com/Progen-Dev/Progen/issues**\n\n" +

                                        " \n-  Discord:  https://discord.gg/rPeBPkr \n" +
                                        "\n- your current Prefix: " + configuration.getPrefix(), false)
                        .build()
        ).queue();
    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.USER;
    }
}
