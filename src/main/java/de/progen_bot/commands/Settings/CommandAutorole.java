package de.progen_bot.commands.Settings;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.permissions.AccessLevel;
import de.progen_bot.permissions.PermissionCore;
import de.progen_bot.db.entities.config.GuildConfiguration;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class CommandAutorole extends CommandHandler{
    public CommandAutorole(String invokeString, String commandUsage, String description) {
        super("autorole", "autorole <@role>", "Set a roll that the user should get automatically when joining");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {

        if (parsedCommand.getArgs().length < 1){
            event.getTextChannel().sendMessage(
                    messageGenerators.generateErrorMsg("Please Read my Command Documentation with hel and commandname")
            ).queue();
            return;
        }

        StringBuilder sb = new StringBuilder();
        Arrays.stream(parsedCommand.getArgs()).forEach(s -> sb.append(s + "  "));
        List<Role> autoRole = event.getGuild().getRolesByName(sb.toString().substring(0, sb.length() - 1), true);

        if (event.getMessage().getMentionedRoles().size() > 0){
            configuration.setAutorole(event.getMessage().getMentionedRoles().get(0).getName(), event.getGuild());
            event.getTextChannel().sendMessage(messageGenerators.generateInfoMsg("Successfully set autorole to `" + event.getMessage().getMentionedRoles().get(0).getName() + "`.")).queue();
        }else if (autoRole.size() > 0){
            configuration.setAutorole(autoRole.get(0).getName(), event.getGuild());
            event.getTextChannel().sendMessage(messageGenerators.generateInfoMsg("Successfully set autorole to `" + autoRole.get(0).getName() + "`.")).queue();
        }else {
            configuration.setAutorole("", event.getGuild());
            event.getTextChannel().sendMessage(messageGenerators.generateInfoMsg("Successfully deactivated autorole.")).queue();
        }
    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.MODERATOR;
    }

}
