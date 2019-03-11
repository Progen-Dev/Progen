package game;

import core.Main;
import db.GameData;
import db.MySQL;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import util.Util;

public class FourConnectGame {

	public static void createGame(GameData gameData, int height, int width, JDA jda) {
		ConnectFourModel conFourMod = new ConnectFourModel(width, height, gameData.getChallengerId(),
				gameData.getOpponentId());
//TODO maybe random player
		String field = generateBoardString(conFourMod.getBoard(), conFourMod.getPlayer1());

		jda.getTextChannelById(gameData.getChannelId()).sendMessage(field).queue(msg -> {
			addReaction(msg, gameData.getWidth());
			conFourMod.setMsgId(msg.getId());
			conFourMod.setActPlayer(conFourMod.getPlayer1());
			MySQL.insertConnectFourData(conFourMod);
		});
	}

	private static String generateBoardString(char[][] board, String userId) {
		String field = Main.getJda().getUserById(userId).getAsMention() + " :red_circle: fängt an\n";

		for (int j = 0; j < board.length; j++) {
			for (int i = 0; i < board[0].length; i++) {
				field += ":white_circle:";
			}
			field += "\n";
		}
		return Util.addTableNumbers(field, board[0].length);
	}

	private static void addReaction(Message message, int count) {
		final String[] numberReactions = { "1⃣", "2⃣", "3⃣", "4⃣", "5⃣", "6⃣", "7⃣", "8⃣" };

		for (int i = 0; i < count; i++) {
			message.addReaction(numberReactions[i]).queue();
		}
	}
}
