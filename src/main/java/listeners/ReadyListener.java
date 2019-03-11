package listeners;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * The listener interface for receiving ready events.
 * The class that is interested in processing a ready
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addReadyListener<code> method. When
 * the ready event occurs, that object's appropriate
 * method is invoked.
 *
 * @see ReadyEvent
 */
public class ReadyListener extends ListenerAdapter {

	/* (non-Javadoc)
	 * @see net.dv8tion.jda.core.hooks.ListenerAdapter#onReady(net.dv8tion.jda.core.events.ReadyEvent)
	 */
	public void onReady(ReadyEvent event) {

		String out = "\nProgen läuft auf:\n" + "----------------------------------\n";

		for (Guild g : event.getJDA().getGuilds()) {
			out += "-" + g.getName() + "(" + g.getId() + ")" +  "\n";
		}

		Game[] games = new Game[] {

				Game.playing("New music feature"),
				Game.watching("new Website: http://progen-bot.de/"),
				Game.playing("Music: pb!help music"),
				Game.playing("pb!help"),
				Game.playing("Version:1.3")};

		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				event.getJDA().getPresence().setGame(games[ThreadLocalRandom.current().nextInt(5)]);
			}

		}, 0, 20000);

		System.out.println(out);
	}
}