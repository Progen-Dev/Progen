package de.progen_bot.commands.moderator.Blacklist;

import com.google.gson.internal.$Gson$Types;
import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.permissions.AccessLevel;
import de.progen_bot.permissions.PermissionCore;
import de.progen_bot.db.entities.config.GuildConfiguration;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.time.Instant;
import java.util.List;

public class CommandBan extends CommandHandler {
    private static final String BAN = "Ban";
    private static final  String EXECUTOR = "Executor";
    private static final String VICTIM = "Victim";
    private static final String REASON = "Reason";
    public CommandBan() {
        super("ban" , "ban <user> <reason>" , "Ban a user from this Server");
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand , MessageReceivedEvent event , GuildConfiguration configuration) {
        Member selfmember = event.getMember();

        final List<String> argsWithoutMention = parsedCommand.getArgsAsList().subList(1, parsedCommand.getArgsAsList().size());

        String reason;
        if (argsWithoutMention.isEmpty())
            reason = "No reason";
        else
            reason = String.join(" ", argsWithoutMention);
        event.getGuild().getTextChannelsByName("progenlog", true).get(0).sendMessage(this.getBanEmbed(event, reason)).queue();
        event.getTextChannel().sendMessage(this.getBanEmbed(event,reason)).queue();
        final String finalReason = reason;
        event.getMessage().getMentionedUsers().get(0).openPrivateChannel().queue(
                privateChannel -> privateChannel.sendMessage(this.getBanEmbed(event, finalReason)).queue()
        );
        if(parsedCommand.getArgs().length > 0){
            List<User> mentioned = event.getMessage().getMentionedUsers();
            for (User user : mentioned){
                Member member = event.getGuild().getMember(user);
                if (selfmember.canInteract(member)){
                    event.getGuild().ban(member, 7).queue();
                }
            }
        }
    }
    private MessageEmbed getBanEmbed(MessageReceivedEvent event, String reason){
        return new EmbedBuilder()
                .setColor(Color.red)
                .setTitle(BAN)
                .addField(VICTIM, event.getMessage().getMentionedMembers().get(0).getAsMention(), true)
                .addField(EXECUTOR, event.getMember().getAsMention(), true)
                .setDescription(event.getMessageId())
                .addField(REASON, reason, false)
                .setTimestamp(Instant.now()).build();
    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.MODERATOR;
    }
}
