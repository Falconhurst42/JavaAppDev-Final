package dartsApp;

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

}
