package dartsApp;

/**
 * Abstract class to represent generic games of darts
 * @author ethan
 *
 */
public abstract class Game {
	protected GameInfo info;
	protected byte
		turn_num = 0,
		player_num = 0;
	enum GameEvent {
		GAMEOVER,
		PLAYERBUSTED
	}

	/**
	 * Creates a new Game with the given number of temporary players. 
	 * @param player_count The number of players
	 * TODO: Don't save GameInfo for these players (id = -1;)
	 */
	public Game(byte player_count) {
		// create temp players
		User[] temp_players = {
				new User(true),
				new User(true),
				new User(true),
				new User(true)
		};
		// create game info
		info = new GameInfo(temp_players);
	}
	
	/**
	 * Creates a new Game with the given players
	 * @param players Players of this game
	 */
	public Game(User[] players) {
		info = new GameInfo(players);
	}
	
	/**
	 * Adds the given dart score to the GameInfo if rules allow
	 * Also updates turn and dart number according to the game's rules
	 * @param score The dart score to be added
	 * @return Returns whether additional action needs to be taken (game ended, player fouled, etc)
	 * TODO: change boolean return to some sort of enum (GAMEOVER, PLAYERBUST, etc)
	 */
	public abstract GameEvent addScore(byte score);

	/**
	 * 
	 * @return Returns the GameInfo associated with this Game
	 * 
	 * TODO: change to return immutable version of GameInfo
	 */
	public GameInfo getInfo() {
		return info;
	}

	/**
	 * 
	 * @return Returns the current turn number
	 */
	public byte getTurnNum() {
		return turn_num;
	}

	/**
	 * 
	 * @return Returns the current player number
	 */
	public byte getPlayerNum() {
		return player_num;
	}
	
	/**
	 * Get the player whose turn it is
	 * @return REturns the player who is up next
	 */
	public User getCurrentPlayer() {
		return info.getPlayers().get(player_num);
	}
	
	/**
	 * Gets the given name of the player of the given number in the players array
	 */

}
