package de.progen_bot.utils.level;

import java.util.stream.IntStream;

public class Level
{
    /**
     * Private constructor to prevent instantiation of utility class
     */
    private Level()
    {
        /* Prevent instantiation */
    }

    /**
     * Calculate the corresponding level from given xp
     *
     * @param totalXp total xp of user
     * @return level for given xp
     */
    public static int calcLevel(long totalXp)
    {
        int level = 0;
        boolean complete = false;

        while (!complete)
        {
            final long xp = calcLevelToTotalXp(level);
            if (totalXp < xp)
                complete = true;
            else
                level += 1;
        }

        return level;
    }

    /**
     * Calculate the corresponding total xp from given level
     *
     * @param level user level
     * @return total xp for given level
     */
    public static long calcLevelToTotalXp(int level)
    {
        return IntStream.range(0, level + 1).mapToLong(Level::xpToLevelUp).sum();
    }


    /**
     * Calculate the corresponding xp for given level
     *
     * @param level user level
     * @return xp for given level
     */
    public static long xpToLevelUp(int level)
    {
        return 10 * level + (long) 10;
    }

    /**
     * Calculate the remaining xp to level up for given total xp
     *
     * @param totalXp total xp of user
     * @return remaining xp to level up
     */
    public static long remainingXp(long totalXp)
    {
        final int level = calcLevel(totalXp);

        if (level == 0)
            return xpToLevelUp(1) - totalXp;

        final long xp = calcLevelToTotalXp(level);
        return totalXp - xp + xpToLevelUp(level);
    }
}
