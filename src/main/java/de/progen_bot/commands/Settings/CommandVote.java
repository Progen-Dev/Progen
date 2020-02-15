package de.progen_bot.commands.Settings;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;

import de.progen_bot.core.PermissionCore;
import de.progen_bot.db.entities.config.GuildConfiguration;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


public class CommandVote extends CommandHandler implements Serializable {
    private static final String[] EMOTES = ( "\uD83C\uDF4F \uD83C\uDF4E \uD83C\uDF50 \uD83C\uDF4A \uD83C\uDF4B \uD83C\uDF4C \uD83C\uDF49 \uD83C\uDF47 \uD83C\uDF53 \uD83C\uDF48 \uD83C\uDF52 \uD83C\uDF51 \uD83C\uDF4D \uD83E\uDD5D " +
            "\uD83E\uDD51 \uD83C\uDF45 \uD83C\uDF46 \uD83E\uDD52 \uD83E\uDD55 \uD83C\uDF3D \uD83C\uDF36 \uD83E\uDD54 \uD83C\uDF60 \uD83C\uDF30 \uD83E\uDD5C \uD83C\uDF6F \uD83E\uDD50 \uD83C\uDF5E " +
            "\uD83E\uDD56 \uD83E\uDDC0 \uD83E\uDD5A \uD83C\uDF73 \uD83E\uDD53 \uD83E\uDD5E \uD83C\uDF64 \uD83C\uDF57 \uD83C\uDF56 \uD83C\uDF55 \uD83C\uDF2D \uD83C\uDF54 \uD83C\uDF5F \uD83E\uDD59 " +
            "\uD83C\uDF2E \uD83C\uDF2F \uD83E\uDD57 \uD83E\uDD58 \uD83C\uDF5D \uD83C\uDF5C \uD83C\uDF72 \uD83C\uDF65 \uD83C\uDF63 \uD83C\uDF71 \uD83C\uDF5B \uD83C\uDF5A \uD83C\uDF59 \uD83C\uDF58 " +
            "\uD83C\uDF62 \uD83C\uDF61 \uD83C\uDF67 \uD83C\uDF68 \uD83C\uDF66 \uD83C\uDF70 \uD83C\uDF82 \uD83C\uDF6E \uD83C\uDF6D \uD83C\uDF6C \uD83C\uDF6B \uD83C\uDF7F \uD83C\uDF69 \uD83C\uDF6A \uD83E\uDD5B " +
            "\uD83C\uDF75 \uD83C\uDF76 \uD83C\uDF7A \uD83C\uDF7B \uD83E\uDD42 \uD83C\uDF77 \uD83E\uDD43 \uD83C\uDF78 \uD83C\uDF79 \uD83C\uDF7E \uD83E\uDD44 \uD83C\uDF74 \uD83C\uDF7D").split(" ");
    public static HashMap<Guild, Poll> polls = new HashMap<>();
    private static HashMap<Guild, Message> tempList = new HashMap<>();
    private static TextChannel channel;
    public CommandVote() {
        super("vote", "<prefix>vote create <question>|answer1|answer2|...\n <prefix>vote stats\n <prefix>vote secret <question>|answer1|answer2|...\n <prefix>poll close", "Create a survey easily a Poll");
    }

    private static void message(String content) {
        EmbedBuilder eb = new EmbedBuilder().setDescription(content);
        channel.sendMessage(eb.build()).queue();
    }

    private static void message(String content, Color color) {
        EmbedBuilder eb = new EmbedBuilder().setDescription(content).setColor(color);
        channel.sendMessage(eb.build()).queue();
    }

    private static EmbedBuilder getParsedPoll(Poll poll, Guild guild) {

        StringBuilder sb = new StringBuilder();
        final AtomicInteger count = new AtomicInteger();

        poll.answers.forEach(s -> {
            long votescount = poll.votes.keySet().stream().filter(k -> poll.votes.get(k).equals(count.get() + 1)).count();
            sb.append(poll.emotis.get(count.get()) + "  -  " + s + "  -  Votes: `" + votescount + "` \n");
            count.addAndGet(1);
        });

        return new EmbedBuilder()
                .setAuthor(poll.getCreator(guild).getEffectiveName(), null, poll.getCreator(guild).getUser().getAvatarUrl())
                .setDescription(poll.heading + "\n\n" + sb.toString())
                .setColor(Color.cyan);

    }

