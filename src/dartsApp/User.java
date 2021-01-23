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

	public static User readUser(int id) {
		// check for temp user
		if(id < 0) {
			return new User(id);
		}
		// otherwise search for user
		else {
			return null;
		}
	}

	public int getWins() {
		return _wins;
	}

	public int getLosses() {
		return _losses;
	}
	
	public void addResult(boolean won) {
		if(won) {
			_wins++;
		}
		else {
			_losses++;
		}
	}

	public int getDartCount() {
		return _dart_count;
	}

	public double getAverage() {
		return _average;
	}
	
	/**
	 * Updates the dart_count and average for this player based off the given game data
	 * @param dart_count The number of darts through in the given period
	 * @param average The average score over the given period
	 */
	public void updateStats(int dart_count, double average) {
		// update average
		_average = ((_average*((double) _dart_count)) + average) / ((double) dart_count);
		// update dart count
		_dart_count += dart_count;
	}
}
