package de.progen_bot.commands.Moderator;

import de.mtorials.config.GuildConfiguration;
import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.listeners.PrivateVoice;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.requests.Route;
import net.dv8tion.jda.core.utils.PermissionUtil;

import java.awt.*;

public class PrivateVoiceChannel extends CommandHandler {

    public PrivateVoiceChannel() {
        super("pc","pc [create/add] <mentioned user>","");
        //TODO add usage
    }

    private String privateChannelPrefix = PrivateVoice.PRIVATEVOICECHANNELPREFIX;

    private boolean checkOwnership(Channel channel, Member member) {

        return channel.getName().equals( privateChannelPrefix + " " + member.getUser().getName());
    }

    private void addUserToChannel(MessageReceivedEvent event) {

        // Check if user is specified
        if (event.getMessage().getMentionedMembers().size() != 1) {
            event.getTextChannel().sendMessage(super.generateErrorMsgWrongInput()).queue();
            return;
        }

        //Check if user is in voice channel
        if (!event.getMember().getVoiceState().inVoiceChannel()) {
            event.getTextChannel().sendMessage(super.generateErrorMsg("Your not in a voice channel!")).queue();
            return;
        }

        //Check if user is owner
        if (!checkOwnership(event.getMember().getVoiceState().getChannel(), event.getMember())) {
            event.getTextChannel().sendMessage(super.generateErrorMsg("This is not your voice channel!")).queue();
            return;
        }

        event.getMember().getVoiceState().getChannel().createPermissionOverride(event.getMessage().getMentionedMembers().get(0)).setAllow(Permission.VOICE_CONNECT).queue();
        event.getTextChannel().sendMessage(super.generateInfoMsg(event.getMessage().getMentionedMembers().get(0).getEffectiveName() + " can now join the voice channel")).queue();
        event.getMessage().getMentionedMembers().get(0).getUser().openPrivateChannel().complete().sendMessage(event.getMember().getEffectiveName() +" gave you the permission to join his/her voice channel").queue();
    }

    private Channel createChannel(MessageReceivedEvent event, String categoryID) {

        //Check if user is in voice channel
        if (!event.getMember().getVoiceState().inVoiceChannel()) {
            event.getTextChannel().sendMessage(super.generateErrorMsg("Your not in a voice channel! Please join any unimportant voice channel.")).queue();
            return null;
        }

        // create new channel
        Channel channel = event.getGuild().getController().createVoiceChannel(privateChannelPrefix + " " + event.getMember().getUser().getName()).complete();
        // set permissions
        channel.putPermissionOverride(event.getGuild().getPublicRole()).setDeny(Permission.VOICE_CONNECT).complete();
        // set category
        channel.getManager().setParent(event.getGuild().getCategoryById(categoryID)).queue();
        // move member
        event.getGuild().getController().moveVoiceMember(event.getMember(), (VoiceChannel)channel).queue();

        event.getTextChannel().sendMessage(super.generateSuccessfulMsg()).queue();

        return channel;
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {

        if (parsedCommand.getArgsAsList().size() == 0) {
            event.getTextChannel().sendMessage(super.generateErrorMsgWrongInput()).queue();
            return;
        }

        switch(parsedCommand.getArgsAsList().get(0)) {

            case "add":
                addUserToChannel(event);
                break;

            case "create":
                createChannel(event, configuration.getTempChannelCatergoryID());
                break;

            default:
                event.getTextChannel().sendMessage(super.generateErrorMsgWrongInput()).queue();
        }
    }

    @Override
    public String help() {
        return null;
    }
}
