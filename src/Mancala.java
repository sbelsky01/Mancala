

public class Mancala extends GenericPit {
	// Mancalas start the game with no pebbles
	public Mancala() {
		super(0);
	}

	public void addPebbles(int num) {
		if (num >= 0)
			numPebbles += num;
		else
			throw new IllegalArgumentException("Invalid number of pebbles");
	}

}
