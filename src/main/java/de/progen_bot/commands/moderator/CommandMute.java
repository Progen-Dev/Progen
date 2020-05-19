package de.progen_bot.commands.moderator;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.permissions.AccessLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandMute extends CommandHandler {

    private static final File SAVE = new File("MUTE/mutes.dat");
    private static HashMap<String, String> mutes = new HashMap<>();

    public CommandMute() {
        super("mute", "mute <user/id> <reason>", "mute and unmute a user");
    }

    public static void load() {

        if (!SAVE.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(SAVE))) {
            br.lines().forEach(l -> {
                String[] split = l.replace("\n", "").split(":::");
                mutes.put(split[0], split[1]);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static HashMap<String, String> getMuted() {
        return mutes;
    }

    private void save() {
        if (!SAVE.exists()) {
            try {
                SAVE.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        } else {
            try {
                BufferedWriter br = new BufferedWriter(new FileWriter(SAVE));
                mutes.forEach((id, rs) -> {
                    try {
                        br.write(String.format("%s:::%s\n", id, rs));
                    } catch (IOException e) { e.printStackTrace(); }
                });
                br.close();
            } catch (IOException e) { e.printStackTrace(); }
        }

    }

    private void toggle(String[] args, Message msg, TextChannel tc) {

        User victim = msg.getMentionedUsers().size() > 0 ? msg.getMentionedUsers().get(0) : msg.getJDA().getUserById(args[0]);

        if (victim == null) {
            tc.sendMessage(messageGenerators.generateErrorMsg("Please enter a valid mention or user ID!")).queue();
            return;
        }

        String vicid = victim.getId();
        if (mutes.containsKey(vicid)) {
            mutes.remove(vicid);
            save();
            tc.sendMessage(messageGenerators.generateRightMsg(String.format("%s unmuted %s.", msg.getAuthor().getAsMention(), victim.getAsMention()))).queue();
        } else {
            String reason = "No reason.";
            if (args.length > 1)
                reason = String.join(" ", Arrays.asList(args).subList(1, args.length));
            mutes.put(vicid, reason);
            save();
            tc.sendMessage(new EmbedBuilder().setColor(Color.orange).setDescription(String.format("%s muted %s.\n\nReason: `%s`", msg.getAuthor().getAsMention(), victim.getAsMention(), reason)).build()).queue();
        }

    }
    @Override
    public void execute(CommandManager.ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration guildConfiguration) {
        TextChannel tc = event.getTextChannel();
        Message msg = event.getMessage();

        if (parsedCommand.getArgs().length < 1) {
            tc.sendMessage(messageGenerators.generateErrorMsg(help())).queue();
            return;
        }

        toggle(parsedCommand.getArgs(), msg, tc);

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
