package de.progen_bot.core;

import de.progen_bot.command.CommandManager;
import de.progen_bot.listeners.*;
import net.dv8tion.jda.api.JDABuilder;

/**
 * The Class BuildManager.
 */
public class BuildManager
{
    private BuildManager()
    {
        /* Prevent instantiation */
    }

    /**
     * Adds the event de.progen_bot.listeners.
     */
    public static void addEventListeners(JDABuilder builder)
    {
        builder.addEventListeners(
                new MuteHandler(),
                new CommandManager(),
                new ReadyListener(),
                new XPListener(),
                new VotingListener(),
                new Autochannel(),
                new PrivateVoice(),
                new FourConnectListener(),
                new MusicListener(),
                new ReconnectListener(),
                new GuildJoinReloadListener(),
                new ReactionListener(),
                new AutoroleListener(),
                new SayListener(),
                new StarBoardListener(),
                new GuildJoinListener(),
                new MessageListener()
        );
    }
}
