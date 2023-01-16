

public class GenericPit {
	protected int numPebbles;

	public GenericPit(int startingNumPebbles) {
		if (startingNumPebbles >= 0)
			numPebbles = startingNumPebbles;
		else
			throw new IllegalArgumentException("Invalid negative number of pebbles");
	}

	public int getNumPebbles() {
		return numPebbles;
	}

	public void depositOnePebble() {
		numPebbles++;
	}

}
