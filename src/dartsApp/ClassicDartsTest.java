package dartsApp;

import java.util.ArrayList;
import java.util.Random;

public class ClassicDartsTest {

	public static void main(String[] args) {
		
		for(int i = 0; i < 3; i++) {			
			simulateGames(new User[] {
					new User(String.format("Ethan #%d", i+1)), 
					new User(String.format("Joshua #%d", i+1))}, 10);
		}
		for(User u: SavedDataReader.getUsers()) {
			System.out.println("\n" + u.toString() + "\n");
		}
	}
	
	private static void simulateGames(User[] players, int game_count) {
		Random rand = new Random();
		
		for(int i = 0; i < game_count; i++) {
			ClassicDarts game = new ClassicDarts(players);
			
			while(game.getInfo().getWinner() == null) {
				game.addScore((short) rand.nextInt(40));
			}
			
			// print game results
			printGame(game);
			// save data
			game.endGame();
		}
	}
	
	private static void printGame(Game g) {
		System.out.println("\n\nScores:");
		for(int i = 0; i < g.info.getPlayers().size(); i++) {
			System.out.printf(
					"%s: %d (avg: %f)\n", 
					g.getInfo().getPlayers().get(i).getName(), 
					g.info.getTotalScores().get(i),
					g.info.getAverages().get(i)
			);
		}
		if(g.getInfo().getWinner() != null) {
			System.out.printf("%s won!\n", g.getInfo().getWinner().getName());
		}
	}

}
