package de.mtorials.fortnite.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.mtorials.fortnite.exeptions.UserNotFoundExeption;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class User {

    private String username;
    private String uid;
    private String[] seasons;
    private String[] platforms;


    public Statistics getStatistics() throws IOException {

        Request request = new Request.Builder()
                .url("https://fortnite-public-api.theapinetwork.com/prod09/users/public/br_stats_v2?user_id=" + this.uid + "&platform=pc")
                .build();

        String resp;

        try {

            Response response = Fortnite.httpClient.newCall(request).execute();
            resp = response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
            throw new UserNotFoundExeption();
        }

        System.out.println(resp);
        return new Statistics(this, new ObjectMapper().readValue(resp, JsonNode.class));
    }

    public String[] getPlatforms() {
        return platforms;
    }

    public String[] getSeasons() {
        return seasons;
    }

    public String getUsername() {
        return username;
    }

    public String getUid() {
        return this.uid;
    }
}
