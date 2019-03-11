package db;

public class GameData {
	private String messageId;
	private String opponentId;
	private String challengerId;
	private String channelId;
	
	private int heigh;
	private int width;

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getOpponentId() {
		return opponentId;
	}

	public void setOpponentId(String opponentId) {
		this.opponentId = opponentId;
	}

	public String getChallengerId() {
		return challengerId;
	}

	public void setChallengerId(String challengerId) {
		this.challengerId = challengerId;
	}

	public int getHeigh() {
		return heigh;
	}

	public void setHeigh(int heigh) {
		this.heigh = heigh;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setChannel(String id) {
		this.channelId = id;
	}
	
	public String getChannelId() {
		return channelId;
	}

}
