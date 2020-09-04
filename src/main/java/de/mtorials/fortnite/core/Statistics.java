package de.mtorials.fortnite.core;

import com.fasterxml.jackson.databind.JsonNode;
import de.mtorials.fortnite.exeptions.NotEnoughDetailsException;

public class Statistics {

    private final User user;
    private final int overallScore;
    private final int overallPlaceTop1;
    private final int overallKills;
    private final int overallMatchesPlayed;

    Statistics(User user, JsonNode jsonNode) {

        this.user = user;

        try {

            this.overallScore = jsonNode.get("overallData").get("defaultModes").get("score").asInt();
            this.overallMatchesPlayed = jsonNode.get("overallData").get("defaultModes").get("matchesplayed").asInt();

        } catch (NullPointerException e) {

            e.printStackTrace();
            throw new NotEnoughDetailsException();
        }

        if (jsonNode.get("overallData").get("defaultModes").get("kills") != null)
            this.overallKills = jsonNode.get("overallData").get("defaultModes").get("kills").asInt();
        else this.overallKills = 0;

        if (jsonNode.get("overallData").get("defaultModes").get("placetop1") != null)
            this.overallPlaceTop1 = jsonNode.get("overallData").get("defaultModes").get("placetop1").asInt();
        else this.overallPlaceTop1 = 0;
    }

    public int getOverallScore() {

        return this.overallScore;
    }

    public User getUser() {
        return this.user;
    }

    public int getOverallPlaceTop1() {
        return overallPlaceTop1;
    }

    public int getOverallKills() {
        return overallKills;
    }

    public int getOverallMatchesPlayed() {
        return overallMatchesPlayed;
    }
}
