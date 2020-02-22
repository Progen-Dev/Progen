package de.progen_bot.permissions;

public enum AccessLevel {

    USER(0), TRUSTED(1), MODERATOR(2), ADMINISTRATOR(3), OWNER(4), BOTOWNER(5);

    private final int level;

    AccessLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
