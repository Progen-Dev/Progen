package de.progen_bot.listeners.game;

import de.progen_bot.core.Main;
import de.progen_bot.database.dao.connectfour.ConnectFourDaoImpl;
import de.progen_bot.database.dao.connectfour.GameDataDaoImpl;
import de.progen_bot.database.entities.connectfour.ConnectFourModel;
import de.progen_bot.database.entities.connectfour.GameData;
import de.progen_bot.misc.FourConnectGame;
import de.progen_bot.utils.misc.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class FourConnectListener extends ListenerAdapter
{
    private static final JDA JDA = Main.getJDA();
    private final Map<String, ScheduledFuture<?>> timerMap = new HashMap<>();

    private static void startGame(GameData gameData)
    {
        final User[] user = new User[1];
        final User[] opponent = new User[1];
        JDA.retrieveUserById(gameData.getChallengerId()).queue(u -> user[0] = u);
        JDA.retrieveUserById(gameData.getOpponentId()).queue(u -> opponent[0] = u);
        user[0].openPrivateChannel()
                .queue(privateChannel -> privateChannel
                        .sendMessage(opponent[0].getName()
                                + " hat deine Spielanfrage angenommen und eine Spielinstanz wird erstellt!")
                        .queue(msg ->
                        {

                            int height = gameData.getHeight();
                            int width = gameData.getWidth();

                            FourConnectGame.createGame(gameData, height, width, JDA);
                            msg.delete().queueAfter(10, TimeUnit.SECONDS);
                        }));
    }

    private static void closeGame(GameData gameData)
    {
        final User[] user = new User[1];
        final User[] opponent = new User[1];
        JDA.retrieveUserById(gameData.getChallengerId()).queue(u -> user[0] = u);
        JDA.retrieveUserById(gameData.getOpponentId()).queue(u -> opponent[0] = u);
        user[0].openPrivateChannel().queue(privateChannel -> privateChannel
                .sendMessage(opponent[0].getName() + " hat deine Spielanfrage abgelehnt!").queue());
    }

    @Override
    public void onPrivateMessageReactionAdd(PrivateMessageReactionAddEvent event)
    {
        final GameDataDaoImpl dao = new GameDataDaoImpl();

        if (event.getUser() == null)
            return;

        // check if reactionMessage is an invite
        if (dao.getGameData(event.getMessageId()) != null && !event.getUser().isBot())
        {
            final GameData gameData = dao.getGameData(event.getMessageId());
            final String emoteName = event.getReactionEmote().getName();

            final PrivateChannel channel = JDA.getPrivateChannelById(event.getChannel().getId());
            if (channel == null)
                return;

            channel.deleteMessageById(event.getReaction().getMessageId()).queue();

            if (emoteName.equals("✅"))
            {
                event.getChannel()
                        .sendMessage(new EmbedBuilder().setColor(Color.DARK_GRAY)
                                .setDescription("Spiel wird vorbereitet").build())
                        .queue(msg -> msg.delete().queueAfter(9, TimeUnit.SECONDS));
                startGame(gameData);
            }
            else if (emoteName.equals("❌"))
            {
                event.getChannel().sendMessage(new EmbedBuilder().setColor(Color.HSBtoRGB(85, 1, 100))
                        .setDescription("Spielanfrage abgelehnt!").build()).queue();
                closeGame(gameData);
            }
        }
    }

    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event)
    {
        final ConnectFourDaoImpl dao = new ConnectFourDaoImpl();

        if (event.getUser().isBot())
        {
            return;
        }

        ConnectFourModel gameData = dao.getConnectFourData(event.getMessageId());

        if (gameData != null)
        {
            if (gameData.isGameOver())
            {
                return;
            }

            if (gameData.nextPlayerMove(event.getUser().getId()))
            {
                int emote = Util.getIntegerFromReaction(event.getReactionEmote().getName());
                if (emote == 0)
                {
                    return;
                }
                if (timerMap.get(gameData.getMsgId()) != null)
                    timerMap.get(gameData.getMsgId()).cancel(true);

                gameData.setField(emote);

                event.getChannel().retrieveMessageById(event.getMessageId()).queue(gameData::updateBoard);

                if (gameData.isGameOver())
                {
                    final String text = event.getUser().getAsMention() + " hat gewonnen!";
                    event.getChannel().sendMessage(text).queue();
                    return;
                }

                setTimer(gameData, event);
            }
            event.getReaction().removeReaction(event.getUser()).queue();
        }
    }

    private void setTimer(ConnectFourModel gameData, GuildMessageReactionAddEvent event)
    {
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        ScheduledFuture<?> countdown = scheduler.schedule(() ->
        {
            gameData.setGameOver(true);
            final String text = "Timeout: " + event.getUser().getAsMention() + " hat gewonnen!";
            event.getChannel().sendMessage(text).queue();

        }, 1, TimeUnit.DAYS);
        timerMap.put(gameData.getMsgId(), countdown);
        scheduler.shutdown();
    }
}
