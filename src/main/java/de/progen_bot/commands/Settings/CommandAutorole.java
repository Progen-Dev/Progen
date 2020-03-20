package de.progen_bot.commands.Settings;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.core.PermissionCore;
import de.progen_bot.db.entities.config.GuildConfiguration;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class CommandAutorole extends CommandHandler{
    public CommandAutorole() {
        super("autorole", "autorole <@role>", "Set a roll that the user should get automatically when joining");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {
        if (PermissionCore.check(2, event));

        if (parsedCommand.getArgs().length < 1){
            event.getTextChannel().sendMessage(messageGenerators.generateErrorMsgWrongInput()).queue();
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        Arrays.stream(parsedCommand.getArgs()).forEach(s -> stringBuilder.append(s + "  "));
        List<Role> autoRole = event.getGuild().getRolesByName(stringBuilder.toString().substring(0, stringBuilder.length() -1), true);

        if (event.getMessage().getMentionedRoles().size() > 0){
            configuration.setAutorole(event.getMessage().getMentionedRoles().get(0).getName(), event.getGuild());
            event.getTextChannel().sendMessage(
                    messageGenerators.generateRightMsg("Successfully set autorole to `" + event.getMessage().getMentionedRoles().get(0).getName() +  "`.")
            ).queue();
        }else if (autoRole.size() > 0){
            configuration.setAutorole(autoRole.get(0).getName(), event.getGuild());
            event.getTextChannel().sendMessage(
                    messageGenerators.generateRightMsg("Successfully set autorole to `" + autoRole.get(0).getName() + "`.")
            ).queue();
        }else {
            configuration.setAutorole("", event.getGuild());
            event.getTextChannel().sendMessage(messageGenerators.generateRightMsg("Successfully deactivated autorole.")).queue();
        }
    }

    @Override
    public String help() {
        return null;
    }
}
