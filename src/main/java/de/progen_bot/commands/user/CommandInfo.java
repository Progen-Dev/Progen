package de.progen_bot.commands.user;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.permissions.AccessLevel;
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
                 .setColor(Color.BLUE)
                 .setTitle("Info")
                 .setDescription("Multi purpose discord bot with webinterface, music, administration, tools, games and more!")
                 .addField("Github","[Github.com/Progen-Dev](https://github.com/Progen-Dev)", false)
                 .addField("Website & Web interface", "[progen-bot.de](https://progen-bot.de) [ProgenWebinterface PWI](https://pwi.progen-bot.de)", false)
                 .addField("Invite", "[Progen](https://discord.com/oauth2/authorize?client_id=495293590503817237&scope=bot&permissions=8) [ProgenMusic1](https://discord.com/oauth2/authorize?client_id=662647209929605126&scope=bot&permissions=3145728) [ProgenMusic2](https://discord.com/oauth2/authorize?client_id=662647378385305620&scope=bot&permissions=3145728)", false)
                 .addField("Bugs and Idea", "[Bugs](https://github.com/Progen-Dev/Progen/issues/new?assignees=&labels=bug&template=bug_report.md&title=%5BBUG%5D+)    [Idea](https://github.com/Progen-Dev/Progen/issues/new?assignees=&labels=enhancement&template=new_command.md&title=%5BNew+Command%5D+)", false)
                 .addField("Discord", "[Join](https://discord.gg/rPeBPkr)", false)
                 .setFooter("Coded by Progen-Dev Team | © 2020 - now David Franzen")
         .build()
       ).queue();
}

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.USER;
    }
}
