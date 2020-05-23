package de.mtorials.exceptions;

public class ArgumentExceptionObject {

    private final String discordMessage;

    ArgumentExceptionObject(int a) {

        switch (a) {

            case 1:
                this.discordMessage = "TOO_LESS_ARGUMENTS";
                break;
            case 2:
                this.discordMessage = "TOO_MUCH_ARGUMENTS";
                break;
            default:
                this.discordMessage = null;
        }
    }

    String getDiscordMessage() {
        return this.discordMessage;
    }
}
