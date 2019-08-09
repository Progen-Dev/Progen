package de.progen_bot.commands.Moderator;

import de.mtorials.config.GuildConfiguration;
import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.core.PermissionCore;
import de.progen_bot.util.Settings;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CmdPoll extends CommandHandler implements Serializable {
    public CmdPoll() {
        super("poll" , "poll create <question>|<answer>|<answer2>|...\n vote number `vote for a answer`\n vote stats `Show the stats of the current vote\n vote close`Close the current vote" , "Vote evnt");
    }
    private static TextChannel channel;
    private static HashMap<Guild, Poll> polls = new HashMap<>();
    private static final String[] numbers = {":one:", ":two:", ":three:", ":four:", ":five:", ":six:", ":seven:", ":seven:", ":eight:", ":nine:", ":keycap_ten:"};

    private class Poll implements Serializable{
        private String creator;
        private String heading;
        private List<String> answers;
        private HashMap<String, Integer> votes;

        private Poll(Member creator, String heading, List<String> answers){
            this.creator = creator.getUser().getId();
            this.heading = heading;
            this.answers = answers;
            this.votes = new HashMap<>();
        }
        private Member getCreator(Guild guild){
            return guild.getMember(guild.getJDA().getUserById(creator));
        }
    }
    private static void message(String content){
        EmbedBuilder embedBuilder = new EmbedBuilder().setDescription(content);
        channel.sendMessage(embedBuilder.build()).queue();
    }
    private static void message(String content, Color color){
        EmbedBuilder embedBuilder = new EmbedBuilder().setDescription(content).setColor(color);
        channel.sendMessage(embedBuilder.build()).queue();
    }

    private EmbedBuilder getParsedPoll(Poll poll, Guild guild){
        StringBuilder anstr = new StringBuilder();
        final AtomicInteger count = new AtomicInteger();

        poll.answers.forEach(s ->{
            int votescount = poll.votes.values().stream().filter(i -> i - 1 == count.get()).findFirst().orElse(0);
            anstr.append(numbers[count.get()] + " - " + s +" - Votes:`" + votescount + "` \n");
            count.addAndGet(1);
        });
        return new EmbedBuilder()
                .setAuthor(poll.getCreator(guild).getEffectiveName() + "'s poll.", null, poll.getCreator(guild).getUser().getAvatarUrl())
                .setDescription(":pencil:   " + poll.heading + "\n\n" + anstr.toString())
                .setFooter("Enter '" + Settings.PREFIX + "poll v <number>' to vote!", null)
                .setColor(Color.cyan);
    }
    private void createPoll(String[] args, MessageReceivedEvent event){
        if (polls.containsKey(event.getGuild())){
            message("There is already a poll running on this guild!", Color.red);
            return;
        }
        String argsSTRG = String.join("  ", new ArrayList<>(Arrays.asList(args).subList(1, args.length)));
        List<String> content = Arrays.asList(argsSTRG.split("\\|"));
        String heading = content.get(0);
        List<String> answers = new ArrayList<>(content.subList(1, content.size()));

        Poll poll = new Poll(event.getMember(), heading, answers);
        polls.put(event.getGuild(), poll);

        channel.sendMessage(getParsedPoll(poll, event.getGuild()).build()).queue();

        channel = event.getTextChannel();

        if (args.length < 1){
            message(help(), Color.red);
            return;
        }
        switch (args[0]){

            case "create":
                createPoll(args,event);
                break;
        }

    }




    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {
        System.out.println("TEst");

    }

    @Override
    public String help() {
        return null;
    }
}
