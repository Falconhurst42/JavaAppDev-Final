package dartsApp;

/**
 * Abstract class to represent generic games of darts
 * @author ethan
 *
 */
public abstract class Game {
	private GameInfo info;
	private byte
		turn_num = 0,
		dart_num = 0;

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
	 * @return Returns whether or not the game is now over
	 */
	public abstract boolean addDartScore(Byte score);

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
	 * @return Returns the current turn number (counting from 1)
	 */
	public byte getTurnNum() {
		return (byte) (turn_num + 1);
	}

	/**
	 * 
	 * @return Returns the current dart number (counting from 1)
	 */
	public byte getDartNum() {
		return (byte) (dart_num + 1);
	}

}
