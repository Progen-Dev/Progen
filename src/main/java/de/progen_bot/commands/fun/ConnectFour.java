package de.progen_bot.commands.fun;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager.ParsedCommandString;
import de.progen_bot.core.Main;
import de.progen_bot.db.dao.connectfour.ConnectFourDaoImpl;
import de.progen_bot.db.entities.GameData;
import de.progen_bot.db.entities.config.GuildConfiguration;
import de.progen_bot.permissions.AccessLevel;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ConnectFour extends CommandHandler {
    public ConnectFour() {
        super("cf", "cf 8 7 Progen",
                "Progen's mini game. Play with a friend. Use this command with: cf <width> <Name>");
    }

    @Override
    public void execute(ParsedCommandString parsedCommand, MessageReceivedEvent event,
            GuildConfiguration configuration) {
        JDA jda = Main.getJda();
        String[] args = parsedCommand.getArgs();

        if (args.length < 1) {

            event.getTextChannel().sendMessage(super.messageGenerators.generateErrorMsgWrongInput()).queue();
            return;
        }

        int width = Integer.parseInt(args[0]);

        User challenger = event.getAuthor();
        String opponent = args[1];

        if (event.getGuild().getMembersByName(opponent, true).isEmpty()) {
            event.getTextChannel().sendMessage(super.messageGenerators.generateErrorMsgWrongInput()).queue();
            return;
        }
        if (event.getGuild().getMembersByName(opponent, true).get(0).getOnlineStatus() != OnlineStatus.ONLINE) {
            event.getTextChannel().sendMessage(super.messageGenerators.generateErrorMsg("User not Online")).queue();
            return;
        }

        if (width > 6 && width < 9) {
            final int height = width - 1;
            if (!opponent.equalsIgnoreCase(challenger.getName())) {
                GameData gameData = new GameData();
                gameData.setOpponentId(event.getGuild().getMembersByName(opponent, true).get(0).getUser().getId());
                gameData.setChallengerId(challenger.getId());
                gameData.setHeight(height);
                gameData.setWidth(width);
                gameData.setChannel(event.getChannel().getId());

                jda.getUsersByName(opponent, true).get(0).openPrivateChannel()
                        .queue(privateChannel -> privateChannel
                                .sendMessage(challenger.getName() + " hat dich zu einer Runde Vier-Gewinnt(" + height
                                        + "x" + width + ") herausgefordert!")
                                .queue(msg -> {
                                    msg.addReaction("✅").queue();
                                    msg.addReaction("❌").queue();
                                    gameData.setMessageId(msg.getId());
                                    new ConnectFourDaoImpl().insertGameData(gameData);
                                }));
            } else {
                event.getTextChannel()
                        .sendMessage(super.messageGenerators.generateErrorMsg("You can't challenge yourself!")).queue();
            }
        } else {
            event.getTextChannel()
                    .sendMessage(super.messageGenerators.generateErrorMsg("The width has to be between 7 and 8."))
                    .queue();
        }
    }

    @Override
    public AccessLevel getAccessLevel() {
        return AccessLevel.USER;
    }

}
