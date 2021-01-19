package dartsApp;

/**
 * A derived version of the Game class representing a classic game of darts
 * Players are aiming to reach a specific score (301 by default)
 * The first to score exactly the target wins
 *
 */
public class ClassicDarts extends Game {
	private final byte TARGET_SCORE;

	/**
	 * Creates a new Game with the given number of temporary players and a default target score (301) 
	 * @param player_count The number of players
	 */
	public ClassicDarts(byte player_count) {
		super(player_count);
		TARGET_SCORE = (byte)301;
	}

	/**
	 * Creates a new Game with the given players and a default target score (301) 
	 * @param players Players of this game
	 */
	public ClassicDarts(User[] players) {
		super(players);
		TARGET_SCORE = (byte)301;
	}
	
	/**
	 * Creates a new Game with the given number of temporary players and the given target score 
	 * @param player_count The number of players
	 * @param target_score The target score for this game
	 */
	public ClassicDarts(byte player_count, byte target_score) {
		super(player_count);
		TARGET_SCORE = target_score;
	}

	/**
	 * Creates a new Game with the given players and the given target score 
	 * @param players Players of this game
	 * @param target_score The target score for this game
	 */
	public ClassicDarts(User[] players, byte target_score) {
		super(players);
		TARGET_SCORE = target_score;
	}

	@Override
	public boolean addScore(byte score) {
		this.getInfo().getTotalScores().set(
				(int) this.getPlayerNum(), 
				(byte) (this.getInfo().getTotalScores().get(this.getPlayerNum())+score)
		);
		return false;
	}

}
