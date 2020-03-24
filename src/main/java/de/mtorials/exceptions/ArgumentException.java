package de.mtorials.exceptions;

public class ArgumentException extends RuntimeException {

    public static final int TOO_LESS_ARGS = 1;
    public static final int TOO_MUCH_ARGS = 1;

    private final ArgumentExceptionObject exceptionObject;

    ArgumentException(int i) {

        this.exceptionObject = new ArgumentExceptionObject(i);
    }

    public String getDiscordMessage() {

        return this.exceptionObject.getDiscordMessage();
    }
}
