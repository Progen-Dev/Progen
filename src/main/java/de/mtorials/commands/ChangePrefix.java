package de.mtorials.commands;

import de.mtorials.config.GuildConfiguration;
import de.mtorials.config.GuildConfigurationBuilder;
import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.core.Main;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ChangePrefix extends CommandHandler {

    public ChangePrefix() {
        super("changeprefix", "changeprefix <new prefix>", "Changes the bots prefix");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {

        if (!event.getMember().isOwner()) {

            event.getTextChannel().sendMessage("You have to be the owner of that guild to change the prefix!").queue();
            return;
        }

        if (parsedCommand.getArgsAsList().size() != 1) {

            event.getTextChannel().sendMessage("See help command for usage. Wrong argument count.").queue();
            return;
        }

        GuildConfiguration oldConfiguration = Main.getConfiguration().getGuildConfiguration(event.getGuild());

        GuildConfiguration newConfiguration = new GuildConfigurationBuilder()
                .setGuildConfig(oldConfiguration)
                .setPrefix(parsedCommand.getArgsAsList().get(0))
                .build();

        Main.getConfiguration().writeGuildConfiguration(event.getGuild(), newConfiguration);

        event.getTextChannel().sendMessage("Prefix has changed to " + newConfiguration.getPrefix()).queue();
    }

    @Override
    public String help() {
        return null;
    }
}
