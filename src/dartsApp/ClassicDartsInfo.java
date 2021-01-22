package dartsApp;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class ClassicDartsInfo extends GameInfo {
	private final short TARGET_SCORE;

	public ClassicDartsInfo(GameInfo gi, short T_S) {
		super(new User[] {});		
		copyGameInfo(gi);
		TARGET_SCORE = T_S;
	}

	public short getTargetScore() {
		return TARGET_SCORE;
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
				(short) jo.getInt(ClassicDarts.GAME_PLAYED_TO)
		);
	}

}
