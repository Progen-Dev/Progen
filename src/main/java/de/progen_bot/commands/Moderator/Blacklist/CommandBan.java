package de.progen_bot.commands.Moderator.Blacklist;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.core.PermissionCore;
import de.progen_bot.db.entities.config.GuildConfiguration;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public class CommandBan extends CommandHandler {
    public CommandBan(){super("ban", "ban <user> <reason>", "Ban a user from this Server");}
    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand , MessageReceivedEvent event , GuildConfiguration configuration) {
        if (PermissionCore.check(2, event));

        ArrayList<String> ban = new ArrayList<>(event.getGuild());
        User victim;

        if (parsedCommand.getArgs().length > 0){
            if (event.getMessage().getMentionedUsers().size() > 0)
                victim = event.getMessage().getMentionedUsers().get(0);
            else {
                event.getTextChannel().sendMessage(
                        super.messageGenerators.generateErrorMsg("Please mention a valid user")
                ).queue();
                return;
            }
            if (ban.contains(victim.getId())){
                ban.remove(victim.getId());
                event.getTextChannel().sendMessage(
                        super.messageGenerators.generateSuccessfulMsg("Succesfully")
                )


            }
        }

    }

    @Override
    public String help() {
        return null;
    }
}
