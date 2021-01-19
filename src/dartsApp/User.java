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
	 * Either creates a temporary User or a new User with a generic name, depending on the boolean passed in
	 * @param isTemporary Whether the user should be temporary or not
	 */
	public User(boolean isTemporary) {
		if(isTemporary) {
			_id = -1;
			_name = "Guest User";
		}
		else {
			_id = next_id++;
			_name = String.format("User #%d", _id);
		}
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
