package de.mtorials.exceptions;

import de.mtorials.config.LANG;

public class ArgumentExpetionObject {

    private String discordMessage;

    ArgumentExpetionObject(int a) {

        switch (a) {

            case 1:
                this.discordMessage = LANG.EXPETION_TOO_LESS_ARGS;

            case 2:
                this.discordMessage = LANG.EXPETION_TOO_MUCH_ARGS;
        }
    }

    String getDiscordMessage() {
        return this.discordMessage;
    }
}
