package de.progen_bot.commands.Fun;

import de.mtorials.config.GuildConfiguration;
import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager.ParsedCommandString;
import de.progen_bot.core.Main;
import de.progen_bot.db.GameData;
import de.progen_bot.db.MySQL;
import net.dv8tion.jda.core.EmbedBuilder;
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

			event.getTextChannel().sendMessage(new EmbedBuilder(generateErrorMsg("Please Use:" + Settings.PREFIX +
					"cf <Width(7-8)> <Height(1 smaller than the width)> <Opponent>")).build()).queue();
		}

		int width = Integer.parseInt(args[0]);
		int heigh = Integer.parseInt(args[1]);

		User challenger = event.getAuthor();
		String opponent = args[2];
		
		if (event.getGuild().getMembersByName(opponent, true).size() == 0) {
			event.getTextChannel()
					.sendMessage(
							new EmbedBuilder(generateErrorMsg("The user " + opponent + " could be found!")).build())
					.queue();
			return;
		}
		if (event.getGuild().getMembersByName(opponent, true).get(0).getOnlineStatus() != OnlineStatus.ONLINE) {
			event.getTextChannel()
					.sendMessage(new EmbedBuilder(generateErrorMsg("The User " + opponent + " is not online")).build()).queue();
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
							.sendMessage(new EmbedBuilder(generateErrorMsg("You can't challenge yourself!")).build())
							.queue();
					return;
				}
			} else {
				event.getTextChannel()
						.sendMessage(new EmbedBuilder(generateWarningMsg("The height must be 1 less than the width!")).build())
						.queue();
				return;
			}
		} else {
			event.getTextChannel().sendMessage(new EmbedBuilder(generateErrorMsg("The width must be between 7 and 8!")).build())
					.queue();
			return;
		}
	}

	@Override
	public String help() {
		return null;
	}

}
