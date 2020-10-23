package de.progen_bot.db.entities;

/**
 * Model class to save playlist id and name.
 */
public class PlaylistData {
    private String id;
    private String name;

    public PlaylistData(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
