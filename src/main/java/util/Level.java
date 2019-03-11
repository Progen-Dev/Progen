package util;

/**
 * The Class Level.
 */
public class Level {
	
	/**
	 * Calculate the level from totalxp
	 *
	 * @param totalXp the total xp
	 * @return the int
	 */
	public static int calcLevel(long totalXp) {
		int level = 0;
		boolean complete = true;

		while (complete) {
			long xp = calcLevelToTotalXp(level);
			if (totalXp < xp) {
				complete = false;
			} else {
				level++;
			}
		}
		return level;
	}

	/**
	 * Calculate totalxp from level
	 *
	 * @param userLevel the user level
	 * @return the long
	 */
	private static long calcLevelToTotalXp(int userLevel) {
		long sumXp = 0;
		for (int level = 0; level <= userLevel; level++) {
			sumXp += xpToLevelUp(level);
		}
		return sumXp;
	}

	/**
	 * XP to level up.
	 *
	 * @param level the level
	 * @return the int
	 */
	public static int xpToLevelUp(int level) {
		return 10 * level + 10;
	}

	/**
	 * Remaining xp.
	 *
	 * @param totalXp the total xp
	 * @return the long
	 */
	public static long remainingXp(long totalXp) {
		int level = calcLevel(totalXp);

		if (level == 0) {
			return totalXp;
		}

		long xp = calcLevelToTotalXp(level);
		return totalXp - xp + xpToLevelUp(level);
	}
}
