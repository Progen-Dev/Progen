package de.progen_bot.db.entities;

public class MessageBuilder {
    private String leaveMessage;
    private String welcomeMessage;

    private String welcomeMessageChannel;
    private String leaveMessageChannel;

    public MessageBuilder setWelcomeMessage(String welcomeMessage){
        this.welcomeMessage = welcomeMessage;
        return this;
    }

    public MessageBuilder setLeaveMessage(String leaveMessage){
        this.leaveMessage = leaveMessage;
        return this;
    }

    public MessageBuilder setWelcomeMessageChannel(String welcomeMessageChannel){
        this.welcomeMessageChannel = welcomeMessageChannel;
        return this;
    }

    public MessageBuilder setLeaveMessageChannel(String leaveMessageChannel){
        this.leaveMessageChannel = leaveMessageChannel;
        return this;
    }

    public MessageData build(){
        return new MessageData(this.welcomeMessage, this.leaveMessage, this.welcomeMessageChannel, this.leaveMessageChannel);
    }
}
