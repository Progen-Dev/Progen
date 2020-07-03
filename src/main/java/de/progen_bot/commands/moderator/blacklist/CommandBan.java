package de.progen_bot.commands.moderator.blacklist;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.permissions.AccessLevel;
import de.progen_bot.db.entities.config.GuildConfiguration;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.internal.requests.Route;

import java.awt.*;
import java.nio.channels.Channels;
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
        Member selfMember = event.getMember();

        if (selfMember == null)
            return;

        final List<String> argsWithoutMention = parsedCommand.getArgsAsList().subList(1, parsedCommand.getArgsAsList().size());

        String reason;
        if (argsWithoutMention.isEmpty())
            reason = "No reason";
        else
            reason = String.join(" ", argsWithoutMention);

        final MessageEmbed eb = this.getBanEmbed(event, reason);
        if (eb == null)
            return;

        List<TextChannel> channels = event.getGuild().getTextChannelsByName("progenlog", true);
        if (channels.isEmpty()){
           event.getTextChannel().sendMessage(eb).queue();
       }else {
           System.out.println("progenlog is not available on the Guild" + event.getGuild());
       }
        event.getMessage().getMentionedUsers().get(0).openPrivateChannel().queue(
                privateChannel -> privateChannel.sendMessage(eb).queue()
        );
        if(parsedCommand.getArgs().length > 0){
            List<User> mentioned = event.getMessage().getMentionedUsers();
            for (User user : mentioned){
                Member member = event.getGuild().getMember(user);

                if (member == null)
                    return;

                if (selfMember.canInteract(member)){
                    event.getGuild().ban(member, 7).queue();
                }
            }
        }
    }
    private MessageEmbed getBanEmbed(MessageReceivedEvent event, String reason){
        if (event.getMember() == null)
            return null;
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
    public AccessLevel getAccessLevel() {
        return AccessLevel.MODERATOR;
    }
}
