package de.progen_bot.db.entities;

public class MessageData {

    private String welcomeMessage;
    private String leaveMessage;

    private String welcomeMessageChannel;
    private String leaveMessageChannel;

    MessageData(String welcomeMessage, String leaveMessage, String welcomeMessageChannel, String leaveMessageChannel){
        this.welcomeMessage = welcomeMessage;
        this.leaveMessage = leaveMessage;

        this.welcomeMessageChannel = welcomeMessageChannel;
        this.leaveMessageChannel = leaveMessageChannel;
    }

    public String getWelcomeMessage(){return welcomeMessage;}
    public void setWelcomeMessage(){this.welcomeMessage = welcomeMessage;}

    public String getLeaveMessage(){return leaveMessage;}
    public void setLeaveMessage(){this.leaveMessage = leaveMessage;}

    public String getWelcomeMessageChannel(){return welcomeMessageChannel;}
    public void setWelcomeMessageChannel(){this.leaveMessageChannel = leaveMessageChannel;}

    public String getLeaveMessageChannel(){return welcomeMessageChannel;}
    public void setLeaveMessageChannel(){this.leaveMessageChannel = leaveMessageChannel;}
}
