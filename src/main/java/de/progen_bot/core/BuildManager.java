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
        builder.addEventListeners(new CommandManager());
        builder.addEventListeners(new ReadyListener());
        builder.addEventListeners(new XPListener());
        builder.addEventListeners(new VotingListener());
        builder.addEventListeners(new Autochannel());
        builder.addEventListeners(new PrivateVoice());
        builder.addEventListeners(new FourConnectListener());
        builder.addEventListeners(new MusicListener());
        builder.addEventListeners(new ServerJoinListener());
        builder.addEventListeners(new ReconnectListener());
        builder.addEventListeners(new MuteHandler());
        builder.addEventListeners(new GuildJoinReloadListener());
        builder.addEventListeners(new ReactionListener());
        builder.addEventListeners(new ReconnectListener());
    }
}