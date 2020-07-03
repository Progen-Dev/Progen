package de.mtorials.pwi.oauth2;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jagrosh.jdautilities.oauth2.OAuth2Client;
import com.jagrosh.jdautilities.oauth2.Scope;
import com.jagrosh.jdautilities.oauth2.entities.OAuth2User;
import com.jagrosh.jdautilities.oauth2.entities.impl.OAuth2ClientImpl;
import com.jagrosh.jdautilities.oauth2.exceptions.InvalidStateException;
import com.jagrosh.jdautilities.oauth2.session.DefaultSessionController;
import com.jagrosh.jdautilities.oauth2.session.Session;
import com.jagrosh.jdautilities.oauth2.state.DefaultStateController;
import de.progen_bot.core.Main;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


/**
 * Thank you @ToπSenpai for your help at ask.
 * https://github.com/TopiSenpai
 */

public class Oauth2Handler {
  private String OK = "{\"status\":200}";

  private Main main;
  private Scope[] scopes;
  private OAuth2Client oAuth2Client;
  private String orginurl;

  public Oauth2Handler(Main main, int port){
      this.main = main;

      scopes = new Scope[]{Scope.IDENTIFY};
      DefaultSessionController sessionController = new DefaultSessionController();
      DefaultStateController stateController = new DefaultStateController();
      oAuth2Client = new OAuth2ClientImpl(495293590503817237L, "5HxydRvFhTK-ScSfE1qEtJThYo5D9NV2" ,sessionController, stateController, main.httpClient);

    URL url = null;
    try {
        url = new URL("https://discord.com");
    } catch (MalformedURLException e) {
        e.printStackTrace();
    }
    orginurl = String.format("%s://%s", url.getProtocol(),url.getHost());

  }
    private void cors(Request request, Response response) {
        List<String> accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
        if (accessControlRequestHeaders != null) {
            response.header("Access-Control-Allow-Headers", String.valueOf(accessControlRequestHeaders));
        }
        List<String> accessControlRequestMethod = request.headers("Access-Control-Request-Method");
        if (accessControlRequestMethod != null) {
            response.header("Access-Control-Allow-Methods", String.valueOf(accessControlRequestMethod));
        }
    }

    private void headers(Request request, Response response){
      response.header("AccessControlAllow", orginurl);
      response.header("AccessControlAllowMethods", "GET, POST, PATCH, PUT, DELETE, OPTIONS");
      response.header("AccessControlAllowHeaders", "Origin, Content-Type, X-Auth-Token");
    }

    private void loginwithdiscord(Request request, Response response){
      List<String> key = request.headers("Authorization");
      if (key == null){
          response.isRedirect(); oAuth2Client.generateAuthorizationURL("https://pwi.progen-bot.de", scopes);
      }else {
          response.isRedirect();
      }
    }

    private void login(Request request, Response response){
        JsonObject json;
        JsonParser parser = new JsonParser();
        json = parser.parse(String.valueOf(request.body())).getAsJsonObject();

        String code = json.get("code").getAsString();
        String state = json.get("state").getAsString();
        try {
            /**
             * connect to database generate uniquekey
             */
            Session session = oAuth2Client.startSession(code, state, String.valueOf(scopes)).complete();
            OAuth2User oAuth2User = oAuth2Client.getUser(session).complete();
            /**
             * add session to database (user.getID and key)
             */
            //return "{\"key\": " + JSONObject.quote() + "}";
        } catch (InvalidStateException e) {
            System.out.println("401");
            System.out.println("State Invalid/expired please try again");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void setSession(Session session){
      session.getAccessToken();
      session.getRefreshToken();
      session.getTokenType();
    }
}
