package de.progen_bot.listeners;

import java.awt.Color;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.core.Main;
import de.progen_bot.util.StarBoard;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class StarBoardListener extends ListenerAdapter {

    /**
     * A HashMap with the key GuildId and value Starboard(MessageId, TextChannelId).
     */
    private final Map<String, StarBoard> starMessage = new HashMap<>();

    /**
     * A HashMap with the key GuildId and value the number of stars given.
     */
    private final Map<String, Integer> starCount = new HashMap<>();
    
    final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public StarBoardListener() {
        scheduler.scheduleAtFixedRate(() -> {
            starMessage.forEach((guildId, starBoard) -> {
                printResult(guildId, starBoard, starCount.get(guildId));
            });
            starMessage.clear();
            starCount.clear();
        }, 0, 1, TimeUnit.MINUTES);
    }

    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        if (event.getReactionEmote().getName().equals("\u2B50")) {
            event.getJDA().getTextChannelById(event.getChannel().getId()).retrieveMessageById(event.getMessageId())
                    .queue(mMessage -> {
                        mMessage.getReactions().forEach(reaction -> {
                            if (reaction.getReactionEmote().getName().equals("\u2B50")) {
                                StarBoard starBoard = new StarBoard(mMessage.getId(),
                                        mMessage.getTextChannel().getId());
                                if (!starMessage.containsKey(mMessage.getGuild().getId())) {
                                    starMessage.put(mMessage.getGuild().getId(), starBoard);
                                    starCount.put(mMessage.getGuild().getId(), reaction.getCount());
                                } else if (starCount.get(mMessage.getGuild().getId()) < reaction.getCount()) {
                                    starMessage.put(mMessage.getGuild().getId(), starBoard);
                                    starCount.put(mMessage.getGuild().getId(), reaction.getCount());
                                }
                            }
                        });
                    });
        }
    }

    GuildConfiguration configuration;

    private void printResult(String guildId, StarBoard starBoard, int count) {
        Main.getJda().getTextChannelById(configuration.getStarBoardChannelID()).retrieveMessageById(starBoard.getMessageId())
                .queue(message -> {
                    final MessageEmbed embed = new EmbedBuilder()
                            .setFooter(message.getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME))
                            .setColor(Color.YELLOW)
                            .setAuthor(message.getAuthor().getAsTag() , null , message.getAuthor().getAvatarUrl())
                            .setTitle("Jump to message" , message.getJumpUrl())
                            .setDescription(message.getContentDisplay())
                            .setImage(message.getAttachments().isEmpty() ? null
                                    : (message.getAttachments().get(0).isImage()
                                    ? message.getAttachments().get(0).getUrl()
                                    : null))
                            .build();

                    final List<TextChannel> channelList = Main.getJda()
                            .getTextChannelsByName(configuration.getStarBoardChannelID(), true);

                    if (!channelList.isEmpty()) {
                        channelList.get(0).sendMessage(count + " \u2B50").embed(embed).queue();
                    }
                });
    }
}