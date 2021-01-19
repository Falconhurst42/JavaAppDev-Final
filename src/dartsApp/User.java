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
	 * Either creates a temporary User or a new new with a generic name, depending on the boolean passed in
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

	public User(String name) {
		_id = next_id++;
		setName(name);
	}

	public int getID() {
		return _id;
	}

	public String getName() {
		return _name;
	}

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
