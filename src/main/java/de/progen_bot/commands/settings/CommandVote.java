package de.progen_bot.commands.settings;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;

import de.progen_bot.permissions.AccessLevel;
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
import java.nio.file.Files;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


public class CommandVote extends CommandHandler implements Serializable {
    private static final long serialVersionUID = -5977967282764066009L;
    private static final String[] EMOTES = ("\uD83C\uDF4F \uD83C\uDF4E \uD83C\uDF50 \uD83C\uDF4A \uD83C\uDF4B \uD83C\uDF4C \uD83C\uDF49 \uD83C\uDF47 \uD83C\uDF53 \uD83C\uDF48 \uD83C\uDF52 \uD83C\uDF51 \uD83C\uDF4D \uD83E\uDD5D ")
            .split(" ");
    private static final Map<Guild, Poll> POLLS = new HashMap<>();
    private static final Map<Guild, Message> TEMP_MAP = new HashMap<>();
    private static TextChannel channel;
    public CommandVote() {
        super("vote", "<prefix>vote create <question>|answer1|answer2|...\n <prefix>vote stats\n <prefix>vote secret <question>|answer1|answer2|...\n <prefix>poll close", "Create a survey easily a Poll");
    }

    private static void message(String content) {
        EmbedBuilder eb = new EmbedBuilder().setDescription(content).setColor(Color.red);
        channel.sendMessage(eb.build()).queue();
    }

    private static EmbedBuilder getParsedPoll(Poll poll, Guild guild) {

        StringBuilder sb = new StringBuilder();
        final AtomicInteger count = new AtomicInteger();

        poll.answers.forEach(s -> {
            long votesCount = poll.votes.keySet().stream().filter(k -> poll.votes.get(k).equals(count.get() + 1)).count();
            sb.append(poll.emojis.get(count.get())).append("  -  ").append(s).append("  -  Votes: `").append(votesCount).append("` \n");
            count.addAndGet(1);
        });

        return new EmbedBuilder()
                .setAuthor(poll.getCreator(guild).getEffectiveName(), null, poll.getCreator(guild).getUser().getAvatarUrl())
                .setDescription(poll.heading + "\n\n" + sb.toString())
                .setColor(Color.cyan);

    }

    private static void addVote(Guild guild, Member author, int voteIndex) {
        Poll poll = POLLS.get(guild);

        if (poll.votes.containsKey(author.getUser().getId())) {
            TEMP_MAP.get(guild).getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.orange).setDescription("Sorry, " + author.getAsMention() + ", you can only vote once!").build()).queue(m ->
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
        POLLS.replace(guild, poll);
        TEMP_MAP.get(guild).editMessage(getParsedPoll(poll, guild).build()).queue();


        try {
            savePoll(guild);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void handleReaction(MessageReactionAddEvent event) {

        final Guild guild = event.getGuild();
        final Member member = event.getMember();

        if (member == null)
            return;

        if (POLLS.containsKey(guild)) {
            Message msg = null;
            try {
                msg = TEMP_MAP.get(guild);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (msg == null)
                return;

            if (event.getMessageId().equals(msg.getId()) && !event.getMember().equals(guild.getSelfMember())) {
                List<String> reactions = msg.getReactions().stream().map(r -> r.getReactionEmote().getName()).collect(Collectors.toList());
                if (reactions.contains(event.getReaction().getReactionEmote().getName())) {
                    addVote(guild, event.getMember(), reactions.indexOf(event.getReaction().getReactionEmote().getName()) + 1);
                    event.getReaction().removeReaction(member.getUser()).queue();
                }
            }

        }
    }

    private static void savePoll(Guild guild) throws IOException {

        if (!POLLS.containsKey(guild))
            return;

        File path = new File("POLL/" + guild.getId());
        if (!path.exists()) {
            path.mkdirs();
        }

        String saveFile = "POLL/" + guild.getId() + "/poll.dat";
        Poll poll = POLLS.get(guild);

        FileOutputStream fos = new FileOutputStream(saveFile);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(poll);
        oos.close();
    }

    private static Poll getPoll(Guild guild) throws IOException, ClassNotFoundException {

        if (POLLS.containsKey(guild))
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
                    final Poll poll = getPoll(g);

                    if (poll == null)
                        return;

                    POLLS.put(g, poll);
                    TEMP_MAP.put(g, poll.getMessage(g));
                } catch (IOException | ClassNotFoundException | NullPointerException e) {
                    e.printStackTrace();
                }

        });

    }

