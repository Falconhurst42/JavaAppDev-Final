package dartsApp;

/**
 * Abstract class to represent generic games of darts
 * @author ethan
 *
 */
public abstract class Game {
	protected static final String 
		SAVED_GAME_INFO_FILE_NAME = "SavedGameInfo.json",
		BASE_ARRAY_NAME = "game types",
		PLAYER_ID_ARRAY = "player_ids",
		SCORE_ARRAY = "player_scores",
		DART_COUNT_ARRAY = "player_dart_counts",
		WINNER_ID = "winner_id";
	protected GameInfo info;
	protected byte
		turn_num = 0,
		player_num = 0;
	enum GameEvent {
		GAMEOVER,
		PLAYERBUSTED,
		INVALIDSCORE
	}

	/**
	 * Creates a new Game with the given number of temporary players. 
	 * @param player_count The number of players
	 * TODO: Don't save GameInfo for these players (id = -1;)
	 */
	public Game(byte player_count) {
		// create temp players
		User[] temp_players = new User[player_count];
		for(int i = 0; i < player_count; i++) {
			temp_players[i] = new User(i+1);
		}
		
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
	 * Adds the given turn score to the GameInfo if rules allow
	 * Also updates turn and dart number according to the game's rules
	 * @param score The turn score to be added
	 * @return Returns whether additional action needs to be taken (game ended, player fouled, etc)
	 * TODO: handle dart count in special cases
	 */
	public abstract GameEvent addScore(short score);
	
	/**
	 * Saves the data of this game to a json/xml file
	 * @return
	 */
	public abstract String saveData();
	
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
	 * @return Returns the player who is up next
	 */
	public User getCurrentPlayer() {
		return info.getPlayers().get(player_num);
	}

}
