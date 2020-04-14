package de.progen_bot.db.entities;

import de.progen_bot.core.Main;
import de.progen_bot.db.dao.xp.XpDaoImpl;
import de.progen_bot.util.Level;
import net.dv8tion.jda.api.entities.User;

/**
 * The Class UserData.
 */
public class UserData {

    /**
     * The id.
     */
    private int id;

    /**
     * The user id.
     */
    private String userId;

    /**
     * The total xp.
     */
    private long totalXp = 0;

    /**
     * The level.
     */
    private int level = 0;

    /**
     * The lvlupNotify
     */
    private boolean lvlupNotify = false;

    /**
     * Gets the id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the user id.
     *
     * @return the user id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Gets the total xp.
     *
     * @return the total xp
     */
    public long getTotalXp() {
        return totalXp;
    }

    /**
     * Gets the level.
     *
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Gets the lvlupNotify.
     *
     * @return the lvlupNotify
     */
    public boolean getLvlUpNotify() {
        return lvlupNotify;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setDBTotalXp(long totalXp) {
        this.totalXp = totalXp;
    }

    public void setTotalXp(long totalXp) {
        this.totalXp = totalXp;

        setLevel(Level.calcLevel(totalXp));
    }

    public void setDBLevel(int level) {
        this.level = level;
    }

    private void setLevel(int level) {
        if (level > this.level) {
            if (this.lvlupNotify) {
                try {
                    final User user = Main.getJda().getUserById(this.userId);
                    if (user != null)
                        user.openPrivateChannel().queue(channel ->
                                channel.sendMessage("Herzlichen Glückwunsch, du bist nun Level " + level + "! :tada: ").queue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            this.level = level;
        }
    }

    public void setLvlUpNotify(boolean status) {
        this.lvlupNotify = status;
    }

    public void save(UserData data) {
        new XpDaoImpl().saveUserData(data);
    }

    public static UserData fromId(String id) {
        return new XpDaoImpl().loadFromId(id);
    }
}
