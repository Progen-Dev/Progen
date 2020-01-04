package de.progen_bot.commands.fun;

import de.progen_bot.command.CommandHandler;
import de.progen_bot.command.CommandManager.ParsedCommandString;
import de.progen_bot.core.Main;
import de.progen_bot.db.dao.connectfour.ConnectFourDaoImpl;
import de.progen_bot.db.entities.GameData;
import de.progen_bot.db.entities.config.GuildConfiguration;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;


public class ConnectFour extends CommandHandler {
    public ConnectFour() {
        super("cf", "cf<height><width>", "Progen's mini game. Play with a friend. Use this command with: <prefix> cf <height><Width><user without @>");
    }

    @Override
    public void execute(ParsedCommandString parsedCommand, MessageReceivedEvent event, GuildConfiguration configuration) {
        JDA jda = Main.getJda();
        String[] args = parsedCommand.getArgs();

        if (args.length < 3) {

            event.getTextChannel().sendMessage(super.messageGenerators.generateErrorMsgWrongInput()).queue();
            return;
        }

        int width = Integer.parseInt(args[0]);
        int heigh = Integer.parseInt(args[1]);

        User challenger = event.getAuthor();
        String opponent = args[2];

        if (event.getGuild().getMembersByName(opponent, true).size() == 0) {
            event.getTextChannel()
                    .sendMessage(
                            super.messageGenerators.generateErrorMsgWrongInput())
                    .queue();
            return;
        }
        if (event.getGuild().getMembersByName(opponent, true).get(0).getOnlineStatus() != OnlineStatus.ONLINE) {
            event.getTextChannel()
                    .sendMessage(super.messageGenerators.generateErrorMsg("User not Online")).queue();
            return;
        }

        if (width > 6 && width < 9) {
            if (heigh == width - 1) {
                if (!opponent.toLowerCase().equals(challenger.getName().toLowerCase())) {
                    GameData gameData = new GameData();
                    gameData.setOpponentId(event.getGuild().getMembersByName(opponent, true).get(0).getUser().getId());
                    gameData.setChallengerId(challenger.getId());
                    gameData.setHeigh(heigh);
                    gameData.setWidth(width);
                    gameData.setChannel(event.getChannel().getId());

                    jda.getUsersByName(opponent, true).get(0).openPrivateChannel().queue(privateChannel -> {
                        privateChannel.sendMessage(challenger.getName() + " hat dich zu einer Runde Vier-Gewinnt("
                                + heigh + "x" + width + ") herausgefordert!").queue(msg -> {
                            msg.addReaction("✅").queue();
                            msg.addReaction("❌").queue();
                            gameData.setMessageId(msg.getId());
                            new ConnectFourDaoImpl().insertGameData(gameData);
                        });
                    });
                } else {
                    event.getTextChannel()
                            .sendMessage(super.messageGenerators.generateErrorMsg("You can't challenge yourself!"))
                            .queue();
                    return;
                }
            } else {
                event.getTextChannel()
                        .sendMessage(super.messageGenerators.generateErrorMsg("The Height must be 1 smaller then the width."))
                        .queue();
                return;
            }
        } else {
            event.getTextChannel().sendMessage(super.messageGenerators.generateErrorMsg("The width has to be between 7 and 8."))
                    .queue();
            return;
        }
    }

    @Override
    public String help() {
        return null;
    }

}
