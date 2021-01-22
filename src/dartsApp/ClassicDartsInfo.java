package dartsApp;

public class ClassicDartsInfo extends GameInfo {
	private final short TARGET_SCORE;

	public ClassicDartsInfo(GameInfo gi, short T_S) {
		super((User[]) gi.getPlayers().toArray());
		copyGameInfo(gi);
		TARGET_SCORE = T_S;
	}

	public short getTargetScore() {
		return TARGET_SCORE;
	}

}
