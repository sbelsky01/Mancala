

public class Pit extends GenericPit {
	// Pits start the game with 4 pebbles
	public Pit() {
		super(4);
	}

	public void emptyAllPebbles() {
		numPebbles = 0;
	}

	public boolean isEmpty() {
		if (numPebbles == 0)
			return true;
		else
			return false;
	}

}
