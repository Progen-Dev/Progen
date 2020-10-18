package de.progen_bot.commands.owner;

import java.awt.Color;
import java.time.Instant;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager.ParsedCommandString;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.permissions.AccessLevel;
import de.progen_bot.util.Statics;
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
            .setDescription("Hey @everyone, `1.0.8` is released.")
            .addField("Version", Statics.VERSION , false)
            .addField("Bug fixes \n",
             "• Command `Warndelete` now deletes warnings again.\n"+
             "• Progen and ProgenMusic now leave the voice channel when they leave.\n" +
             "• Timeout (1day) for Progen's minigame\n" +
             "• Progen status now works correctly again.",false)
            .addField("New Commands/Features\n",
            "`Mutelist`: List your total Guild mutes\n" +
            "`Startboard`: The best comments and pictures belong in the Starboard. Create the text channel Starboard and mark the coolest pictures or messages with a star. After 24 hours the message with the most messages will be placed in the Starboard.\n"+
            "`Delete Execute messages`: Progen immediately deletes messages you write to it. This is to keep order and the chat empty. It is also practical."
            ,false)
             .addField("Bug", "You have found a bug or have an idea? Please help us!\n"+ "[Bugs](https://github.com/Progen-Dev/Progen/issues/new?assignees=&labels=bug&template=bug_report.md&title=%5BBUG%5D+)"  +  "      [Idea](https://github.com/Progen-Dev/Progen/issues/new?assignees=&labels=enhancement&template=new_command.md&title=%5BNew+Command%5D+)" + "     [Join](https://discord.gg/rPeBPkr)", false)
             .setDescription("For more information click [here](https://github.com/Progen-Dev/Progen/blob/master/RELEASE.md)")
             .setTimestamp(Instant.now())
         .build()
        ).queue();
    }

    @Override
    public AccessLevel getAccessLevel(){
        return AccessLevel.BOT_OWNER;
    }

}