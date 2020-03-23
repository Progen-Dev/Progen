package de.progen_bot.core;

import de.progen_bot.command.CommandManager;
import de.progen_bot.listeners.*;
import net.dv8tion.jda.api.JDABuilder;

/**
 * The Class BuildManager.
 */
public class BuildManager {

    /**
     * The builder.
     */
    private JDABuilder builder;

    /**
     * Instantiates a new builds the manager.
     *
     * @param builder the builder
     */
    public BuildManager(JDABuilder builder) {
        this.builder = builder;
        addEventListeners();
    }

    /**
     * Adds the event de.progen_bot.listeners.
     */
    private void addEventListeners() {
        builder.addEventListeners(
                new CommandManager(),
                new ReadyListener(),
                new XPListener(),
                new VotingListener(),
                new Autochannel(),
                new PrivateVoice(),
                new FourConnectListener(),
                new MusicListener(),
                new ServerJoinListener(),
                new ReconnectListener(),
                new MuteHandler(),
                new GuildJoinReloadListener(),
                new ReactionListener()
        );
    }
}