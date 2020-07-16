package de.mtorials.pwi.oauth2;

import com.jagrosh.jdautilities.oauth2.OAuth2Client;
import com.jagrosh.jdautilities.oauth2.Scope;
import com.jagrosh.jdautilities.oauth2.entities.impl.OAuth2ClientImpl;
import com.jagrosh.jdautilities.oauth2.session.DefaultSessionController;
import com.jagrosh.jdautilities.oauth2.state.DefaultStateController;
import de.progen_bot.core.Main;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.HashMap;

import static io.javalin.apibuilder.ApiBuilder.*;
/**
 * Thank you @ToπSenpai for your help at ask.
 * https://github.com/TopiSenpai
 */



public class Oauth2Handler {
    private static final String OK = "{\"status\":200}";
    private final Scope[] scopes;
    private final OAuth2Client oAuth2Client;
    private Main main;

    public Oauth2Handler(Main main, int port) {
        this.main = main;
        scopes = new Scope[]{Scope.IDENTIFY};

        DefaultStateController stateController = new DefaultStateController();
        DefaultSessionController sessionController = new DefaultSessionController();
        oAuth2Client = new OAuth2ClientImpl(495293590503817237L, "5HxydRvFhTK-ScSfE1qEtJThYo5D9NV2", sessionController, stateController, main.httpClient);

        Javalin.create(config -> {
            config.enableCorsForOrigin("https://pwi.progen-bot.de");
        }).routes(() ->{
            get("/oauth",    this::oauthgenerate);
            post("/login",   this::loginwithdiscord);
            path("/user", () -> {
                before("/*", this::checkDiscordLogin);
                get("/me",   this::userinfo);
            });
            path("/guilds", () -> {
                before("/*", this::guildPerms);
            });
            }).start(port);
        }

    private void oauthgenerate(Context context){
        String key = context.header("Authorization");
        if (key == null){
            
        }
    }
    private void loginwithdiscord(Context context){
    }
    private void checkDiscordLogin(Context context){
    }
    private void getGuilds(Context context){
    }
    private void guildPerms(Context context){
    }
    private void userinfo(Context context){
    }

}
