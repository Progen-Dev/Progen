package de.progen_bot.listeners.say;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SayListener extends ListenerAdapter
{
    private static final Pattern COLOR_PATTERN = Pattern.compile("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$");
    private static final Map<Long, SayModel> SAY_STORAGE = new HashMap<>();

    public static void addModel(long userId, SayModel sayModel)
    {
        SAY_STORAGE.put(userId, sayModel);
    }

    private static boolean isHexadecimal(String input)
    {
        return COLOR_PATTERN.matcher(input).matches();
    }

    @Override
    public void onPrivateMessageReceived(@Nonnull PrivateMessageReceivedEvent event)
    {
        final long userId = event.getAuthor().getIdLong();

        if (SAY_STORAGE.containsKey(userId))
        {
            final String content = event.getMessage().getContentRaw();

            if (content.equalsIgnoreCase("abort"))
            {
                SAY_STORAGE.remove(userId);
                return;
            }

            SayModel sayModel = SAY_STORAGE.get(userId);
            switch (sayModel.getStage()) {
                case 0:
                    if (isHexadecimal(event.getMessage().getContentRaw())) {

                        sayModel.setEmbedBuilder(
                                sayModel.getEmbedBuilder().setColor(Color.decode(event.getMessage().getContentRaw())));
                        sayModel.setStage(1);
                        // Replace old sayModel in every stage
                        SAY_STORAGE.replace(userId, sayModel);
                        event.getChannel().sendMessage("Please enter a title for your embed.").queue();
                    } else {
                        event.getChannel().sendMessage("This is not a valid color!").queue();
                    }
                    break;
                case 1:
                    sayModel.setEmbedBuilder(sayModel.getEmbedBuilder().setTitle(event.getMessage().getContentRaw()));
                    sayModel.setStage(2);
                    SAY_STORAGE.replace(userId, sayModel);
                    event.getChannel().sendMessage("Do you want to set an url for the title? <y/n>").queue();
                    break;
                case 2:
                    if (event.getMessage().getContentRaw().equals("y")) {
                        sayModel.setStage(3);
                        SAY_STORAGE.replace(userId, sayModel);
                        event.getChannel().sendMessage("Enter the url with http(s) for the title.").queue();
                    } else if (event.getMessage().getContentRaw().equals("n")) {
                        sayModel.setStage(7);
                        SAY_STORAGE.replace(userId, sayModel);
                        event.getChannel().sendMessage("Preview: if you want to send this embed, send <y/n>")
                                .embed(sayModel.getEmbedBuilder().build()).queue();
                    }
                    break;
                // Set TitleUrl
                case 3:
                    sayModel.setEmbedBuilder(sayModel.getEmbedBuilder().setTitle(
                            sayModel.getEmbedBuilder().build().getTitle(), event.getMessage().getContentRaw()));
                    sayModel.setStage(7);
                    SAY_STORAGE.replace(userId, sayModel);
                    event.getChannel().sendMessage("Preview: if you want to send this embed, send <y/n>")
                            .embed(sayModel.getEmbedBuilder().build()).queue();
                    break;
                case 7:
                    if (event.getMessage().getContentRaw().equals("y")) {
                        final TextChannel channel = event.getJDA().getTextChannelById(sayModel.getTextChannelId());

                        if (channel == null)
                        {

                            event.getChannel().sendMessage("An error occurred").queue();
                            return;
                        }

                        channel.sendMessage(sayModel.getEmbedBuilder().build()).queue();
                    } else {
                        SAY_STORAGE.remove(userId);
                    }
                    break;
            }
        }
    }

    public static final class SayModel
    {
        private long textChannelId;
        private int stage;
        private EmbedBuilder embedBuilder;

        public SayModel(long textChannelId, int stage, EmbedBuilder embedBuilder)
        {
            this.textChannelId = textChannelId;
            this.stage = 0;
            this.embedBuilder = embedBuilder;
        }

        public long getTextChannelId()
        {
            return textChannelId;
        }

        public void setTextChannelId(long textChannelId)
        {
            this.textChannelId = textChannelId;
        }

        public int getStage()
        {
            return stage;
        }

        public void setStage(int stage)
        {
            this.stage = stage;
        }

        public EmbedBuilder getEmbedBuilder()
        {
            return embedBuilder;
        }

        public void setEmbedBuilder(EmbedBuilder embedBuilder)
        {
            this.embedBuilder = embedBuilder;
        }
    }
}
