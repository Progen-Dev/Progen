package de.progen_bot.commands;

import de.progen_bot.core.Main;
import de.progen_bot.core.command.CommandHandler;
import de.progen_bot.core.command.CommandManager;
import de.progen_bot.database.entities.config.GuildConfiguration;
import de.progen_bot.utils.permission.AccessLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Help extends CommandHandler
{
    public Help()
    {
        super("help", "help <command>", "List all registered commands or lookup specific help and usage for a command.");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString commandString, GuildMessageReceivedEvent event, GuildConfiguration configuration)
    {
        final String[] args = commandString.getArgs();
        final EmbedBuilder eb = new EmbedBuilder();

        if (args.length == 0)
        {
            final Map<String, List<CommandHandler>> commandsByGroup = new HashMap<>();

            for (CommandHandler handler : Main.getCommandManager().getCommands().values())
            {
                final String[] packageSplit = handler.getClass().getPackageName().split("\\.");
                final String packageName = packageSplit[packageSplit.length - 1].toLowerCase();

                if (!commandsByGroup.containsKey(packageName))
                    commandsByGroup.put(packageName, new ArrayList<>());

                commandsByGroup.get(packageName).add(handler);
            }

            // FIXME: 22.02.2021
            eb.setAuthor("Progen", "https://progen-bot.de" /* or link to GitHub */, "INSERT")
                    .setTitle("Help")
                    .setDescription("For more information on commands use `" + configuration.getPrefix() + "help` <command>")
                    .setFooter("Discord Server: https://discord.gg/rPeBPkR | Current prefix: " + configuration.getPrefix());

            for (String group : commandsByGroup.keySet())
            {
                final StringBuilder sb = new StringBuilder();

                for (CommandHandler handler : commandsByGroup.get(group))
                {
                    sb.append('`').append(handler.getInvoke()).append('`').append('\n');
                }

                if ("commands".equalsIgnoreCase(group))
                    group = "Not grouped";

                eb.addField(group, sb.toString(), true);
            }

            event.getChannel().sendMessage(eb.build()).queue();
        }
        else
        {
            final CommandHandler handler = Main.getCommandManager().getCommandHandler(args[0].toLowerCase());

            if (handler == null)
            {
                eb.setColor(Color.red)
                        .setTitle("⚠ Invalid command")
                        .setDescription(
                                "There is no registered command maned `" + args[0] + "`. Use `" +
                                configuration.getPrefix() + "help` to get a full list of registered commands."
                        );
            }
            else
            {
                eb.setColor(Color.green)
                        .setTitle("Command Details")
                        .setDescription(handler.getDescription())
                        .addField("Usage", "`" + handler.getUsage() + "`", true)
                        .addField("Required permission level", String.valueOf(handler.getAccessLevel().getLevel()), true)
                        .addField("Your permission level", String.valueOf(AccessLevel.getAccessLevel(event.getMember()).getLevel()), true)
                        .setFooter("Permission level: higher is better");
            }

            event.getChannel().sendMessage(eb.build()).queue();
        }
    }

    @Override
    public AccessLevel getAccessLevel()
    {
        return AccessLevel.USER;
    }
}
