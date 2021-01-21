package dartsApp;

import org.json.JSONObject;

/**
 * Class to represent players in the darts application
 * @author ethan
 *
 */
public class User {
	private static int next_id = 0;
	private final int _id;
	private String _name;

	/**
	 * Creates a new User with a generic name
	 */
	public User() {
		_id = next_id++;
		_name = String.format("User #%d", _id);
	}

	/**
	 * Creates a user with the given name
	 * @param name The User's name
	 */
	public User(String name) {
		_id = next_id++;
		setName(name);
	}

	/**
	 * Creates a new temporary User with the given player number
	 */
	public User(int player_num) {
		_id = -Math.abs(player_num);
		_name = String.format("Guest User #%d", Math.abs(player_num));
	}

	/**
	 * Creates a User with the given id and name
	 * @param id Id to be
	 * @param name Name to be
	 */
	public User(int id, String name) {
		_id = id;
		setName(name);
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
	 * @return REturns the User's name
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
		return _id == -1;
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
}
