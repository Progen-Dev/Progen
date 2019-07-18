package de.progen_bot.commands.Moderator;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.core.PermissionCore;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Vote extends CommandHandler implements Serializable {
    public Vote() {
        super("Vote","vote create <question>|<answer>|<answer2>|...\n vote number `vote for a answer`\n vote stats `Show the stats of the current vote\n vote close`Close the current vote","Vote evnt");

    }
    private static TextChannel channel;
    private static HashMap<Guild, Pool> votepools = new HashMap<>();
    private static final String[] emjis = {":one:",":two:", ":three:", ":four:", ":five:", ":six:", ":seven:",":eight:", ":nine:", ":keycap_ten:"};
    private class Pool implements Serializable{
        private String creator;
        private String heading;
        private List<String> answers;
        private HashMap<String, Integer> votes;
        private Pool(Member member){

        }
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand , MessageReceivedEvent event) {
        if (PermissionCore.check(2, event));

    }

    @Override
    public String help() {
        return null;
    }
}
