package de.progen_bot.misc;

import de.progen_bot.core.Main;
import de.progen_bot.database.dao.connectfour.ConnectFourDaoImpl;
import de.progen_bot.database.entities.connectfour.ConnectFourModel;
import de.progen_bot.database.entities.connectfour.GameData;
import de.progen_bot.utils.misc.Util;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class FourConnectGame
{

    private FourConnectGame()
    {
        /* Prevent instantiation */
    }

    public static void createGame(GameData gameData, int height, int width, JDA jda)
    {
        ConnectFourModel conFourMod = new ConnectFourModel(width, height, gameData.getChallengerId(),
                gameData.getOpponentId());

        String field = generateBoardString(conFourMod.getBoard(), conFourMod.getPlayer1());

        final TextChannel channel = jda.getTextChannelById(gameData.getChannelId());
        if (channel == null)
            return;

        channel.sendMessage(field).queue(msg ->
        {
            addReaction(msg, gameData.getWidth());
            conFourMod.setMsgId(msg.getId());
            conFourMod.setActPlayer(conFourMod.getPlayer1());
            new ConnectFourDaoImpl().insertConnectFourData(conFourMod);
        });
    }

    private static String generateBoardString(char[][] board, String userId)
    {
        final User[] user = new User[1];
        Main.getJDA().retrieveUserById(userId).queue(u -> user[0] = u);

        StringBuilder field = new StringBuilder(user[0].getAsMention() + " :red_circle: fängt an\n");

        for (int j = 0; j < board.length; j++)
        {
            for (int i = 0; i < board[0].length; i++)
            {
                field.append(":white_circle:");
            }
            field.append("\n");
        }
        return Util.addTableNumbers(field.toString(), board[0].length);
    }

    private static void addReaction(Message message, int count)
    {
        final String[] numberReactions = {"1⃣", "2⃣", "3⃣", "4⃣", "5⃣", "6⃣", "7⃣", "8⃣"};

        for (int i = 0; i < count; i++)
        {
            message.addReaction(numberReactions[i]).queue();
        }
    }
}

