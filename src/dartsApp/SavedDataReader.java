package dartsApp;

import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Abstract class with static functions for reading data from a json file
 * TODO: really this is should be a class which stores a file location and the functions then aren't static
 * @author ethan
 *
 */
public abstract class SavedDataReader {

	protected static final String 
		SAVED_GAME_INFO_FILE_NAME = "resources\\saved_info\\SavedGameInfo.json",
		GAME_TYPE_ARRAY = "game types",
		USER_ARRAY = "users",
		GAME_PLAYER_ID_ARRAY = "player ids",
		GAME_SCORE_ARRAY = "player scores",
		GAME_DART_COUNT_ARRAY = "player dart counts",
		GAME_WINNER_ID = "winner id",
		USER_NEXT_ID = "next id",
		USER_ID = "id",
		USER_NAME = "name",
		USER_WINS = "wins",
		USER_LOSSES = "losses",
		USER_DART_COUNT = "dart count",
		USER_AVERAGE = "average";  
	
	// PRIVATE READERS
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
				
				br.close();
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
			
			// create new base arrays (and user id field) and add them to base object
			base_json_obj.put(USER_NEXT_ID, 1);
			base_json_obj.put(GAME_TYPE_ARRAY, new JSONArray());
			base_json_obj.put(USER_ARRAY, new JSONArray());
			
			System.out.println("Recreated base object");
			printToFile(base_json_obj);
			
