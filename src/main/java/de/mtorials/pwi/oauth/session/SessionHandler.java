package de.mtorials.pwi.oauth.session;

import java.util.HashMap;

import com.jagrosh.jdautilities.oauth2.session.Session;

public class SessionHandler{
    private static HashMap<String, Session> SESSION;
    private static HashMap<String, Boolean> USER;

 public void createSession(SessionManager session){
     SESSION.put(session.getSessionKey(), session);
     USER.put(session.getUserId(), true);
 }   
}