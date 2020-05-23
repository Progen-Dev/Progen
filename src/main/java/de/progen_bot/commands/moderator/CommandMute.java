package de.progen_bot.commands.moderator;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.permissions.AccessLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandMute extends CommandHandler {

    private static final File SAVE = new File("MUTE/mutes.dat");
    private static final Map<String, String> MUTES = new HashMap<>();

    public CommandMute() {
        super("mute", "mute <user/id> <reason>", "mute and unmute a user");
        load();
    }

    public static void load() {

        if (!SAVE.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(SAVE))) {
            br.lines().forEach(l -> {
                String[] split = l.replace("\n", "").split(":::");
                MUTES.put(split[0], split[1]);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Map<String, String> getMuted() {
        return MUTES;
    }

    private void save() {
        try {
            SAVE.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedWriter br = new BufferedWriter(new FileWriter(SAVE))) {
            MUTES.forEach((id, rs) -> {
                try {
                    br.write(String.format("%s:::%s%n", id, rs));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void toggle(String[] args, Message msg, TextChannel tc) {

        User victim = msg.getMentionedUsers().size() > 0 ? msg.getMentionedUsers().get(0) : msg.getJDA().getUserById(args[0]);

        if (victim == null) {
            tc.sendMessage(messageGenerators.generateErrorMsg("Please enter a valid mention or user ID!")).queue();
            return;
        }

        String victimId = victim.getId();
        if (MUTES.containsKey(victimId)) {
            MUTES.remove(victimId);
            save();
            tc.sendMessage(messageGenerators.generateRightMsg(String.format("%s unmuted %s.", msg.getAuthor().getAsMention(), victim.getAsMention()))).queue();
        } else {
            String reason = "No reason.";
            if (args.length > 1)
                reason = String.join(" ", Arrays.asList(args).subList(1, args.length));
            MUTES.put(victimId, reason);
            save();
            tc.sendMessage(new EmbedBuilder().setColor(Color.orange).setDescription(String.format("%s muted %s.%n%nReason: `%s`", msg.getAuthor().getAsMention(), victim.getAsMention(), reason)).build()).queue();
        }

    }
    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration guildConfiguration) {
        TextChannel tc = event.getTextChannel();
        Message msg = event.getMessage();

        if (parsedCommand.getArgs().length < 1) {
            tc.sendMessage(messageGenerators.generateErrorMsg("Work in Progress!")).queue(); // TODO: 23.05.2020  
            return;
        }

        toggle(parsedCommand.getArgs(), msg, tc);

    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.MODERATOR;
    }
}
