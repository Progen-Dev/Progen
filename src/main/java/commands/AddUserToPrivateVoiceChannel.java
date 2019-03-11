package commands;

import command.CommandHandler;
import command.CommandManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class AddUserToPrivateVoiceChannel extends CommandHandler {

    public AddUserToPrivateVoiceChannel() {
        super("AddUserToPrivateVoiceChannel","","add a user to your private channel");
        //TODO add usage
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event) {
        EmbedBuilder error =  new EmbedBuilder().setColor(Color.RED).setTitle("Error");
        EmbedBuilder ok = new EmbedBuilder().setColor(Color.green).setTitle("Erfolgreich");
        if (event.getMessage().getMentionedMembers().size() != 0){
            if (event.getMember().getVoiceState().inVoiceChannel()) {
                if (event.getMember().getVoiceState().getChannel().getName().equals("[PRIVAT] " + event.getMember().getUser().getName())) {
                    event.getMember().getVoiceState().getChannel().createPermissionOverride(event.getMessage().getMentionedMembers().get(0)).setAllow(Permission.VOICE_CONNECT).queue();
                    ok.setDescription(event.getMessage().getMentionedMembers().get(0).getAsMention()+" hat jetzt rechte in deinen Voicechannel zu joinen");
                    event.getTextChannel().sendMessage(ok.build()).queue();
                    event.getMessage().getMentionedMembers().get(0).getUser().openPrivateChannel().complete().sendMessage("User "+ event.getMember().getAsMention() +" hat dir Rechte gegeben seinem Voicechannel zu joinen").queue();
                } else {
                    error.setDescription("Du besitzt keinen Privaten Voicechannel");
                    event.getTextChannel().sendMessage(error.build()).queue();
                    return;
                }
            } else {
                error.setDescription("Du bist in keinem Voicechannel");
                event.getTextChannel().sendMessage(error.build()).queue();
                return;
            }
        } else {
            error.setDescription("Du musst einen User auswählen");
            event.getTextChannel().sendMessage(error.build()).queue();
            return;
        }
    }
    @Override
    public String help() {
        return null;
    }
}
