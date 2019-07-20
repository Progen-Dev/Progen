package de.progen_bot.commands.Fun;

import de.mtorials.config.GuildConfiguration;
import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager.ParsedCommandString;
import de.progen_bot.core.Main;
import de.progen_bot.db.GameData;
import de.progen_bot.db.MySQL;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import de.progen_bot.util.Settings;

public class ConnectFour extends CommandHandler {
	public ConnectFour() {
		super("cf", "cf", "cf");
	}

	@Override
	public void execute(ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {
		JDA jda = Main.getJda();
		String[] args = parsedCommand.getArgs();

		if (args.length < 3) {

			event.getTextChannel().sendMessage(error.setDescription("Bitte benutze den Befehl so: " + Settings.PREFIX
					+ "cf <Breite(7-8)> <Höhe(Um 1 kleiner als die Breite)> <Gegenspieler>").build()).queue();
			return;
		}

		int width = Integer.parseInt(args[0]);
		int heigh = Integer.parseInt(args[1]);

		User challenger = event.getAuthor();
		String opponent = args[2];
		
		if (event.getGuild().getMembersByName(opponent, true).size() == 0) {
			event.getTextChannel()
					.sendMessage(
							error.setDescription("Der User " + opponent + " konnte nicht gefunden werden!").build())
					.queue();
			return;
		}
		if (event.getGuild().getMembersByName(opponent, true).get(0).getOnlineStatus() != OnlineStatus.ONLINE) {
			event.getTextChannel()
					.sendMessage(error.setDescription("Der User " + opponent + " ist nicht online!").build()).queue();
			return;
		}

		if (width > 6 && width < 9) {
			if (heigh == width - 1) {
				if (!opponent.toLowerCase().equals(challenger.getName().toLowerCase())) {
					GameData gameData = new GameData();
					gameData.setOpponentId(event.getGuild().getMembersByName(opponent, true).get(0).getUser().getId());
					gameData.setChallengerId(challenger.getId());
					gameData.setHeigh(heigh);
					gameData.setWidth(width);
					gameData.setChannel(event.getChannel().getId());

					jda.getUsersByName(opponent, true).get(0).openPrivateChannel().queue(privateChannel -> {
						privateChannel.sendMessage(challenger.getName() + " hat dich zu einer Runde Vier-Gewinnt("
								+ heigh + "x" + width + ") herausgefordert!").queue(msg -> {
									msg.addReaction("✅").queue();
									msg.addReaction("❌").queue();
									gameData.setMessageId(msg.getId());
									MySQL.insertGameData(gameData);
								});
					});
				} else {
					event.getTextChannel()
							.sendMessage(error.setDescription("Du kannst dich nicht selbst herausfordern!").build())
							.queue();
					return;
				}
			} else {
				event.getTextChannel()
						.sendMessage(error.setDescription("Die Höhe muss um 1 kleiner sein als die Breite!").build())
						.queue();
				return;
			}
		} else {
			event.getTextChannel().sendMessage(error.setDescription("Die Breite muss zwischen 7 und 8 liegen!").build())
					.queue();
			return;
		}
	}

	@Override
	public String help() {
		return null;
	}

}
