package de.mtorials.exceptions;

public class ArgumentExeption extends RuntimeException {

    public static final int TOO_LESS_ARGS = 1;
    public static final int TOO_MUCH_ARGS = 1;

    private ArgumentExpetionObject expetionObject;

    ArgumentExeption(int i) {

        this.expetionObject = new ArgumentExpetionObject(i);
    }

    public String getDiscordMessage() {

        return this.expetionObject.getDiscordMessage();
    }
}
