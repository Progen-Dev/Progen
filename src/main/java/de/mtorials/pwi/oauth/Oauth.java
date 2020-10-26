package de.mtorials.pwi.oauth;

import com.jagrosh.jdautilities.oauth2.OAuth2Client;
import com.jagrosh.jdautilities.oauth2.Scope;

import de.progen_bot.core.Main;
import io.javalin.Javalin;
import io.javalin.http.Context;


import static io.javalin.apibuilder.ApiBuilder.*;



public class Oauth {

    /**
     * Scopes needed for the web interface.
     * IDENTIFY: Allows /users/@me without email
     * GUILDS: Allows /users/@me/guilds to return basic information about all of a user's guilds
     */
    private static final Scope[] SCOPES = {Scope.IDENTIFY, Scope.GUILDS};
    private static final OAuth2Client CLIENT = new OAuth2Client.Builder()
        .setClientId(12345L)
        .setClientSecret("_abcd123345")
        .setOkHttpClient(Main.geHttpClient())
        .build();

    public Oauth(){
        Javalin.create(settings -> 
        settings.enableCorsForOrigin("https://pwi-canary.progen-bot.de")).routes(() -> {
            get("/login", this::loginWithDiscord);
            
        });
    }

    /**
     * User Authorization for your Login with your discord account.
     * Context redirect to authurl
     * CLIENT generate Authorization url with the base url https://pwi-canary.progen-bot.de for you
     * @param ctx
     */
    private void loginWithDiscord(Context ctx){
        var key = ctx.header("Authorization");
        if(key == null){
        ctx.redirect(CLIENT.generateAuthorizationURL("https://pwi-canary.progen-bot.de", SCOPES));
        } else{
            ctx.redirect("https://pwi-canary.progen-bot.de");
        }
    }
 

    /**
     * After login all guilds which you and Progen are listed on /guilds
     * The information of the guilds is taken from the Scope IDENTIFY.
     * @param ctx
     */
}
 


