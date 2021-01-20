package dartsApp;

import java.util.Scanner;

public class ClassicDartsTest {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		ClassicDarts game = new ClassicDarts((byte) 2);
		
		while(game.getInfo().getWinner() == null) {
			printGame(game);
			System.out.printf("Enter Score for %s: ", game.getCurrentPlayer().getName());
			if(game.addScore(sc.nextShort()) == Game.GameEvent.PLAYERBUSTED) {
				System.out.println("BUSTED!");
			}
		}
		
		printGame(game);
		
		sc.close();

	}
	
	private static void printGame(Game g) {
		System.out.println("\n\nScores:");
		for(int i = 0; i < g.info.getPlayers().size(); i++) {
			System.out.printf(
					"%s: %d (avg: %f)\n", 
					g.getCurrentPlayer().getName(), 
					g.info.getTotalScores().get(i),
					(double)g.info.getTotalScores().get(i) / (double)g.info.getDartCounts().get(i)
			);
		}
		if(g.getInfo().getWinner() != null) {
			System.out.printf("%s won!\n", g.getInfo().getWinner().getName());
		}
	}

}