			return base_json_obj;
		}
		
		/**
		 * Gets the base JSONObject and game type and user arrays of the saved data file
		 * If the array or file do not exist or are unreadable, creates a new base JSONObject and JSONArray
		 * @return Returns an Object array: [(JSONObject) base_obj, (JSONArray) game_type_arr, (JSONArray) user_arr]
		 */
		private static Object[] getBaseJSONObjectAndArrays() {		
			// get base JSONObject
			JSONObject base_obj = getBaseJSONObject();
			
			// get game_type and users arrays
			JSONArray game_type_arr = null;
			JSONArray users_arr = null;
			try {
				game_type_arr = (JSONArray) base_obj.get(GAME_TYPE_ARRAY);
				users_arr = (JSONArray) base_obj.get(USER_ARRAY);
			} // if that fails, recreate the base JSON object and try again
			catch (Exception ex) {
				try {
					base_obj = recreateBaseJSONObject();
					game_type_arr = (JSONArray) base_obj.get(GAME_TYPE_ARRAY);
					users_arr = (JSONArray) base_obj.get(USER_ARRAY);
				}
				catch (Exception ex2) {
					System.out.printf("Error reading base arrays from fresh base object: %s\n", ex2.getLocalizedMessage());
				}
			}
			
			// return base array
			Object[] ret = {base_obj, game_type_arr, users_arr};
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
				Object[] base_json_comps = getBaseJSONObjectAndArrays();
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
					new_cda_obj.put(game_type_array_name, new JSONArray());
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

	// PUBLIC READERS
		
		/**
		 * Gets the number of GameInfos saved for the given game type
		 * @param game_type The class of the game type in question
		 * @return Returns the number of GameInfos saved for the given game type
		 */
		public static int getGameInfoCountForType(Class<? extends Game> game_type) {
			return readSavedDataArray(game_type).length();
		}
		
		/**
		 * Reads all the saved instances of the the given game type from memory into GameInfo objects
		 * @param game_type The class of the game type in question
		 * @return Returns an ArrayList of GameInfo objects corresponding to the saved games
		 */
		public static ArrayList<GameInfo> getGameInfosForType(Class<? extends Game> game_type) {
			return getGameInfosForType(game_type, 0, getGameInfoCountForType(game_type));
		}
		
		/**
		 * Reads the last [count] saved instances of the the given game type from memory into GameInfo objects
		 * @param game_type The class of the game type in question
		 * @param count The number of games to fetch
		 * @return Returns an ArrayList of GameInfo objects corresponding to the saved games
		 */
		public static ArrayList<GameInfo> getGameInfosForType(Class<? extends Game> game_type, int count) {
			return getGameInfosForType(game_type, 0, count);
		}
		
		/**
		 * Reads a range of saved instances of the the given game type from memory into GameInfo objects
		 * @param game_type The class of the game type in question
		 * @param start The index of the first GameInfo to read where 0 is the last game
		 * @param end The index to stop at, this index will not be read
		 * @return Returns an ArrayList of GameInfo objects corresponding to the saved games
		 */
		public static ArrayList<GameInfo> getGameInfosForType(Class<? extends Game> game_type, int start, int end) {
			// get users
			ArrayList<User> users = getUsers();
			
			// get JSONArray of data
			JSONArray json_arr = readSavedDataArray(game_type);
			
			// create returning array
			ArrayList<GameInfo> ret = new ArrayList<GameInfo>();
			
			// convert to GameInfo objects
			for(int i = start; i < end && i < json_arr.length(); i++) {
				JSONObject jo = (JSONObject) json_arr.get(i);
				
				// get user ids
				ArrayList<Integer> user_ids = new ArrayList<Integer>();
				ArrayList<User> temp_users = new ArrayList<User>();
				jo.getJSONArray(GAME_PLAYER_ID_ARRAY).toList().forEach((obj) -> user_ids.add((Integer) obj));
				// add winner to user ids
				user_ids.add(jo.getInt(GAME_WINNER_ID));
				// get users
				for(Integer id: user_ids) {
					// create temp users
					if(id < 0) {
						temp_users.add(new User(id));
					} 
					// search for non-temp users
					else {
						User user = null;
						for(User u: users) {
							if(u.getID() == id) {
								user = u;
								break;
							}
						}
						// if user is found, add them
						if(user != null) {
							temp_users.add(user);
						} 
						// otherwise create a generic user with the same id
						// and save them to the file
						else {
							User new_user = new User(
									id, 
									String.format("User #%d", id),
									0, 0, 0, 0
							);
							temp_users.add(new_user);
							new_user.saveData();
							users.add(new_user);
						}
					}
				}
				
				// create GameInfo
				// TODO: get correct convertJSON function
				// ret.add(GameInfo.convertJSON(jo, temp_users));
				try {
					Class<? extends Game> cl = (Class<? extends Game>) game_type.getField("GAME_INFO_TYPE").get(null);
					Method m = cl.getMethod("convertJSON", new Class[] {JSONObject.class, List.class});
					// Object[] args = new Object[] {jo, users};
					GameInfo got_gi = (GameInfo) m.invoke(null, jo, temp_users);
					
					ret.add(got_gi);
				} catch (Exception ex) { System.out.printf("Error occured calling alternate convertJSON(): %s", ex.getLocalizedMessage()); }
			}
			
			// return
			return ret;
		}
		
		/**
		 * Reads all users from the saved data file
		 * @return Returns an ArrayList of all users
		 */
		public static ArrayList<User> getUsers() {
			// get json components
			Object[] base_objs = getBaseJSONObjectAndArrays();
			JSONArray user_arr = (JSONArray) base_objs[2];
			
			// create returning arraylist
			ArrayList<User> users = new ArrayList<User>();
			
			user_arr.forEach(
					(o) -> users.add(
							new User(
									((JSONObject) o).getInt(USER_ID), 
									((JSONObject) o).getString(USER_NAME),
									((JSONObject) o).getInt(USER_WINS), 
									((JSONObject) o).getInt(USER_LOSSES), 
									((JSONObject) o).getInt(USER_DART_COUNT), 
									((JSONObject) o).getDouble(USER_AVERAGE)
							)
					)
			);
			
			return users;
		}

		/**
		 * Reads the next user id from the saved data file
		 * @return Returns the next user id
		 */
		public static int getNextID() {
			try {
				// try getting the id
				JSONObject base_obj = getBaseJSONObject();
				return base_obj.getInt(USER_NEXT_ID);
			} catch (Exception ex) {
				// if that fails recreate the base object and try again
				recreateBaseJSONObject();
				return getNextID();
			}
		}
		
	// PRINTERS
		/**
		 * Prints the given JSONObject to the saved data file
		 * @param base_obj The JSONObject to print
		 */
		private static void printToFile(JSONObject base_obj) {
			// print
			try {
				// get printwriter
				// this method of file writing from https://stackoverflow.com/questions/57913106/append-to-jsonobject-write-object-to-file-using-org-json-for-java
				PrintWriter writer = new PrintWriter(SAVED_GAME_INFO_FILE_NAME);
				
				// print base JSONObject to file
				writer.println(base_obj.toString(4));
				writer.close();
				
			} catch (Exception ex) {
				System.out.printf("Error occured writing file: %s\n", ex.getLocalizedMessage());
			}
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
			Object[] base_json_comps = getBaseJSONObjectAndArrays();
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
			printToFile(base_obj);
		}
	
		/**
		 * Saves the given JSONObject as user data to the saved data file
		 * If a user with the given id already exists, it replaces that user
		 * @param data JSONObject to be saved
		 */
		public static void saveUserData(JSONObject data) {
			// get base JSONObject and JSONArray
			Object[] base_json_comps = getBaseJSONObjectAndArrays();
			JSONObject base_obj = (JSONObject) base_json_comps[0];
			JSONArray base_arr = (JSONArray) base_json_comps[2];
			
			int _id = data.getInt(USER_ID);
			int index = -1;
			
			for(int i = 0; i < base_arr.length() && index == -1; i++) {
				JSONObject jo = (JSONObject) base_arr.get(i);
				
				if(jo.getInt(USER_ID) == _id) {
					index = i;
				}
			}
			// if we found the user, replace
			if(index != -1) {
				base_arr.put(index, data);
			}
			// otherwise just write
			else {
				base_arr.put(data);
			}
			
			// print
			printToFile(base_obj);
		}
		
		/**
		 * Increments the next user ID stored in saved data file
		 */
		public static void incrementNextUserID() {
			try {
				// try incrementing the id
				JSONObject base_obj = getBaseJSONObject();
				base_obj.put(USER_NEXT_ID, base_obj.getInt(USER_NEXT_ID)+1);
				printToFile(base_obj);
			} catch (Exception ex) {
				// if that fails recreate the base object and try again
				recreateBaseJSONObject();
				incrementNextUserID();
			}
		}
}