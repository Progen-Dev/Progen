package de.progen_bot.commands;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.core.Main;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.permissions.AccessLevel;
import de.progen_bot.util.Settings;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Help extends CommandHandler {
    public Help() {
        super("help", "help", "get some help");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {
        EmbedBuilder builder = new EmbedBuilder();

        if (parsedCommand.getArgs().length == 0) {

            HashMap<String, ArrayList<CommandHandler>> commandsByGroup = new HashMap<>();
            for (CommandHandler commandHandler : Main.getCommandManager().getCommandAssociations().values()) {

                String[] packageSplit = commandHandler.getClass().getPackageName().split("\\.");
                String packageName = packageSplit[ packageSplit.length - 1 ];

                if (!commandsByGroup.containsKey(packageName)) commandsByGroup.put(packageName, new ArrayList<>());
                commandsByGroup.get(packageName).add(commandHandler);
            }

            EmbedBuilder msg = new EmbedBuilder()
                    .setAuthor("Progen")
                    .setTitle("Help")
                    .setDescription("For more information about commands use " + configuration.prefix + "help <command>\n")
                    .setFooter("Discord Server: https://discord.gg/Xbtn9zG\nYour current Prefix: " + configuration.prefix);

            for (String group : commandsByGroup.keySet()) {
                StringBuilder s = new StringBuilder();
                for (CommandHandler commandHandler1 : commandsByGroup.get(group)) {

                    s.append('`').append(commandHandler1.getInvokeString()).append('`').append('\n');
                }
                msg.addField(group + "\n", s.toString(), true);
            }

            event.getTextChannel().sendMessage(msg.build()).queue();

        } else {
            CommandHandler handler = Main.getCommandManager().getCommandHandler(parsedCommand.getArgs()[0]);

            if (handler == null) {
                builder.setColor(Color.red)
                        .setTitle(":warning: Invalid command")
                        .setDescription("There is no command named `" + parsedCommand.getArgs()[0] + "`. Use `"
                        + Settings.PREFIX + parsedCommand.getCommand() + "` to get a full command list.");
            } else {
                builder.setColor(Color.green)
                        .setTitle("Command Infos")
                        .setDescription(handler.getDescription())
                        .addField("Commands: ", '`' + handler.getCommandUsage() + '`', true);
            }
            event.getChannel().sendMessage(builder.build()).queue();
        }
    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.USER;
    }

}
