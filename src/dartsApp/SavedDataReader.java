package dartsApp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class SavedDataReader {

	protected static final String 
		SAVED_GAME_INFO_FILE_NAME = "resources\\saved_info\\SavedGameInfo.json",
		BASE_ARRAY_NAME = "game types",
		PLAYER_ID_ARRAY = "player_ids",
		SCORE_ARRAY = "player_scores",
		DART_COUNT_ARRAY = "player_dart_counts",
		WINNER_ID = "winner_id";  
	
	/**
	 * Returns the value of the JSON_ARRAY_NAME attribute of the given game type class 
	 * @param game_type The class of the Game variation in question
	 * @return Returns the value of the JSON_ARRAY_NAME attribute
	 */
	private static String gameTypeToJSONArrayString(Class<? extends Game> game_type) {
		// get JSONArray String
		// don't allow the Game class
		// TODO: make the Game class fetch the home array?
		String game_type_array_name = null;
		if(game_type.isAssignableFrom(Game.class)) {
			// game_type_array_name = Game.JSON_ARRAY_NAME;
		}
		else {
			try {
				game_type_array_name = (String) game_type.getField("JSON_ARRAY_NAME").get(game_type_array_name);
			} catch (Exception ex) {
				System.out.printf("Error Occured: %s\n", ex.getLocalizedMessage());
			}
		}
		
		return game_type_array_name;
	}
	
	/**
	 * Gets the base JSONObject of the saved data file
	 * If the file does not exist is unreadable, the file is cleared and a new JSONObject is created
	 * TODO: really shouldn't clear the file here right?
	 * @return Returns the base JSONObject of the saved data file 
	 */
	private static JSONObject getBaseJSONObject() {
		// create JSONObject we will be returning
		JSONObject base_json_obj = null;
		
		// get File
		File info_file = new File(SAVED_GAME_INFO_FILE_NAME);
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
			
			// get "game types" array
			if(!new_file) {
				try {
					base_json_obj = new JSONObject(strbuf.toString());
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
				base_json_obj = recreateBaseJSONObject();
			}
		}
		catch (Exception ex) {
			System.out.printf("Error occured: %s\n", ex.getLocalizedMessage());
		}
		
		return base_json_obj; 
	}
	
	/**
	 * Creates a new base JSONObject for the saved data file
	 * @return Returns the new JSONObject 
	 */
	private static JSONObject recreateBaseJSONObject() {
		// create JSONObject we will be returning
		JSONObject base_json_obj = null;
		
		// create new base object
		base_json_obj = new JSONObject();
		
		// create new base array and add it to base object
		JSONArray base_arr = new JSONArray();
		base_json_obj.put(BASE_ARRAY_NAME, base_arr);
		
		System.out.println("Recreated base object");
		
		return base_json_obj;
	}
	
	/**
	 * Gets the base JSONArray and JSONObject of the saved data file
	 * If the array or file do not exist or are unreadable, creates a new base JSONObject and JSONArray
	 * @return Returns the JSONArray and JSONObject in an Object array: [JSONObject, JSONArray]
	 */
	private static Object[] getBaseJSONArrayAndObject() {		
		// get base JSONObject and JSONArray
		JSONObject base_obj = getBaseJSONObject();
		JSONArray base_arr = null;
		try {
			base_arr = (JSONArray) base_obj.get(BASE_ARRAY_NAME);
		} // if that fails, recreate the base JSON object and try again
		catch (Exception ex) {
			try {
				base_obj = recreateBaseJSONObject();
				base_arr = (JSONArray) base_obj.get(BASE_ARRAY_NAME);
			}
			catch (Exception ex2) {
				System.out.printf("Error reading base array rom fresh base object: %s\n", ex2.getLocalizedMessage());
			}
		}
		
		// return base array
		Object[] ret = {base_obj, base_arr};
		return ret;
	}
	
	/**
	 * Fetches the JSONArray containing the data for the given game type
	 * If the array and/or data file do not exist or are unreadable, they will be created
	 * @param game_type The class of the Game variation in question
	 * @return Returns the JSONArray containing saved info about the given Game type
	 */
	private static JSONArray readSavedDataArray(Class<? extends Game> game_type) {
		// get JSONArray String
		String game_type_array_name = gameTypeToJSONArrayString(game_type);
		if(game_type_array_name == null) {
			return null;
		}
		
		try {
			// get base JSONObject and JSONArray
			Object[] base_json_comps = getBaseJSONArrayAndObject();
			JSONArray base_arr = (JSONArray) base_json_comps[1];
			
			// get array for given game type
			JSONArray game_data_arr = null;
			for(Object o: base_arr) {
				// try getting arr
				try {
					game_data_arr = (JSONArray) ((JSONObject) o).get(game_type_array_name);
				}
				catch (Exception ex) {}
			}
			// if array doesn't exist, create it and find it
			if(game_data_arr == null) {
				// create it
				JSONObject new_cda_obj = new JSONObject();
				new_cda_obj.append(game_type_array_name, new JSONArray());
				// append it to the base array
				base_arr.put(new_cda_obj);
				
				// find again
				for(Object o: base_arr) {
					// try getting arr
					try {
						game_data_arr = (JSONArray) ((JSONObject) o).get(game_type_array_name);
					}
					catch (Exception ex) {}
				}
			}
			
			// return the data arry for the given game type
			return game_data_arr;
		}
		catch (Exception ex) {
			System.out.printf("Error occured: %s\n", ex.getLocalizedMessage());
		}
		return null;
	}

	/**
	 * Appends the given JSONObject to the JSONArray for the given game type in the saved data file
	 * @param data The JSONObject to be appended
	 * @param game_type The class of the game type in question
	 */
	protected static void appendGameData(JSONObject data, Class<? extends Game> game_type) {
		// get JSONArray String
		String game_type_array_name = gameTypeToJSONArrayString(game_type);
		if(game_type_array_name == null) {
			return;
		}

		// get base object and arr
		Object[] base_json_comps = getBaseJSONArrayAndObject();
		JSONObject base_obj = (JSONObject) base_json_comps[0];
		JSONArray base_arr = (JSONArray) base_json_comps[1];
		
		// find JSONObject containing array
		JSONObject game_type_obj = null;
		for(Object o: base_arr) {
			if(((JSONObject) o).has(game_type_array_name)) {
				game_type_obj = (JSONObject) o;
			}
		}
		// if the object can't be found, create it
		if(game_type_obj == null) {
			game_type_obj = new JSONObject();
			base_arr.put(game_type_obj);
		}

		// get JSONArray
		JSONArray data_arr = null;
		try {
			data_arr = game_type_obj.getJSONArray(game_type_array_name);
		} catch (Exception ex) {}
		// if its not found, create and add it
		if(data_arr == null) {
			data_arr = new JSONArray();
			game_type_obj.remove(game_type_array_name);
			game_type_obj.put(game_type_array_name, data_arr);
		}
		
		// append to array
		data_arr.put(data);
		
		// print
		try {
			// get printwriter
			// this method of file writing from https://stackoverflow.com/questions/57913106/append-to-jsonobject-write-object-to-file-using-org-json-for-java
			PrintWriter writer = new PrintWriter(SAVED_GAME_INFO_FILE_NAME);
			
			// print base JSONObject to file
			String f = base_obj.toString(4);
			writer.println(base_obj.toString(4));
			writer.close();
			
		} catch (Exception ex) {
			System.out.printf("Error occured writing file: %s\n", ex.getLocalizedMessage());
		}
	}

	/**
	 * Reads all the saved instances of the the given game type from memory into GameInfo objects
	 * @param game_type The class of the game type in question
	 * @return Returns an ArrayList of GameInfo objects corresponding to the saved games
	 */
	public static ArrayList<GameInfo> getGameInfosForType(Class<? extends Game> game_type) {
		// get JSONArray of data
		JSONArray json_arr = readSavedDataArray(game_type);
		
		// create returning array
		ArrayList<GameInfo> ret = new ArrayList<GameInfo>();
		
		// convert to GameInfo objects
		for(Object o: json_arr) {
			JSONObject jo = (JSONObject) o;
		}
		
		// return
		return ret;
	}

	/**
	 * Saves the given JSONObject as user data to the saved data file
	 * If a user with the given id already exists, it replaces that user
	 * @param data JSONObject to be saved
	 */
	public static void saveUserData(JSONObject data) {
		
	}
}