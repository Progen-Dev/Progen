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
        String userId = event.getAuthor().getId();

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
                        mSayModel.setStage(7);
                        sayStorage.replace(userId, mSayModel);
                        event.getChannel().sendMessage("Preview: if you want to send it send <y/n>").embed(mSayModel.getEmbedBuilder().build())
                                .queue();
                    } else {
                        event.getChannel().sendMessage("This is not a valid color!").queue();
                    }
                    break;
                case 7:
                    if(event.getMessage().getContentRaw().equals("y")){
                        event.getJDA().getTextChannelById(mSayModel.getTextchannelId())
                            .sendMessage(mSayModel.getEmbedBuilder().build()).queue();
                    }else{
                        sayStorage.remove(userId);
                    }
                    break;
            }
        }
    }

    public static void addSayModel(String userId, SayModel sayModel) {
        sayStorage.put(userId, sayModel);
    }

    Pattern colorPattern = Pattern.compile("#([0-9a-f]{3}|[0-9a-f]{6}|[0-9a-f]{8})");

    private boolean isHexadecimal(String input) {
        final Matcher matcher = colorPattern.matcher(input);
        return matcher.matches();
    }
}
