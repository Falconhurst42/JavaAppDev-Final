package dartsApp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
	private ArrayList<Short> dart_counts = new ArrayList<Short>();
	private User winner = null;

	/**
	 * Initialize a new GameInfo object with the given Users as players
	 * @param players The players for this GameInfo
	 */
	public GameInfo(User[] players) {
		_players = new ArrayList<User>(Arrays.asList(players));
		for(int i = 0; i < _players.size(); i++) {
			total_scores.add( (short) 0 );
			dart_counts.add( (short) 0 );
		}
	}
	
	/**
	 * Initializes a GameInfo object with all parameters given (used for reading GameInfo from a file)
	 * @param players Array of players
	 * @param scores Array of scores
	 * @param dart_counts Array of dart counts 
	 * @param winner Winning player
	 */
	public GameInfo(User[] players, Short[] scores, Short[] dart_counts, User winner) {
		// copy data
		_players = new ArrayList<User>();
		Collections.addAll(_players, players);
		total_scores = new ArrayList<Short>();
		Collections.addAll(total_scores, scores);
		this.dart_counts = new ArrayList<Short>();
		Collections.addAll(this.dart_counts, dart_counts);
		this.winner = winner;
	}
	
	protected void copyGameInfo(GameInfo gi) {
		_players = gi.getPlayers();
		total_scores = gi.getTotalScores();
		dart_counts = gi.getDartCounts();
		winner = gi.getWinner();
	}
	
	/**
	 * Converts a JSONObject and a users array into a GameInfo Object and returns it
	 * @param jo JSONObject containing the game's info
	 * @param players A List of the Users in the game
	 * @return Returns a new GameInfo object 
	 */
	public static GameInfo convertJSON(JSONObject jo, List<User> players) {
		// get users
		User[] users = new User[players.size()-1];
		for(int i = 0; i < players.size()-1; i++) {
			users[i] = players.get(i);
		}
		// get scores
		Short[] scores = new Short[users.length],
				dart_counts = new Short[users.length];
		int i = 0;
		for(Object o: jo.getJSONArray(SavedDataReader.GAME_SCORE_ARRAY)) {
			scores[i++] = ((Integer) o).shortValue();
		}
		// get dart counts
		i = 0;
		for(Object o: jo.getJSONArray(SavedDataReader.GAME_DART_COUNT_ARRAY)) {
			dart_counts[i++] = ((Integer) o).shortValue();
		}
		
		
		return new GameInfo(
				users, 
				scores,
				dart_counts,
				players.get(players.size()-1));
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
	public ArrayList<Short> getDartCounts() {
		return dart_counts;
	}
	
	/**
	 * Returns the average scores for the players
	 * @returnReturns an ArrayList containing the player's average scores
	 */
	public ArrayList<Double> getAverages() {
		ArrayList<Double> ret = new ArrayList<Double>();
		
		for(int i = 0; i < _players.size(); i++) {
			ret.add(((double) total_scores.get(i)) / ((double) dart_counts.get(i)));
		}
		
		return ret;
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
}
