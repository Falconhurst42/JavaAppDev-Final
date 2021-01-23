package dartsApp;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class ClassicDartsInfo extends GameInfo {
	private final short TARGET_SCORE;

	/**
	 * Creates a new ClassicDartsInfo which is a copy of the given GameInfo plus the given target score
	 * @param gi The GameInfo to copy from
	 * @param T_S The target score of the CDI
	 */
	public ClassicDartsInfo(GameInfo gi, short T_S) {
		super(new User[] {});		
		copyGameInfo(gi);
		TARGET_SCORE = T_S;
	}

	/**
	 * @return Returns the score that this game is being / was played to
	 */
	public short getTargetScore() {
		return TARGET_SCORE;
	}
	
	/**
	 * Returns the average scores for the players
	 * @returnReturns an ArrayList containing the player's average scores
	 */
	@Override
	public ArrayList<Double> getAverages() {
		ArrayList<Double> ret = new ArrayList<Double>();
		
		for(int i = 0; i < super.getPlayers().size(); i++) {
			ret.add(((double) (getTargetScore() - super.getTotalScores().get(i))) / ((double) super.getDartCounts().get(i)));
		}
		
		return ret;
	}
	
	/**
	 * Converts a JSONObject and a users array into a GameInfo Object and returns it
	 * @param jo JSONObject containing the game's info
	 * @param players A List of the Users in the game
	 * @return Returns a new GameInfo object 
	 */
	public static GameInfo convertJSON(JSONObject jo, List<User> players) {
		return new ClassicDartsInfo(
				GameInfo.convertJSON(jo, players), 
				(short) jo.getInt(ClassicDarts.GAME_PLAYED_FROM)
		);
	}

}
