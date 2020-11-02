package de.progen_bot.listeners;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.awt.Color;

import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SayListener extends ListenerAdapter {
    private static Map<String, SayModel> sayStorage = new HashMap<>();

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        final String userId = event.getAuthor().getId();

        if (sayStorage.containsKey(userId)) {

            if (event.getMessage().getContentRaw().equals("abort")) {
                sayStorage.remove(userId);
                return;
            }

            SayModel mSayModel = sayStorage.get(userId);
            switch (mSayModel.getStage()) {
                case 0:
                    if (isHexadecimal(event.getMessage().getContentRaw())) {

                        mSayModel.setEmbedBuilder(
                                mSayModel.getEmbedBuilder().setColor(Color.decode(event.getMessage().getContentRaw())));
                        mSayModel.setStage(1);
                        // Replace old sayModel in every stage
                        sayStorage.replace(userId, mSayModel);
                        event.getChannel().sendMessage("Please enter a title for your embed.").queue();
                    } else {
                        event.getChannel().sendMessage("This is not a valid color!").queue();
                    }
                    break;
                case 1:
                    mSayModel.setEmbedBuilder(mSayModel.getEmbedBuilder().setTitle(event.getMessage().getContentRaw()));
                    mSayModel.setStage(2);
                    sayStorage.replace(userId, mSayModel);
                    event.getChannel().sendMessage("Do you want to set an url for the title? <y/n>").queue();
                    break;
                case 2:
                    if (event.getMessage().getContentRaw().equals("y")) {
                        mSayModel.setStage(3);
                        sayStorage.replace(userId, mSayModel);
                        event.getChannel().sendMessage("Enter the url with http(s) for the title.").queue();
                    } else if (event.getMessage().getContentRaw().equals("n")) {
                        mSayModel.setStage(7);
                        sayStorage.replace(userId, mSayModel);
                        event.getChannel().sendMessage("Preview: if you want to send this embed, send <y/n>")
                                .embed(mSayModel.getEmbedBuilder().build()).queue();
                    }
                    break;
                case 3: // Set TitleUrl
                    mSayModel.setEmbedBuilder(mSayModel.getEmbedBuilder().setTitle(
                            mSayModel.getEmbedBuilder().build().getTitle(), event.getMessage().getContentRaw()));
                    mSayModel.setStage(7);
                    sayStorage.replace(userId, mSayModel);
                    event.getChannel().sendMessage("Preview: if you want to send this embed, send <y/n>")
                            .embed(mSayModel.getEmbedBuilder().build()).queue();
                    break;
                case 7:
                    if (event.getMessage().getContentRaw().equals("y")) {
                        event.getJDA().getTextChannelById(mSayModel.getTextchannelId())
                                .sendMessage(mSayModel.getEmbedBuilder().build()).queue();
                    } else {
                        sayStorage.remove(userId);
                    }
                    break;
            }
        }
    }

    public static void addSayModel(String userId, SayModel sayModel) {
        sayStorage.put(userId, sayModel);
    }

    final Pattern colorPattern = Pattern.compile("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$");

    private boolean isHexadecimal(String input) {
        final Matcher matcher = colorPattern.matcher(input);
        return matcher.matches();
    }
}
