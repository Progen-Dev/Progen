package de.mtorials.fortnite.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.mtorials.fortnite.exeptions.UserNotFoundException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class Fortnite {

    static final OkHttpClient httpClient = new OkHttpClient();

    public User getUserByName(String name) {

        Request request = new Request.Builder()
                .url("https://fortnite-public-api.theapinetwork.com/prod09/users/id?username=" + name)
                .build();

        String resp;

        try {

            Response response = httpClient.newCall(request).execute();
            if (response.body() == null)
                throw new IllegalStateException("Response body is null");
            resp = response.body().string();
            return new ObjectMapper().readValue(resp, User.class);

        } catch (IOException e) {
            e.printStackTrace();
            throw new UserNotFoundException();
        }
    }
}
