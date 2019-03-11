package game;

import core.Main;
import db.MySQL;
import net.dv8tion.jda.core.entities.Message;
import util.Util;

/**
 * The Class ConnectFourModel.
 */
public class ConnectFourModel {

	private boolean gameOver = false;

	/** The board. */
	private char[][] board;

	/** The player 1. */
	private String player1;

	/** The player 2. */
	private String player2;

	/** The counter. */
	private int counter;

	/** The marker. */
	private char marker;

	/** The act player. */
	private String actPlayer;

	private String msgId;

	/**
	 * Instantiates a new connect four model.
	 *
	 * @param width   the width
	 * @param height  the height
	 * @param player1 the player 1
	 * @param player2 the player 2
	 */
	public ConnectFourModel(int width, int height, String player1, String player2) {
		this.board = new char[height][width];
		this.player1 = player1;
		this.player2 = player2;
	}

	/**
	 * Instantiates a new connect four model.
	 *
	 * @param board     the board
	 * @param width     the width
	 * @param height    the height
	 * @param player1   the player 1
	 * @param player2   the player 2
	 * @param actPlayer the act player
	 * @param msgId    the msg id
	 */
	public ConnectFourModel(char[][] board, int width, int height, String player1, String player2, String actPlayer,
			String msgId, boolean gameOver, int counter) {
		this.board = board;
		this.actPlayer = actPlayer;
		this.player1 = player1;
		this.player2 = player2;
		this.msgId = msgId;
		this.gameOver = gameOver;
		this.counter = counter;
	}

	/**
	 * Next player move.
	 *
	 * @param player the player
	 * @return true, if successful
	 */
	public boolean nextPlayerMove(String player) {
		actPlayer = (counter % 2 == 0) ? player1 : player2;
		return actPlayer.equals(player);
	}
	
	public String nextPlayerMoveColor() {
		return (counter % 2 == 0) ? ":red_circle:" : ":large_blue_circle:";
	}

	/**
	 * Sets the field.
	 *
	 * @param column the new field
	 */
	public void setField(int column) {
		marker = (counter % 2 == 0) ? 'x' : 'o';
		
		counter++;
		column--;

		if (board[0][column] != '\0') {
			counter--;
		} else {
			for (int i = 0; i < board.length; i++) {
				if (i + 1 == board.length) {
					if (board[i][column] == '\0') {
						board[i][column] = marker;
						if (isGameOver(column, i)) {
							gameOver = true;
						}
						break;
					}
				} else {
					if (board[i + 1][column] != '\0') {
						board[i][column] = marker;
						if (isGameOver(column, i)) {
							gameOver = true;
						}
						break;
					}
				}
			}
		}
	}

	/**
	 * Checks if is game over.
	 *
	 * @param column the column
	 * @param row    the row
	 * @return true, if is game over
	 */
	public boolean isGameOver(int column, int row) {
		boolean b1 = gameIsOver_row(column, row);
		boolean b2 = gameIsOver_column(column, row);
		boolean b3 = gameIsOver_straight1(column, row);
		boolean b4 = gameIsOver_straight2(column, row);

		return (b1 || b2 || b3 || b4);
	}

	/**
	 * Game is over straight 2.
	 *
	 * @param column the column
	 * @param row    the row
	 * @return true, if successful
	 */
	private boolean gameIsOver_straight2(int column, int row) {

		int startingPoint1 = row - 1;
		int startingPoint2 = column + 1;
		int i = 1;
		while (startingPoint1 >= 0 && startingPoint2 < board.length) {
			if (board[startingPoint2][startingPoint1] != marker)
				break;
			startingPoint1--;
			startingPoint2++;
			i++;
		}

		startingPoint1 = row + 1;
		startingPoint2 = column - 1;
		while (startingPoint1 < board[0].length && startingPoint2 >= 0) {
			if (board[startingPoint2][startingPoint1] != marker)
				break;
			startingPoint1++;
			startingPoint2--;
			i++;
		}
		return i > 3;
	}

	/**
	 * Game is over straight 1.
	 *
	 * @param column the column
	 * @param row    the row
	 * @return true, if successful
	 */
	private boolean gameIsOver_straight1(int column, int row) {

		int startingPoint = row - 1;
		int startingPoint2 = column - 1;
		int i = 1;
		while (startingPoint >= 0 && startingPoint2 >= 0) {
			if (board[startingPoint2][startingPoint] != marker)
				break;
			startingPoint--;
			startingPoint2--;
			i++;
		}

		startingPoint = row + 1;
		startingPoint2 = column + 1;
		while (startingPoint < board[0].length && startingPoint2 < board.length) {
			if (board[startingPoint2][startingPoint] != marker)
				break;
			startingPoint++;
			startingPoint2++;
			i++;
		}
		return i > 3;
	}

	/**
	 * Game is over column.
	 *
	 * @param column the column
	 * @param row    the row
	 * @return true, if successful
	 */
	private boolean gameIsOver_column(int column, int row) {
		int startPoint = column - 1;
		int points = 1;
		while (startPoint >= 0) {
			if (board[row][startPoint] != marker) {
				break;
			}
			startPoint--;
			points++;
		}

		startPoint = column + 1;
		while (startPoint < board[0].length) {
			if (board[row][startPoint] != marker) {
				break;
			}
			startPoint++;
			points++;
		}
		return points > 3;
	}

	/**
	 * Game is over row.
	 *
	 * @param column the column
	 * @param row    the row
	 * @return true, if successful
	 */
	private boolean gameIsOver_row(int column, int row) {
		int startPoint = row - 1;
		int points = 1;
		while (startPoint >= 0) {
			if (board[startPoint][column] != marker) {
				break;
			}
			startPoint--;
			points++;
		}

		startPoint = row + 1;
		while (startPoint < board.length) {
			if (board[startPoint][column] != marker) {
				break;
			}
			startPoint++;
			points++;
		}
		return points > 3;
	}

	/**
	 * Update board.
	 *
	 * @param message the message
	 */
	public void updateBoard(Message message) {
		String output = Main.getJda().getUserById(getActPlayer()).getAsMention()+ " " + nextPlayerMoveColor() + " ist am Zug!" + "\n";
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				switch (board[i][j]) {
				case '\0':
					output += ":white_circle:";
					break;
				case 'x':
					output += ":red_circle:";
					break;
				case 'o':
					output += ":large_blue_circle:";
					break;
				}
			}
			output += "\n";	
		}
		message.editMessage(Util.addTableNumbers(output,board[0].length)).queue();
		MySQL.insertConnectFourData(this);
	}

	/**
	 * Gets the board.
	 *
	 * @return the board
	 */
	public char[][] getBoard() {
		return board;
	}

	/**
	 * Gets the act player.
	 *
	 * @return the act player
	 */
	public String getActPlayer() {
		return actPlayer = (counter % 2 == 0) ? player1 : player2;
	}

	public String getPlayer1() {
		return player1;
	}

	public String getPlayer2() {
		return player2;
	}

	public int getCounter() {
		return counter;
	}

	public char getMarker() {
		return marker;
	}

	public void setBoard(char[][] board) {
		this.board = board;
	}

	public void setPlayer1(String player1) {
		this.player1 = player1;
	}

	public void setPlayer2(String player2) {
		this.player2 = player2;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public void setMarker(char marker) {
		this.marker = marker;
	}

	public void setActPlayer(String actPlayer) {
		this.actPlayer = actPlayer;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public boolean isGameOver() {
		return gameOver;
	}

}
