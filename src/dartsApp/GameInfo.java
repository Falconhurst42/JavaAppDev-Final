package dartsApp;

import java.util.ArrayList;
import java.util.Arrays;

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
	private ArrayList<Byte> total_scores = new ArrayList<Byte>();
	private ArrayList<ArrayList<Byte>> dart_scores = new ArrayList<ArrayList<Byte>>();
	private User winner = null;

	/**
	 * Initialize a new GameInfo object with the given Users as players
	 * @param players The players for this GameInfo
	 */
	public GameInfo(User[] players) {
		_players = new ArrayList<User>(Arrays.asList(players));
	}
	
	/**
	 * Get the total scores for the players
	 * @return Returns an ArrayList containing the player's total scores
	 */
	public ArrayList<Byte> getTotalScores() {
		return total_scores;
	}

	/**
	 * Get the dart scores for the players
	 * @return Returns an ArrayList containing the player's dart scores
	 */
	public ArrayList<ArrayList<Byte>> getDartScores() {
		return dart_scores;
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

}