    private void createPoll(String[] args, MessageReceivedEvent event, boolean secret) {

        if (POLLS.containsKey(event.getGuild())) {
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

        if (event.getMember() == null)
            return;

        Poll poll = new Poll(event.getMember(), heading, answers, toAddEmotes, msg, secret);

        POLLS.put(event.getGuild(), poll);

        channel.editMessageById(msg.getId(), getParsedPoll(poll, event.getGuild()).build()).queue();
        toAddEmotes.forEach(s -> msg.addReaction(s).queue());
        channel.pinMessageById(msg.getId()).queue();

        TEMP_MAP.put(event.getGuild(), POLLS.get(event.getGuild()).getMessage(event.getGuild()));

        try {
            savePoll(event.getGuild());
        } catch (IOException e) {
            e.printStackTrace();
        }

        event.getMessage().delete().queue();

    }

    private void statsPoll(MessageReceivedEvent event) {

        if (!POLLS.containsKey(event.getGuild())) {
            messageGenerators.generateErrorMsg("There is currently no vote running!");
        }

        Poll poll = POLLS.get(event.getGuild());
        Guild g = event.getGuild();

        if (poll.secret && !poll.getMessage(g).getTextChannel().equals(event.getTextChannel())) {
            message("The running poll is a `secret` poll and can only be accessed from the channel where it was created from!");
            return;
        }

        channel.sendMessage(getParsedPoll(poll, g).build()).queue();

    }

    private void closePoll(MessageReceivedEvent event) {

        if (!POLLS.containsKey(event.getGuild())) {
            messageGenerators.generateErrorMsg("There is currently no vote running!");
            return;
        }

        Guild g = event.getGuild();
        Poll poll = POLLS.get(g);

        if (poll.secret && !poll.getMessage(g).getTextChannel().equals(event.getTextChannel())) {
            messageGenerators.generateInfoMsg("The running poll is a `secret` poll and can only be accessed from the channel where it was created from!");
        }

        TEMP_MAP.get(event.getGuild()).delete().queue();
        POLLS.remove(g);
        TEMP_MAP.remove(g);
        channel.sendMessage(getParsedPoll(poll, g).build()).queue();
        messageGenerators.generateRightMsg("Poll closed by " + event.getAuthor().getAsMention() + ".");

        try {
            Files.delete(new File(String.format("POLL/%s/poll.dat", event.getGuild().getId())).toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {
        setChannel(event.getTextChannel());

        if (parsedCommand.getArgs().length < 1) {
            message("Work in Progress!"); // TODO: 23.05.2020
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
            default:
                this.messageGenerators.generateErrorMsgWrongInput();
                break;
        }

    }

    private static void setChannel(TextChannel channel) {
        CommandVote.channel = channel;
    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.ADMINISTRATOR;
    }

    public static class Poll implements Serializable {

        private static final long serialVersionUID = -4410308390500314827L;
        private final String creator;
        private final String heading;
        private final List<String> answers;
        private final List<String> emojis;
        private final String message;
        private final HashMap<String, Integer> votes;
        private final boolean secret;

        public Poll(Member creator, String heading, List<String> answers, List<String> emojis, Message message, boolean secret) {
            this.creator = creator.getUser().getId();
            this.heading = heading;
            this.answers = answers;
            this.emojis = emojis;
            this.votes = new HashMap<>();
            this.message = message.getId();
            this.secret = secret;
        }

        private Member getCreator(Guild guild) {
            Member[] member = new Member[1];
            guild.retrieveMemberById(creator).queue(m -> member[0] = m);
            return member[0];
        }

        public Message getMessage(Guild guild) {
            List<Message> messages = new ArrayList<>();
            guild.getTextChannels().forEach(c -> {
                try {
                    messages.add(c.retrieveMessageById(message).complete());
                } catch (Exception ignored) {
                    /* Ignored */
                }
            });
            return messages.isEmpty() ? null : messages.get(0);
        }

    }
}

