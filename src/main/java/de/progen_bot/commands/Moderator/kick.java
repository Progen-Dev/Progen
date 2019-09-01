package de.progen_bot.commands.Moderator;

import de.mtorials.config.GuildConfiguration;
import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.core.PermissionCore;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;


public class kick extends CommandHandler{
    public kick() {
        super("kick", "kick <user> <reason>", "Kick a user from this server");
    }


    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand , MessageReceivedEvent event , GuildConfiguration configuration) {
        System.out.println("[Info] Command pb!kick wird ausgeführt!");

        if (PermissionCore.check(2, event)) return;

        String reason = " ";
        if (parsedCommand.lenght > 1) {
            reason = parsedCommand.getArgs()[1];
        }
        event.getTextChannel().sendMessage(

                ":small_red_triangle_down:  " + event.getGuild().getMember(event.getMessage().getMentionedUsers().get(0)).getAsMention() + " got kicked by " + event.getAuthor().getAsMention() + " (" + event.getMember().getRoles().get(0).getName() + ").\n\n" +
                        "Reason: " + reason
        ).queue();
        PrivateChannel pc = event.getMessage().getMentionedUsers().get(0).openPrivateChannel().complete();
        pc.sendMessage(
                "Sorry, you got kicked from Server " + event.getGuild().getName() + " by " + event.getAuthor().getAsMention() + " (" + event.getMember().getRoles().get(0).getName() + ").\n\n" +
                        "Reason: " + reason
        ).queue();
        event.getGuild().kick(
                event.getGuild().getMember(
                        event.getMessage().getMentionedUsers().get(0)
                )
        ).queue();
    }



    @Override
    public String help() {
        return null;
    }
}
