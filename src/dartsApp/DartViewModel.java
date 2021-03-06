package dartsApp;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * ViewModel class to manage a Game object for a DartView
 * @author ethan
 *
 */
public class DartViewModel {	
	private Game _game;
	private ArrayList<User> users;
	
	/**
	 * Default constructor, sets Game to a two-player ClassicDarts game
	 */
	public DartViewModel() {
		_game = new ClassicDarts((byte) 2);
		init();
	}
	
	/**
	 * Constructs a DVM for the given Game object
	 * @param game The game object in question
	 */
	public DartViewModel(Game game) {
		_game = game;
		init();
	}
	
	private void init() {
		users = SavedDataReader.getUsers();
	}
	
	/**
	 * Saves the data of the current game and creates a new one
	 * 
	 * TODO: creating new game should give more control
	 */
	public void endGame() {
		_game.endGame();
		//newGame(ClassicDarts.class, new Object[] {(Byte) (byte) 2});
	}
	
	/**
	 * Initializes a new game for this DVM
	 * @param game_type The class of the game type to be initializes
	 * @param params The parameters of a constructor for the given game type contained in an array of Objects
	 */
	public void newGame(Class<? extends Game> game_type, Object[] params) {
		try {
			Class[] classes = new Class[params.length];
			
			Set<Class<?>> primitives = new HashSet<Class<?>>();
			primitives.add(Byte.class);
			primitives.add(Short.class);
			primitives.add(Integer.class);
			primitives.add(Long.class);
			primitives.add(Float.class);
			primitives.add(Double.class);
			primitives.add(Character.class);
			primitives.add(Boolean.class);
			
			for(int i = 0; i < params.length; i++) {
				// correct for primitive.class vs. primitive.TYPE
				if(primitives.contains(params[i].getClass())) {
					Class cl = params[i].getClass();
					// just gotta check all primitives manually cuz you can't cast to a generic primitive wrapper class
					if(cl == Byte.class) {
						classes[i] = Byte.TYPE;
					} else if(cl == Short.class) {
						classes[i] = Short.TYPE;
					} else if(cl == Integer.class) {
						classes[i] = Integer.TYPE;
					} else if(cl == Long.class) {
						classes[i] = Long.TYPE;
					} else if(cl == Float.class) {
						classes[i] = Float.TYPE;
					} else if(cl == Double.class) {
						classes[i] = Double.TYPE;
					} else if(cl == Character.class) {
						classes[i] = Character.TYPE;
					} else if(cl == Boolean.class) {
						classes[i] = Boolean.TYPE;
					}
				}
				// otherwise just take the class normally
				else {
					classes[i] = params[i].getClass();
				}
			}
			
			Constructor<? extends Game> constr = game_type.getConstructor(classes);
			_game = constr.newInstance(params);
			//Constructor<? extends Game> constr = game_type.getConstructor(new Class[] {players.getClass()} );
			//_game = constr.newInstance(new Object[] {players} );
		} catch (Exception ex) {
			System.out.printf("Exception constructing new Game with reflections: %s\n", ex.getLocalizedMessage()); 
		}
	}
	
	/**
	 * inputs the given score to the game object and returns the code
	 * @param score The score to be input
	 * @return Returns a GameEvent if something special happened or null if everything is normal
	 */
	public Game.GameEvent inputScore(short score) {
		return _game.addScore(score);
	}
	
	/**
	 * Gets the number of players in this game
	 * @return Returns the number of players in this game
	 */
	public int getPlayerCount() {
		return getPlayers().size();
	}
	
	/**
	 * Gets the current turn number (counts from 0)
	 * @return Returns the current turn number counting from zero
	 */
	public byte getTurnNum() {
		return _game.getTurnNum();
	}
	
	/**
	 * Gets the current player number (counts from 0)
	 * @return Returns the current player number counting from zero
	 */
	public byte getPlayerNum() {
		return _game.getPlayerNum();
	}
	
	/**
	 * Gets the current player
	 * @return Returns the current player
	 */
	public User getCurrentPlayer() {
		return _game.getCurrentPlayer();
	}
	
	/**
	 * Gets the players of this game
	 * @return Returns the game's players as an ArrayList of Users
	 */
	public ArrayList<User> getPlayers() {
		return _game.getInfo().getPlayers();
	}
	
	/**
	 * Gets the scores of the players of this game
	 * @return Returns the player's scores in the same order as the players array
	 */
	public ArrayList<Short> getScores() {
		return _game.getInfo().getTotalScores();
	}
	
	/**
	 * Gets the dart counts of the players of this game
	 * @return Returns the player's dart counts in the same order as the players array
	 */
	public ArrayList<Short> getDartCounts() {
		return _game.getInfo().getDartCounts();
	}
	
	/**
	 * Gets the average dart scores of the players of this game
	 * @return Returns the player's average scores in the same order as the players array
	 */
	public ArrayList<Double> getAverages() {
		return _game.getInfo().getAverages();
	}
	
	/**
	 * Gets the winner of this game (null if there is no winner)
	 * @return Returns the winning player
	 */
	public User getWinner() {
		return _game.getInfo().getWinner();
	}
	
	/**
	 * @return Returns whether or not this game has a winner yet
	 */
	public boolean hasWinner() {
		return _game.getInfo().getWinner() != null;
	}

	/**
	 * @return Returns an ArrayList of all the Users this DVM is aware of
	 */
	public ArrayList<User> getUsers() {
		return users;
	}

	/**
	 * Adds a User to this DVM and to the saved data file
	 * NOTE: this does not add the User to the current Game, only to the DVM's list of all users and to the saved data file
	 * @param user User to be added
	 */
	public void addUser(User user) {
		if(!users.contains(user)) {
			users.add(user);
		}
		user.saveData();
	}
}
