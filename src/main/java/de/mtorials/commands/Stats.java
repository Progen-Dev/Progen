package de.mtorials.commands;

import de.mtorials.fortnite.core.Statistics;
import de.mtorials.fortnite.exeptions.NotEnoughtDetailsException;
import de.mtorials.fortnite.exeptions.UserNotFoundExeption;
import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.core.Main;
import de.progen_bot.db.entities.config.GuildConfiguration;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.io.IOException;

public class Stats extends CommandHandler {


    public Stats() {
        super("stats", "stats <fortnite player name>", "A simple command to get stats and the player data of fortnite players.");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {

        try {

            de.mtorials.fortnite.core.User fortniteUser = Main.getFortnite().getUserByName(parsedCommand.getArgsAsList().get(0));
            Statistics stats = fortniteUser.getStatistics();

            MessageEmbed msg = new EmbedBuilder()
                    .setTitle("Statistics - " + fortniteUser.getUsername())
                    .setDescription("These are the statistics of the user " + fortniteUser.getUsername() + ".")
                    .addField("Overall Score", String.valueOf(stats.getOverallScore()), false)
                    .addField("Overall Kills", String.valueOf(stats.getOverallKills()), false)
                    .addField("Overall First Place", String.valueOf(stats.getOverallPlaceTop1()), false)
                    .addField("Overall Matches Played", String.valueOf(stats.getOverallMatchesPlayed()), false)
                    .setColor(Color.ORANGE)
                    .build();

            event.getTextChannel().sendMessage(msg).queue();

        } catch (UserNotFoundExeption e) {

            e.printStackTrace();
            event.getTextChannel().sendMessage("Sorry, but we can't find this user.").queue();

        } catch (IOException e) {

            e.printStackTrace();
            event.getTextChannel().sendMessage("Sorry, but we failed reaching the api.").queue();

        } catch (IndexOutOfBoundsException e) {

            event.getTextChannel().sendMessage("Sorry, but you have to specify the user you want to get the statistics of.").queue();

        } catch (NotEnoughtDetailsException e) {

            event.getTextChannel().sendMessage("Sorry, the we have not enough information about the user to create a statistic.").queue();
        }
    }

    @Override
    public String help() {
        return null;
    }
}
