package dartsApp;

import org.json.JSONObject;

/**
 * Class to represent players in the darts application
 * @author ethan
 *
 */
public class User {
	private final int _id;
	private int _wins, _losses, _dart_count;
	private double _average;
	private String _name;

	/**
	 * Creates a new User with a generic name
	 */
	public User() {
		_id = SavedDataReader.getNextID();
		SavedDataReader.incrementNextUserID();
		_name = String.format("User #%d", _id);
		_average = _dart_count = _wins = _losses = 0;
	}

	/**
	 * Creates a user with the given name
	 * @param name The User's name
	 */
	public User(String name) {
		_id = SavedDataReader.getNextID();
		SavedDataReader.incrementNextUserID();
		setName(name);
		_average = _dart_count = _wins = _losses = 0;
	}

	/**
	 * Creates a new temporary User with the given player number
	 */
	public User(int player_num) {
		_id = -Math.abs(player_num);
		_name = String.format("Guest User #%d", Math.abs(player_num));
	}

	/**
	 * Creates a User with the given id, name, wins, losses, dart count, and average
	 * @param id Id to be
	 * @param name Name to be
	 */
	public User(int id, String name, int wins, int losses, int dart_count, double average) {
		_id = id;
		setName(name);
		_wins = wins;
		_losses = losses;
		_dart_count = dart_count;
		_average = average;
	}
	
	@Override
	public String toString() {
		return String.format(
			"\"%s\" (id: %d)\nWin/Loss: %d/%d\nTotal Darts: %d\nAverage Score: %.2f",
			_name,
			_id,
			_wins,
			_losses,
			_dart_count,
			_average
		);
	}
	
	/**
	 * 
	 * @return Returns the User's id
	 */
	public final int getID() {
		return _id;
	}

	/**
	 * 
	 * @return Returns the User's name
	 */
	public String getName() {
		return _name;
	}

	/**
	 * Sets the User's name to the given string
	 * @param name The User's name
	 */
	public void setName(String name) {
		_name = name;
	}
	
	/**
	 * 
	 * @return Returns whether or not this user is denoted as temporary
	 */
	public boolean isTemporary() {
		return _id < 0;
	}

	/**
	 * Saves this user's data to the saved data file
	 */
	public void saveData() {
		// don't save temp users
		if(!isTemporary()) {
			JSONObject user_info = new JSONObject();
			user_info.put(SavedDataReader.USER_ID, _id);
			user_info.put(SavedDataReader.USER_NAME, _name);
			user_info.put(SavedDataReader.USER_WINS, _wins);
			user_info.put(SavedDataReader.USER_LOSSES, _losses);
			user_info.put(SavedDataReader.USER_DART_COUNT, _dart_count);
			user_info.put(SavedDataReader.USER_AVERAGE, _average);
			
			SavedDataReader.saveUserData(user_info);
		}
	}

	/**
	 * @return Returns the number of games this user has won
	 */
	public int getWins() {
		return _wins;
	}

	/**
	 * @return Returns the number of games this user has lost
	 */
	public int getLosses() {
		return _losses;
	}
	
	/**
	 * Updates the user's win/loss record based on the give game result
	 * @param won A boolean representing whether the user won the game in question
	 */
	public void addResult(boolean won) {
		if(won) {
			_wins++;
		}
		else {
			_losses++;
		}
	}

	/**
	 * @return Returns this user's total dart count
	 */
	public int getDartCount() {
		return _dart_count;
	}

	/**
	 * @return Returns this user's average score
	 */
	public double getAverage() {
		return _average;
	}
	
	/**
	 * Updates the dart_count and average for this player based off the given game data
	 * @param dart_count The number of darts through in the given period
	 * @param total The total score over the given period
	 */
	public void updateStats(int dart_count, double total) {
		// update average
		_average = ((_average*((double) _dart_count)) + total) / ((double) dart_count+_dart_count);
		// update dart count
		_dart_count += dart_count;
	}
}
