package dartsApp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A derived version of the Game class representing a classic game of darts
 * Players are aiming to reach a specific score (301 by default)
 * The first to score exactly the target wins
 */
public class ClassicDarts extends Game {
	private final static byte DARTS_PER_TURN = 3;
	private final static short DEF_TARGET_SCORE = 301;
	private final static String 
		JSON_ARRAY_NAME = "classic darts",
		PLAYED_TO = "played to";
	private final short TARGET_SCORE;

	/**
	 * Creates a new Game with the given number of temporary players and a default target score (301) 
	 * @param player_count The number of players
	 */
	public ClassicDarts(byte player_count) {
		super(player_count);
		TARGET_SCORE = DEF_TARGET_SCORE;
	}

	/**
	 * Creates a new Game with the given players and a default target score (301) 
	 * @param players Players of this game
	 */
	public ClassicDarts(User[] players) {
		super(players);
		TARGET_SCORE = DEF_TARGET_SCORE;
	}
	
	/**
	 * Creates a new Game with the given number of temporary players and the given target score 
	 * @param player_count The number of players
	 * @param target_score The target score for this game
	 */
	public ClassicDarts(byte player_count, short target_score) {
		super(player_count);
		TARGET_SCORE = target_score;
	}

	/**
	 * Creates a new Game with the given players and the given target score 
	 * @param players Players of this game
	 * @param target_score The target score for this game
	 */
	public ClassicDarts(User[] players, short target_score) {
		super(players);
		TARGET_SCORE = target_score;
	}

	@Override
	public GameEvent addScore(short score) {
		GameEvent ret = null;
		
		// (naively) check for valid score
		if(score < 0 || score > 180) {
			ret = GameEvent.INVALIDSCORE;
		}
		else {
			// check for bust
			if(info.getTotalScores().get(player_num)+score > TARGET_SCORE) {
				// return that bust occurred
				ret = GameEvent.PLAYERBUSTED;
			}
			// otherwise add score
			else {
				info.getTotalScores().set(
						(int) player_num, 
						(short) (info.getTotalScores().get(player_num)+score)
				);
				// check for winner
				if(info.getTotalScores().get(player_num) == TARGET_SCORE) {
					// set winner and return that the game is over
					info.setWinner(info.getPlayers().get(player_num));
					ret = GameEvent.GAMEOVER;
				}
			}
			
			// update dart_count and turn nums
			// TODO: naively always adds three darts
			info.getDartCounts().set(
					(int) player_num, 
					(byte) (info.getDartCounts().get(player_num) + DARTS_PER_TURN)
			);
		}
		// increment player
		player_num++;
		// check for turn increment
		if(player_num >= info.getPlayers().size()) {
			turn_num++;
			player_num = 0;
		}		
		
		return ret;
	}

	@Override
	public String saveData() {
		// get File
		File info_file = new File(Game.SAVED_GAME_INFO_FILE_NAME);
		boolean new_file = false;
		
		// if the file doesn't exist, create it
		if(!info_file.exists()) {
			try {
				info_file.createNewFile();
				new_file = true;
			}
			catch (Exception ex) {}
		}
		try {
			// get file as string
			FileInputStream fis = new FileInputStream(info_file);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			StringBuffer strbuf = new StringBuffer();
			String line = br.readLine();
			while (line != null) {
				strbuf.append(line);
				line = br.readLine();
			}
			
			// get printwriter
			// this method of file writing from https://stackoverflow.com/questions/57913106/append-to-jsonobject-write-object-to-file-using-org-json-for-java
			PrintWriter writer = new PrintWriter(Game.SAVED_GAME_INFO_FILE_NAME);
			
			// get "game types" array
			JSONObject json_arr = null;
			JSONArray game_types_arr = null;
			if(!new_file) {
				try {
					json_arr = new JSONObject(strbuf.toString());
					game_types_arr = (JSONArray) json_arr.get(Game.BASE_ARRAY_NAME);
				}
				catch (Exception ex) {	
					// if the base array is not found, the file is either new or corrupted
					// if its not new, it should be deleted and recreated to wipe it
					info_file.delete();
					info_file.createNewFile();
					
					new_file = true;
				}
			}
			
			// if this is a new file, create "game types" array
			if(new_file) {
				// create new base object
				json_arr = new JSONObject();
				
				// create new base array and add it to base object
				game_types_arr = new JSONArray();
				json_arr.put(Game.BASE_ARRAY_NAME, game_types_arr);
				
				// create and add "classic darts" object while we're at it
				JSONObject new_classic_darts_obj = new JSONObject();
				game_types_arr.put(new_classic_darts_obj);
				new_classic_darts_obj.put(JSON_ARRAY_NAME, new JSONArray());
			}
			
			// get "classic darts" array
			JSONArray classic_darts_arr = null;
			for(Object o: game_types_arr) {
				// try getting arr
				try {
					classic_darts_arr = (JSONArray) ((JSONObject) o).get(JSON_ARRAY_NAME);
				}
				catch (Exception ex) {}
			}
			// if "classic darts" array doesn't exist, create it and find it
			if(classic_darts_arr == null) {
				// create it
				JSONObject new_cda_obj = new JSONObject();
				new_cda_obj.append(JSON_ARRAY_NAME, new JSONArray());
				game_types_arr.put(new_cda_obj);
				
				// find again
				for(Object o: game_types_arr) {
					// try getting arr
					try {
						classic_darts_arr = (JSONArray) ((JSONObject) o).get(JSON_ARRAY_NAME);
					}
					catch (Exception ex) {}
				}
			}
			
			// add game's info to "classic darts" array
			// create object
			JSONObject game_info = new JSONObject();
			// add ids
			ArrayList<Integer> ids = new ArrayList<Integer>();
			info.getPlayers().forEach(u -> ids.add(u.getID()));
			game_info.put(Game.PLAYER_ID_ARRAY, ids.toArray());
			// add scores
			game_info.put(Game.SCORE_ARRAY, info.getTotalScores().toArray());
			// add dart counts
			game_info.put(Game.DART_COUNT_ARRAY, info.getDartCounts().toArray());
			// add winner
			game_info.put(Game.WINNER_ID, info.getWinner().getID());
			// add target score
			game_info.put(PLAYED_TO, TARGET_SCORE);
			
			// add object to array
			classic_darts_arr.put(game_info);
			
			// update file?
			writer.println(json_arr.toString(4));
			writer.close();
			br.close();
		}
		catch (Exception ex) {
			System.out.printf("Error occured: %s\n", ex.getLocalizedMessage());
		}
		
		return null;
	}

}
