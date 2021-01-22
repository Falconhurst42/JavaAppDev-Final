package dartsApp;

import java.util.Random;

public class ClassicDartsTest {

	public static void main(String[] args) {
		
		User plyr1 = new User("Ethan");
		User plyr2 = new User("Joshua");
		
		Random rand = new Random();
		
		for(int i = 0; i < 5; i++) {
			ClassicDarts game = new ClassicDarts(new User[] {plyr1, plyr2});
			
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
					(double)g.info.getTotalScores().get(i) / (double)g.info.getDartCounts().get(i)
			);
		}
		if(g.getInfo().getWinner() != null) {
			System.out.printf("%s won!\n", g.getInfo().getWinner().getName());
		}
	}

}
