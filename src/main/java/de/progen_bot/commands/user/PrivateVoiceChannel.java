package de.progen_bot.commands.user;

import java.util.EnumSet;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.db.dao.config.ConfigDaoImpl;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.listeners.PrivateVoice;
import de.progen_bot.permissions.AccessLevel;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.ErrorResponse;

public class PrivateVoiceChannel extends CommandHandler {

    public PrivateVoiceChannel() {
        super("pc",
                "`pc create` creates a private voice channel.\n"
                        + "`pc add <mentioned user>` adds the mentioned user to the channel.\n"
                        + "`pc category <category name>` sets the category under which the channels will be created.",
                "You can create your private temporary voice channel. With the add command you can allow "
                        + "access to your channel. If no one is in it, it will be removed.");
    }

    private static final String PRIVATE_CHANNEL_PREFIX = PrivateVoice.PRIVATEVOICECHANNELPREFIX;

    private boolean checkOwnership(VoiceChannel channel, Member member) {

        return channel.getName().equals(PRIVATE_CHANNEL_PREFIX + " " + member.getUser().getName());
    }

    private void addUserToChannel(MessageReceivedEvent event) {
        final Member member = event.getMember();
        if (member == null)
            return;

        // Check if user is specified
        if (event.getMessage().getMentionedMembers().size() != 1) {
            event.getTextChannel().sendMessage(super.messageGenerators.generateErrorMsgWrongInput()).queue();
            return;
        }

        final GuildVoiceState voiceState = member.getVoiceState();
        if (voiceState == null)
            return;

        // Check if user is in voice channel
        if (!voiceState.inVoiceChannel()) {
            event.getTextChannel()
                    .sendMessage(super.messageGenerators.generateErrorMsg("You are not in a voice channel!")).queue();
            return;
        }

        final VoiceChannel channel = voiceState.getChannel();
        if (channel == null)
            return;

        // Check if user is owner
        if (!checkOwnership(channel, member)) {
            event.getTextChannel()
                    .sendMessage(super.messageGenerators.generateErrorMsg("This is not your voice channel!")).queue();
            return;
        }

        channel.createPermissionOverride(event.getMessage().getMentionedMembers().get(0))
                .setAllow(Permission.VOICE_CONNECT).queue();

        event.getTextChannel().sendMessage(super.messageGenerators.generateInfoMsg(
                event.getMessage().getMentionedMembers().get(0).getEffectiveName() + " can now join the voice channel"))
                .queue();

        event.getMessage().getMentionedMembers().get(0).getUser().openPrivateChannel()
                .flatMap(privateChannel -> privateChannel
                        .sendMessage(
                                member.getEffectiveName() + " gave you the permission to join his/her voice channel")
                        .onErrorFlatMap(ErrorResponse.CANNOT_SEND_TO_USER::test, (error) -> event.getTextChannel()
                                .sendMessage("Please enable your DM´s to use this command.")))
                .queue();
    }

    private void createChannel(MessageReceivedEvent event, String categoryID) {
        final Member member = event.getMember();
        if (member == null)
            return;

        final GuildVoiceState guildVoiceState = member.getVoiceState();
        if (guildVoiceState == null)
            return;

        if (!member.getVoiceState().inVoiceChannel()) {
            event.getTextChannel()
                    .sendMessage(super.messageGenerators
                            .generateErrorMsg("You are not in a voice channel! Please join any voice channel."))
                    .queue();
            return;
        }

        // create new channel
        event.getGuild().createVoiceChannel(PRIVATE_CHANNEL_PREFIX + " " + member.getUser().getName())
                .addPermissionOverride(event.getGuild().getPublicRole(), null, EnumSet.of(Permission.VOICE_CONNECT))
                .addMemberPermissionOverride(member.getIdLong(), EnumSet.of(Permission.VOICE_CONNECT), null)
                .queue(channel -> {
                    if (categoryID != null) {
                        channel.getManager().setParent(event.getGuild().getCategoryById(categoryID)).queue();
                    }

                    event.getGuild().moveVoiceMember(member, channel).queue();
                    event.getTextChannel().sendMessage(super.messageGenerators.generateSuccessfulMsg()).queue();
                });
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event,
            GuildConfiguration configuration) {
        final Member member = event.getMember();
        if (member == null)
            return;

        if (parsedCommand.getArgsAsList().isEmpty()) {
            event.getTextChannel().sendMessage(super.messageGenerators.generateErrorMsgWrongInput()).queue();
            return;
        }

        switch (parsedCommand.getArgsAsList().get(0)) {

            case "add":
                addUserToChannel(event);
                break;

            case "create":
                createChannel(event, configuration.getTempChannelCategoryID());
                break;

            case "category":
                // If not owner
                if (!member.isOwner()) {
                    event.getTextChannel().sendMessage(super.messageGenerators
                            .generateErrorMsg("Sorry, you have to be the owner of the guild to create the category."))
                            .queue();
                    return;
                }

                // get length
                if (parsedCommand.getArgsAsList().size() != 2) {
                    event.getTextChannel().sendMessage(super.messageGenerators.generateErrorMsgWrongInput()).queue();
                    return;
                }

                // create channel

                event.getGuild().createCategory(parsedCommand.getArgsAsList().get(1))
                        .queue(c -> configuration.setTempChannelCategoryID(c.getId()));
                new ConfigDaoImpl().writeConfig(configuration, event.getGuild());
                break;

            default:
                event.getTextChannel().sendMessage(super.messageGenerators.generateErrorMsgWrongInput()).queue();
        }
    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.USER;
    }
}
