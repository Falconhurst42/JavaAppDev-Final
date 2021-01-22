package dartsApp;

import java.util.ArrayList;

/**
 * Abstract class with static functions for reading data from a json file
 * TODO: really this is should be a class which stores a file location and the functions then aren't static
 * @author ethan
 *
 */
public abstract class DartViewModel {	
	private Game _game;
	
	/**
	 * Default constructor, sets Game to a two-player ClassicDarts game
	 */
	public DartViewModel() {
		_game = new ClassicDarts((byte) 2);
	}
	
	/**
	 * Constructs a DVM for the given Game object
	 * @param game The game object in question
	 */
	public DartViewModel(Game game) {
		_game = game;
	}
	
	/**
	 * Saves the data of the current game and creates a new one
	 * 
	 * TODO: creating new game should give more control
	 */
	public void endGame() {
		_game.endGame();
		_game = new ClassicDarts((byte) 2);
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
		// get data
		ArrayList<Short> dart_counts = getDartCounts();
		ArrayList<Short> scores = getScores();
		// create averages array
		ArrayList<Double> averages = new ArrayList<Double>();
		// compute averages
		for(int i = 0; i < getPlayerCount(); i++) {
			averages.add((((double) scores.get(i)) / ((double) dart_counts.get(i))));
		}
		return averages;
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
		return _game.getInfo().getWinner() == null;
	}
}
