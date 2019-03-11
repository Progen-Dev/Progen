package listeners;

import java.awt.Color;
import java.util.concurrent.TimeUnit;

import core.Main;
import db.GameData;
import db.MySQL;
import game.ConnectFourModel;
import game.FourConnectGame;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import util.Util;

public class FourConnectListener extends ListenerAdapter {
	private static JDA jda;

	@Override
	public void onPrivateMessageReactionAdd(PrivateMessageReactionAddEvent event) {
		jda = Main.getJda();

		// check if reactionMessage is an invite
		if (MySQL.getGameData(event.getMessageId()) != null) {
			if (!event.getUser().isBot()) {
				GameData gameData = MySQL.getGameData(event.getMessageId());
				String emoteName = event.getReactionEmote().getName();

				jda.getPrivateChannelById(event.getChannel().getId())
						.deleteMessageById(event.getReaction().getMessageId()).queue();

				switch (emoteName) {

				case "✅":

					event.getChannel()
							.sendMessage(new EmbedBuilder().setColor(Color.DARK_GRAY)
									.setDescription("Spiel wird vorbereitet...").build())
							.queue(msg -> msg.delete().queueAfter(9, TimeUnit.SECONDS));

					startGame(gameData);
					break;

				case "❌":

					event.getChannel().sendMessage(new EmbedBuilder().setColor(Color.HSBtoRGB(85, 1, 100))
							.setDescription("Spielanfrage abgelehnt!").build()).queue();

					closeGame(gameData);
					break;
				}
			}
		}
	}

	private static void startGame(GameData gameData) {
		jda.getUserById(gameData.getChallengerId()).openPrivateChannel().queue(privateChannel -> {
			privateChannel.sendMessage(jda.getUserById(gameData.getOpponentId()).getName()
					+ " hat deine Spielanfrage angenommen und eine Spielinstanz wird erstellt!").queue(msg -> {

						int heigh = gameData.getHeigh();
						int width = gameData.getWidth();

						FourConnectGame.createGame(gameData, heigh, width, jda);
						msg.delete().queueAfter(10, TimeUnit.SECONDS);
					});
		});
	}

	private static void closeGame(GameData gameData) {
		jda.getUserById(gameData.getChallengerId()).openPrivateChannel().queue(privateChannel -> {
			privateChannel
					.sendMessage(
							jda.getUserById(gameData.getOpponentId()).getName() + " hat deine Spielanfrage abgelehnt!")
					.queue();
		});
	}

	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
		if (event.getUser().isBot()) {
			return;
		}

		ConnectFourModel gameData = MySQL.getConnectFourData(event.getMessageId());

		if (gameData != null) {
			if (gameData.isGameOver()) {
				return;
			}

			if (gameData.nextPlayerMove(event.getUser().getId())) {
				int emote = Util.getIntegerFromReaction(event.getReactionEmote().getName());
				if (emote == 0) {
					return;
				}

				gameData.setField(emote);

				event.getJDA().getTextChannelById(event.getChannel().getId()).getMessageById(event.getMessageId())
						.queue(msgid -> gameData.updateBoard(msgid));

				if (gameData.isGameOver()) {
					String text = event.getUser().getAsMention() + " hat gewonnen!";
					event.getChannel().sendMessage(text).queue();
					return;
				}
			}
			event.getReaction().removeReaction(event.getUser()).queue();
		}
	}
}
