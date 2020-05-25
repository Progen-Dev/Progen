package de.progen_bot.listeners;

import de.progen_bot.core.Main;
import de.progen_bot.db.dao.connectfour.ConnectFourDaoImpl;
import de.progen_bot.db.entities.ConnectFourModel;
import de.progen_bot.db.entities.GameData;
import de.progen_bot.game.FourConnectGame;
import de.progen_bot.util.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class FourConnectListener extends ListenerAdapter {
    private static JDA jda;
    ConnectFourDaoImpl dao;

    @Override
    public void onPrivateMessageReactionAdd(PrivateMessageReactionAddEvent event) {
        jda = Main.getJda();
        dao = new ConnectFourDaoImpl();

        if (event.getUser() == null)
            return;

        // check if reactionMessage is an invite
        if (dao.getGameData(event.getMessageId()) != null && !event.getUser().isBot()) {
            final GameData gameData = dao.getGameData(event.getMessageId());
            final String emoteName = event.getReactionEmote().getName();

            final PrivateChannel channel = jda.getPrivateChannelById(event.getChannel().getId());
            if (channel == null)
                return;

            channel.deleteMessageById(event.getReaction().getMessageId()).queue();

            if (emoteName.equals("✅")) {
                event.getChannel().sendMessage(
                        new EmbedBuilder().setColor(Color.DARK_GRAY).setDescription("Spiel wird vorbereitet").build()
                ).queue(msg -> msg.delete().queueAfter(9, TimeUnit.SECONDS));
                startGame(gameData);
            } else if (emoteName.equals("❌")) {
                event.getChannel().sendMessage(
                        new EmbedBuilder().setColor(Color.HSBtoRGB(85, 1, 100)).setDescription("Spielanfrage abgelehnt!").build()
                ).queue();
                closeGame(gameData);
            }
        }
    }

    private static void startGame(GameData gameData) {
        final User[] user = new User[1];
        final User[] opponent = new User[1];
        jda.retrieveUserById(gameData.getChallengerId()).queue(u -> user[0] = u);
        jda.retrieveUserById(gameData.getOpponentId()).queue(u -> opponent[0] = u);
        user[0].openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(opponent[0].getName()
                + " hat deine Spielanfrage angenommen und eine Spielinstanz wird erstellt!").queue(msg -> {

            int height = gameData.getHeight();
            int width = gameData.getWidth();

            FourConnectGame.createGame(gameData, height, width, jda);
            msg.delete().queueAfter(10, TimeUnit.SECONDS);
        }));
    }

    private static void closeGame(GameData gameData) {
        final User[] user = new User[1];
        final User[] opponent = new User[1];
        jda.retrieveUserById(gameData.getChallengerId()).queue(u -> user[0] = u);
        jda.retrieveUserById(gameData.getOpponentId()).queue(u -> opponent[0] = u);
        user[0].openPrivateChannel().queue(privateChannel -> privateChannel
                .sendMessage(
                        opponent[0].getName() + " hat deine Spielanfrage abgelehnt!")
                .queue());
    }

    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        dao = new ConnectFourDaoImpl();

        if (event.getUser().isBot()) {
            return;
        }

        ConnectFourModel gameData = dao.getConnectFourData(event.getMessageId());

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

                event.getChannel().retrieveMessageById(event.getMessageId())
                        .queue(gameData::updateBoard);

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
