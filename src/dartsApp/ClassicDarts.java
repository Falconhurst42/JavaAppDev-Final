package dartsApp;

/**
 * A derived version of the Game class representing a classic game of darts
 * Players are aiming to reach a specific score (301 by default)
 * The first to score exactly the target wins
 */
public class ClassicDarts extends Game {
	private final static byte DARTS_PER_TURN = 3;
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
	public GameEvent addScore(byte score) {
		GameEvent ret = null;
		// check for bust
		if(info.getTotalScores().get(player_num)+score > TARGET_SCORE) {
			// return that bust occurred
			ret = GameEvent.PLAYERBUSTED;
		}
		// otherwise add score
		else {
			info.getTotalScores().set(
					(int) player_num, 
					(byte) (info.getTotalScores().get(player_num)+score)
			);
			// check for winner
			if(info.getTotalScores().get(score) == TARGET_SCORE) {
				// set winner and return that the game is over
				info.setWinner(info.getPlayers().get(player_num));
				ret = GameEvent.PLAYERBUSTED;
			}
		}
		
		// update dart_count and turn nums
		// TODO: naively always adds three darts
		info.getDartCounts().set(
				(int) player_num, 
				(byte) (info.getDartCounts().get(player_num) + DARTS_PER_TURN)
		);
		// increment player
		player_num++;
		// check for turn increment
		if(player_num >= info.getPlayers().size()) {
			turn_num++;
			player_num = 0;
		}		
		
		return ret;
	}

}
