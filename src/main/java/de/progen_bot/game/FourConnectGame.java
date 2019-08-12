package de.progen_bot.game;

import de.progen_bot.core.Main;
import de.progen_bot.db.GameData;
import de.progen_bot.db.MySQL;
import de.progen_bot.util.Util;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;

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
        final String[] numberReactions = {"1⃣", "2⃣", "3⃣", "4⃣", "5⃣", "6⃣", "7⃣", "8⃣"};

        for (int i = 0; i < count; i++) {
            message.addReaction(numberReactions[i]).queue();
        }
    }
}
