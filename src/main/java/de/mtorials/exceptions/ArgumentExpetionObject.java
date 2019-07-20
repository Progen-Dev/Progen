package de.mtorials.exceptions;

public class ArgumentExpetionObject {

    private String discordMessage;

    ArgumentExpetionObject(int a) {

        switch (a) {

            case 1:
                this.discordMessage = "TOO_LESS_ARGUMENTS";

            case 2:
                this.discordMessage = "TOO_MUCH_ARGUMENTS";
        }
    }

    String getDiscordMessage() {
        return this.discordMessage;
    }
}
