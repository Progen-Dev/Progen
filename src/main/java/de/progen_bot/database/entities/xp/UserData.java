package de.progen_bot.database.entities.xp;

import de.progen_bot.core.Main;
import de.progen_bot.database.dao.xp.XpDaoImpl;
import de.progen_bot.utils.level.Level;
import net.dv8tion.jda.api.entities.User;

public class UserData
{

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
    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * Gets the user id.
     *
     * @return the user id
     */
    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    /**
     * Gets the total xp.
     *
     * @return the total xp
     */
    public long getTotalXp()
    {
        return totalXp;
    }

    public void setTotalXp(long totalXp)
    {
        this.totalXp = totalXp;

        setLevel(Level.calcLevel(totalXp));
    }

    /**
     * Gets the level.
     *
     * @return the level
     */
    public int getLevel()
    {
        return level;
    }

    private void setLevel(int level)
    {
        if (level > this.level)
        {
            if (this.lvlupNotify)
            {
                try
                {
                    final User user = Main.getJDA().getUserById(this.userId);
                    if (user != null)
                        user.openPrivateChannel().queue(channel ->
                                // TODO: 22.02.2021 english
                                channel.sendMessage("Herzlichen Glückwunsch, du bist nun Level " + level + "! :tada: ").queue());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            this.level = level;
        }
    }

    /**
     * Gets the lvlupNotify.
     *
     * @return the lvlupNotify
     */
    public boolean getLvlUpNotify()
    {
        return lvlupNotify;
    }

    public void setLvlUpNotify(boolean status)
    {
        this.lvlupNotify = status;
    }

    public void setDBTotalXp(long totalXp)
    {
        this.totalXp = totalXp;
    }

    public void setDBLevel(int level)
    {
        this.level = level;
    }

    public static void save(UserData data)
    {
        new XpDaoImpl().saveUserData(data);
    }

    public static UserData fromId(String id)
    {
        return new XpDaoImpl().loadFromId(id);
    }
}