    private static void addVote(Guild guild, Member author, int voteIndex) {
        Poll poll = polls.get(guild);

        if (poll.votes.containsKey(author.getUser().getId())) {
            tempList.get(guild).getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.orange).setDescription("Sorry, " + author.getAsMention() + ", you can only vote once!").build()).queue(m ->
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            m.delete().queue();
                        }
                    }, 4000)
            );
            return;
        }

        poll.votes.put(author.getUser().getId(), voteIndex);
        polls.replace(guild, poll);
        tempList.get(guild).editMessage(getParsedPoll(poll, guild).build()).queue();


        try {
            savePoll(guild);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void handleReaction(MessageReactionAddEvent event) {

        Guild guild = event.getGuild();


        if (polls.containsKey(guild)) {
            Message msg = null;
            try {
                msg = tempList.get(guild);
            } catch (Exception e) {
                e.printStackTrace();
            }


            if (event.getMessageId().equals(msg != null ? msg.getId() : "") && !event.getMember().getUser().equals(event.getJDA().getSelfUser())) {
                List<String> reactions = msg.getReactions().stream().map(r -> r.getReactionEmote().getName()).collect(Collectors.toList());
                if (reactions.contains(event.getReaction().getReactionEmote().getName())) {
                    addVote(guild, event.getMember(), reactions.indexOf(event.getReaction().getReactionEmote().getName()) + 1);
                    event.getReaction().removeReaction(event.getUser()).queue();
                }
            }

        }
    }

    private static void savePoll(Guild guild) throws IOException {

        if (!polls.containsKey(guild))
            return;

        File path = new File("POLL/" + guild.getId());
        if (!path.exists()) {
            path.mkdirs();
        }

        String saveFile = "POLL/" + guild.getId() + "/poll.dat";
        Poll poll = polls.get(guild);

        FileOutputStream fos = new FileOutputStream(saveFile);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(poll);
        oos.close();
    }

    private static Poll getPoll(Guild guild) throws IOException, ClassNotFoundException {

        if (polls.containsKey(guild))
            return null;

        String saveFile = "POLL/" + guild.getId() + "/poll.dat";

        FileInputStream fis = new FileInputStream(saveFile);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Poll out = (Poll) ois.readObject();
        ois.close();
        return out;
    }

    public static void loadPolls(JDA jda) {

        jda.getGuilds().forEach(g -> {

            File f = new File("POLL/" + g.getId() + "/poll.dat");
            if (f.exists())
                try {
                    Poll poll = getPoll(g);
                    polls.put(g, poll);
                    tempList.put(g, poll.getMessage(g));
                } catch (IOException | ClassNotFoundException | NullPointerException e) {
                    e.printStackTrace();
                }

        });

    }

    private void createPoll(String[] args, MessageReceivedEvent event, boolean secret) {

        if (polls.containsKey(event.getGuild())) {
            messageGenerators.generateErrorMsg("There is already a vote running on this guild!");
            return;
        }

        String sb = String.join(" ", new ArrayList<>(Arrays.asList(args).subList(1, args.length)));
        List<String> content = Arrays.asList(sb.split("\\|"));
        String heading = content.get(0);
        List<String> answers = new ArrayList<>(content.subList(1, content.size()));
        Message msg = channel.sendMessage(messageGenerators.generateInfoMsg("CREATING...")).complete();
        List<String> emotes = new ArrayList<>(Arrays.asList(EMOTES));
        List<String> toAddEmotes = new ArrayList<>();
        answers.forEach(a -> {
            int rand = new Random().nextInt(emotes.size() - 1);
            String randEmotes = emotes.get(rand);
            emotes.remove(rand);
            toAddEmotes.add(randEmotes);
        });

        Poll poll = new Poll(event.getMember(), heading, answers, toAddEmotes, msg, secret);

        polls.put(event.getGuild(), poll);

        channel.editMessageById(msg.getId(), getParsedPoll(poll, event.getGuild()).build()).queue();
        toAddEmotes.forEach(s -> msg.addReaction(s).queue());
        channel.pinMessageById(msg.getId()).queue();

        tempList.put(event.getGuild(), polls.get(event.getGuild()).getMessage(event.getGuild()));

        try {
            savePoll(event.getGuild());
        } catch (IOException e) {
            e.printStackTrace();
        }

        event.getMessage().delete().queue();

    }

    private void statsPoll(MessageReceivedEvent event) {

        if (!polls.containsKey(event.getGuild())) {
            messageGenerators.generateErrorMsg("There is currently no vote running!");
        }

        Poll poll = polls.get(event.getGuild());
        Guild g = event.getGuild();

        if (poll.secret && !poll.getMessage(g).getTextChannel().equals(event.getTextChannel())) {
            message("The running poll is a `secret` poll and can only be accessed from the channel where it was created from!", Color.red);
            return;
        }

        channel.sendMessage(getParsedPoll(poll, g).build()).queue();

    }

    private void closePoll(MessageReceivedEvent event) {

        if (!polls.containsKey(event.getGuild())) {
            messageGenerators.generateErrorMsg("There is currently no vote running!");
            return;
        }

        Guild g = event.getGuild();
        Poll poll = polls.get(g);

        if (poll.secret && !poll.getMessage(g).getTextChannel().equals(event.getTextChannel())) {
            messageGenerators.generateInfoMsg("The running poll is a `secret` poll and can only be accessed from the channel where it was created from!");
        }

        if (!poll.getCreator(g).equals(event.getMember()) && PermissionCore.check(2, event)) {
            messageGenerators.generateErrorMsg("Only the creator of the poll (" + poll.getCreator(g).getAsMention() + ") can close this poll!");
        }

        tempList.get(event.getGuild()).delete().queue();
        polls.remove(g);
        tempList.remove(g);
        channel.sendMessage(getParsedPoll(poll, g).build()).queue();
        messageGenerators.generateRightMsg("Poll closed by " + event.getAuthor().getAsMention() + ".");

        new File("POLL/" + event.getGuild().getId() + "/poll.dat").delete();
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {
        if (PermissionCore.check(2, event));
        channel = event.getTextChannel();

        if (parsedCommand.getArgs().length < 1) {
            message(help(), Color.red);
            return;
        }

        switch (parsedCommand.getArgs()[0]) {

            case "create":
                createPoll(parsedCommand.getArgs(), event, false);
                break;

            case "secret":
                createPoll(parsedCommand.getArgs(), event, true);
                messageGenerators.generateInfoMsg("The created vote is `secret` and can only be accessed from this channel.");
                break;
            case "stats":
                statsPoll(event);
                break;

            case "close":
                closePoll(event);
                break;

        }

    }

    @Override
    public String help() {
        return null;
    }

    public class Poll implements Serializable {

        private static final long serialVersionUID = -4410308390500314827L;
        private String creator;
        private String heading;
        private List<String> answers;
        private List<String> emotis;
        private String message;
        private HashMap<String, Integer> votes;
        private boolean secret;

        public Poll(Member creator, String heading, List<String> answers, List<String> emotis, Message message, boolean secret) {
            this.creator = creator.getUser().getId();
            this.heading = heading;
            this.answers = answers;
            this.emotis = emotis;
            this.votes = new HashMap<>();
            this.message = message.getId();
            this.secret = secret;
        }

        private Member getCreator(Guild guild) {
            return guild.getMember(guild.getJDA().getUserById(creator));
        }

        public Message getMessage(Guild guild) {
            List<Message> messages = new ArrayList<>();
            guild.getTextChannels().forEach(c -> {
                try {
                    messages.add(c.retrieveMessageById(message).complete());
                } catch (Exception e) { }
            });
            return messages.isEmpty() ? null : messages.get(0);
        }

    }
}

