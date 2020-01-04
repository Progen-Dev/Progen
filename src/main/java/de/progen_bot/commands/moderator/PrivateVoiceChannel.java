package de.progen_bot.commands.moderator;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.db.dao.config.ConfigDaoImpl;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.listeners.PrivateVoice;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PrivateVoiceChannel extends CommandHandler {

    public PrivateVoiceChannel() {
        super("pc", "pc [create/add/category] </mentioned user/category name>",
                "You can create your private temporary voice channel. With the add command you can allow " +
                        "access to your channel. If no one is in it, it will be removed.");
    }

    private String privateChannelPrefix = PrivateVoice.PRIVATEVOICECHANNELPREFIX;

    private boolean checkOwnership(VoiceChannel channel, Member member) {

        return channel.getName().equals(privateChannelPrefix + " " + member.getUser().getName());
    }

    private void addUserToChannel(MessageReceivedEvent event) {

        // Check if user is specified
        if (event.getMessage().getMentionedMembers().size() != 1) {
            event.getTextChannel().sendMessage(super.messageGenerators.generateErrorMsgWrongInput()).queue();
            return;
        }

        //Check if user is in voice channel
        if (!event.getMember().getVoiceState().inVoiceChannel()) {
            event.getTextChannel().sendMessage(super.messageGenerators.generateErrorMsg("Your not in a voice channel!")).queue();
            return;
        }

        //Check if user is owner
        if (!checkOwnership(event.getMember().getVoiceState().getChannel(), event.getMember())) {
            event.getTextChannel().sendMessage(super.messageGenerators.generateErrorMsg("This is not your voice channel!")).queue();
            return;
        }

        event.getMember().getVoiceState().getChannel().createPermissionOverride(event.getMessage().getMentionedMembers().get(0)).setAllow(Permission.VOICE_CONNECT).queue();
        event.getTextChannel().sendMessage(super.messageGenerators.generateInfoMsg(event.getMessage().getMentionedMembers().get(0).getEffectiveName() + " can now join the voice channel")).queue();
        event.getMessage().getMentionedMembers().get(0).getUser().openPrivateChannel().complete().sendMessage(event.getMember().getEffectiveName() + " gave you the permission to join his/her voice channel").queue();
    }

    private VoiceChannel createChannel(MessageReceivedEvent event, String categoryID) {

        //Check if user is in voice channel
        if (!event.getMember().getVoiceState().inVoiceChannel()) {
            event.getTextChannel().sendMessage(super.messageGenerators.generateErrorMsg("Your not in a voice channel! Please join any voice channel.")).queue();
            return null;
        }

        // create new channel
        VoiceChannel channel =
                event.getGuild().createVoiceChannel(privateChannelPrefix + " " + event.getMember().getUser().getName()).complete();
        // set permissions
        channel.putPermissionOverride(event.getGuild().getPublicRole()).setDeny(Permission.VOICE_CONNECT).complete();
        // set category
        channel.getManager().setParent(event.getGuild().getCategoryById(categoryID)).queue();
        // move member
        event.getGuild().moveVoiceMember(event.getMember(), channel).queue();

        event.getTextChannel().sendMessage(super.messageGenerators.generateSuccessfulMsg()).queue();

        return channel;
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {

        if (parsedCommand.getArgsAsList().size() == 0) {
            event.getTextChannel().sendMessage(super.messageGenerators.generateErrorMsgWrongInput()).queue();
            return;
        }

        switch (parsedCommand.getArgsAsList().get(0)) {

            case "add":
                addUserToChannel(event);
                break;

            case "create":
                createChannel(event, configuration.tempChannelCategoryID);
                break;

            case "category":
                // If not owner
                if (!event.getMember().isOwner()) {
                    event.getTextChannel().sendMessage(super.messageGenerators.generateErrorMsg("Sorry, you have to be the owner of the guild to create the category.")).queue();
                    return;
                }

                // get length
                if (parsedCommand.getArgsAsList().size() != 2) {
                    event.getTextChannel().sendMessage(super.messageGenerators.generateErrorMsgWrongInput()).queue();
                    return;
                }

                // create channel
                String id = event.getGuild().createCategory(parsedCommand.getArgsAsList().get(1)).complete().getId();

                configuration.tempChannelCategoryID = id;
                new ConfigDaoImpl().writeConfig(configuration, event.getGuild());
                break;

            default:
                event.getTextChannel().sendMessage(super.messageGenerators.generateErrorMsgWrongInput()).queue();
        }
    }

    @Override
    public String help() {
        return null;
    }
}
