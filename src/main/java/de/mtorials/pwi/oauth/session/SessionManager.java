package de.mtorials.pwi.oauth.session;

import java.time.OffsetDateTime;

import com.jagrosh.jdautilities.oauth2.Scope;
import com.jagrosh.jdautilities.oauth2.session.Session;
import com.jagrosh.jdautilities.oauth2.session.SessionData;

public class SessionManager implements Session {

	private final String sessionKey, accessToken, refreshToken, tokenType;
	private final Scope[] scopes;
	private String id;

	public SessionManager(String userId, SessionData sessionData) {
		this.id = userId;
		this.sessionKey = sessionData.getIdentifier();
		this.accessToken = sessionData.getAccessToken();
		this.refreshToken = sessionData.getRefreshToken();
		this.tokenType = sessionData.getTokenType();
		this.scopes = sessionData.getScopes();
	}

	public String getSessionKey() {
		return this.sessionKey;
	}

	public String getUserId() {
		return this.id;
	}

	public void setUserId(String userId) {
		this.id = userId;
	}

	@Override
	public String getAccessToken() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRefreshToken() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Scope[] getScopes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTokenType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OffsetDateTime getExpiration() {
		// TODO Auto-generated method stub
		return null;
	}
}
