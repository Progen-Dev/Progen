package listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import db.UserData;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import util.Settings;
import util.Statics;

/**
 * The listener interface for receiving xp events. The class that is interested
 * in processing a xp event implements this interface, and the object created
 * with that class is registered with a component using the component's
 * <code>addXpListener<code> method. When the xp event occurs, that object's
 * appropriate method is invoked.
 *
 *
 */
public class XPListener extends ListenerAdapter {
	private List<String> spamFilter = new ArrayList<>();

	/**
	 * On guild message received.
	 *
	 * @param event
	 *            the event
	 */
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) {
			return;
		}

		if (event.getMessage().getContentRaw().startsWith(Settings.PREFIX)) {
			return;
		}
		
		if(spamFilter.contains(event.getAuthor().getId())) {
			return;
		}

		UserData data = UserData.fromId(event.getAuthor().getId());

		if (data == null) {
			data = new UserData();
			data.setUserId(event.getAuthor().getId());
		}

		data.setTotalXp(data.getTotalXp() + Statics.XP_GAIN);
		data.save(data);

		spamFilter.add(event.getAuthor().getId());
		//only 1 Message every 60s counts
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				spamFilter.remove(event.getAuthor().getId());
			}
		}, 1000 * 10);
	}
}
