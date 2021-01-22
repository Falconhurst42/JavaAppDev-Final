package dartsApp;
import java.util.ArrayList;

import org.json.JSONObject;

/**
 * A derived version of the Game class representing a classic game of darts
 * Players are aiming to reach a specific score (301 by default)
 * The first to score exactly the target wins
 */
public class ClassicDarts extends Game {
	private final static byte DARTS_PER_TURN = 3;
	private final static short DEF_TARGET_SCORE = 301;
	public final static String JSON_ARRAY_NAME = "classic darts";
	private final static String GAME_PLAYED_TO = "played to";
	private final short TARGET_SCORE;

	/**
	 * Creates a new Game with the given number of temporary players and a default target score (301) 
	 * @param player_count The number of players
	 */
	public ClassicDarts(byte player_count) {
		super(player_count);
		TARGET_SCORE = DEF_TARGET_SCORE;
	}

	/**
	 * Creates a new Game with the given players and a default target score (301) 
	 * @param players Players of this game
	 */
	public ClassicDarts(User[] players) {
		super(players);
		TARGET_SCORE = DEF_TARGET_SCORE;
	}
	
	/**
	 * Creates a new Game with the given number of temporary players and the given target score 
	 * @param player_count The number of players
	 * @param target_score The target score for this game
	 */
	public ClassicDarts(byte player_count, short target_score) {
		super(player_count);
		TARGET_SCORE = target_score;
	}

	/**
	 * Creates a new Game with the given players and the given target score 
	 * @param players Players of this game
	 * @param target_score The target score for this game
	 */
	public ClassicDarts(User[] players, short target_score) {
		super(players);
		TARGET_SCORE = target_score;
	}

	@Override
	public GameEvent addScore(short score) {
		GameEvent ret = null;
		
		// (naively) check for valid score
		if(score < 0 || score > 180) {
			ret = GameEvent.INVALIDSCORE;
		}
		else {
			// check for bust
			if(info.getTotalScores().get(player_num)+score > TARGET_SCORE) {
				// return that bust occurred
				ret = GameEvent.PLAYERBUSTED;
			}
			// otherwise add score
			else {
				info.getTotalScores().set(
						(int) player_num, 
						(short) (info.getTotalScores().get(player_num)+score)
				);
				// check for winner
				if(info.getTotalScores().get(player_num) == TARGET_SCORE) {
					// set winner and return that the game is over
					info.setWinner(info.getPlayers().get(player_num));
					ret = GameEvent.GAMEOVER;
				}
			}
			
			// update dart_count and turn nums
			// TODO: naively always adds three darts
			info.getDartCounts().set(
					(int) player_num, 
					(byte) (info.getDartCounts().get(player_num) + DARTS_PER_TURN)
			);
		}
		// increment player
		player_num++;
		// check for turn increment
		if(player_num >= info.getPlayers().size()) {
			turn_num++;
			player_num = 0;
		}		
		
		return ret;
	}

	@Override
	public void saveData() {
		// create object
		JSONObject game_info = new JSONObject();
		// add ids
		ArrayList<Integer> ids = new ArrayList<Integer>();
		info.getPlayers().forEach(u -> ids.add(u.getID()));
		game_info.put(SavedDataReader.GAME_PLAYER_ID_ARRAY, ids.toArray());
		// add scores
		game_info.put(SavedDataReader.GAME_SCORE_ARRAY, info.getTotalScores().toArray());
		// add dart counts
		game_info.put(SavedDataReader.GAME_DART_COUNT_ARRAY, info.getDartCounts().toArray());
		// add winner
		game_info.put(SavedDataReader.GAME_WINNER_ID, info.getWinner().getID());
		// add target score
		game_info.put(GAME_PLAYED_TO, TARGET_SCORE);
		
		SavedDataReader.appendGameData(game_info, this.getClass());
	}

}
