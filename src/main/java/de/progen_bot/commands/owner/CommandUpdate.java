package de.progen_bot.commands.owner;

import java.awt.Color;
import java.time.Instant;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager.ParsedCommandString;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.permissions.AccessLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandUpdate extends CommandHandler {

    public CommandUpdate() {
        super("update", "update", "a simple embed to display the latest updates of Progen.");
    }
    @Override
    public void execute(ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {
        event.getTextChannel().sendMessage(
            new EmbedBuilder()
            .setColor(Color.blue)
            .setTitle("Update")
            .setDescription("Hey guys, `1.0.8` is released. During the last four months we could fix many bugs, extend commands and finish a long awaited command.")
            .addField("Version", "1.0.7", false)
            .addField("Troubleshooting",
             "• Command Ban and Kick can now be executed without the `progenlog`.\n"+
             "• If you are muted on one guild you are no longer muted on all guilds. \n"+
             "• The Server Count display no longer updates only after each restart.\n"+ 
             "• The strange reason when a person has been warned is adapted.\n"+
             "• The music command works again without problems. ", false)
            .addField("New Command", 
             "In the command collection of Progen we can now also call command `autorole` Welcome. With this command, new members can automatically get the role `user`.\n"+
             "Active: `pb!autorole @role | Disable: pb!autorle disable`", false)
             .addField("Command changes", 
             "• Mute\n"+
             "• Ban\n"+
             "• Kick\n"+
             "• Info \n"+
             "• Warn \n"+
             "• Music" ,false)
             .addField("Bug", "You have found a bug or have an idea? Please help us!\n"+ "[Bugs](https://github.com/Progen-Dev/Progen/issues/new?assignees=&labels=bug&template=bug_report.md&title=%5BBUG%5D+)"  +  "      [Idea](https://github.com/Progen-Dev/Progen/issues/new?assignees=&labels=enhancement&template=new_command.md&title=%5BNew+Command%5D+)" + "     [Join](https://discord.gg/rPeBPkr)", false)
            .setTimestamp(Instant.now())
         .build()
        ).queue();
    }

    @Override
    public AccessLevel getAccessLevel(){
        return AccessLevel.BOT_OWNER;
    }

}