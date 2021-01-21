package dartsApp;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONObject;

/**
 * Class to store the core info of a Game
 * This info is split from the rest of the Game class so it can be easily saved separately when the Game is finished
 * @author ethan
 * 
 * TODO: how do we go about updating scores/winner?
 *
 */
public class GameInfo {
	private ArrayList<User> _players;
	private ArrayList<Short> total_scores = new ArrayList<Short>();
	private ArrayList<Byte> dart_counts = new ArrayList<Byte>();
	private User winner = null;

	/**
	 * Initialize a new GameInfo object with the given Users as players
	 * @param players The players for this GameInfo
	 */
	public GameInfo(User[] players) {
		_players = new ArrayList<User>(Arrays.asList(players));
		for(int i = 0; i < _players.size(); i++) {
			total_scores.add( (short) 0 );
			dart_counts.add( (byte) 0 );
		}
	}
	
	/**
	 * Get the total scores for the players
	 * @return Returns an ArrayList containing the player's total scores
	 */
	public ArrayList<Short> getTotalScores() {
		return total_scores;
	}

	/**
	 * Get the dart scores for the players
	 * @return Returns an ArrayList containing the player's dart counts
	 */
	public ArrayList<Byte> getDartCounts() {
		return dart_counts;
	}

	/**
	 * Get the players of the GameInfo
	 * @return Returns an ArrayList containing the players
	 */
	public ArrayList<User> getPlayers() {
		return _players;
	}

	/**
	 * Get the winning player of this GameInfo
	 * @return Returns the winning player or null if there is none
	 * TODO: support multiple winners?
	 */
	public User getWinner() {
		return winner;
	}
	
	/**
	 * Sets the given User as this Game's winner
	 * @param winner The winning User
	 */
	public void setWinner(User winner) {
		this.winner = winner;
	}

	/*public static GameInfo convertJSON(JSONObject jo) {
		// TODO: do when user data saving is done
	}*/
}
