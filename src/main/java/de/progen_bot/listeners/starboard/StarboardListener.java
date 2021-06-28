package de.progen_bot.listeners.starboard;

import de.progen_bot.core.Main;
import de.progen_bot.utils.starboard.Starboard;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StarboardListener extends ListenerAdapter
{
    private final Map<String, Starboard> starMessage = new HashMap<>();
    private final Map<String, Integer> starCount = new HashMap<>();

    public StarboardListener()
    {
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() ->
        {
            this.starMessage.forEach((id, starBoard) ->
            {
                printResult(id, starBoard, this.starCount.get(id));
            });

            this.starMessage.clear();
            this.starCount.clear();
        }, 0, 1, TimeUnit.DAYS);
    }

    // TODO: 26.06.2021 null check
    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event)
    {
        if (event.getReactionEmote().getName().equals("\u2B50"))
        {
            event.getJDA().getTextChannelById(event.getChannel().getId()).retrieveMessageById(event.getMessageId())
                    .queue(mMessage ->
                    {
                        mMessage.getReactions().forEach(reaction ->
                        {
                            if (reaction.getReactionEmote().getName().equals("\u2B50"))
                            {
                                Starboard starBoard = new Starboard(mMessage.getId(),
                                        mMessage.getTextChannel().getId());
                                if (!starMessage.containsKey(mMessage.getGuild().getId()))
                                {
                                    starMessage.put(mMessage.getGuild().getId(), starBoard);
                                    starCount.put(mMessage.getGuild().getId(), reaction.getCount());
                                }
                                else if (starCount.get(mMessage.getGuild().getId()) < reaction.getCount())
                                {
                                    starMessage.put(mMessage.getGuild().getId(), starBoard);
                                    starCount.put(mMessage.getGuild().getId(), reaction.getCount());
                                }
                            }
                        });
                    });
        }
    }

    // TODO: 26.06.2021 null checks 
    private void printResult(String guildId, Starboard starBoard, int count)
    {
        final JDA jda = Main.getJDA();

        jda.getTextChannelById(starBoard.getTextChannelId()).retrieveMessageById(starBoard.getMessageId())
                .queue(message ->
                {
                    final MessageEmbed embed = new EmbedBuilder()
                            .setFooter(message.getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME))
                            .setColor(Color.YELLOW)
                            .setAuthor(message.getAuthor().getAsTag(), null, message.getAuthor().getAvatarUrl())
                            .setTitle("Jump to message", message.getJumpUrl())
                            .setDescription(message.getContentDisplay())
                            .setImage(message.getAttachments().isEmpty() ? null
                                    : (message.getAttachments().get(0).isImage()
                                    ? message.getAttachments().get(0).getUrl()
                                    : null))
                            .build();

                    final List<TextChannel> channelList = jda.getGuildById(guildId)
                            .getTextChannelsByName("starboard", true);

                    if (!channelList.isEmpty())
                    {
                        channelList.get(0).sendMessage(count + " \u2B50").embed(embed).queue();
                    }
                });
    }
}
