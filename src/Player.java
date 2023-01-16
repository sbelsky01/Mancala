

public class Player {
	private String myName;
	private Mancala myMancala;
	private GenericPit[] pits = new GenericPit[13];
	private int lastPitLandedIn;

	public Player(String name, int playerNum, GenericPit[] publicArray) {
		myName = name;

		if (playerNum == 0) {
			createPrivatePitsArrayInSameOrderAsPublicArray(publicArray);
		} else if (playerNum == 1) {
			createPrivatePitArrayByShiftingOverPublicPits7Positions(publicArray);
		}
		myMancala = new Mancala();
		pits[6] = myMancala;

	}

	private void createPrivatePitsArrayInSameOrderAsPublicArray(GenericPit[] publicArray) {
		for (int i = 0; i < 13; i++) {
			if (i != 6)
				pits[i] = publicArray[i];
		}
	}

	private void createPrivatePitArrayByShiftingOverPublicPits7Positions(GenericPit[] publicArray) {
		for (int i = 0; i < 13; i++) {
			if (i != 6)
				pits[i] = publicArray[(i + 7) % 14];
		}
	}

	public String getName() {
		return myName;
	}

	public void addPebblesToMancala(int num) {
		myMancala.addPebbles(num);
	}

	public int numPebblesInMancala() {
		return myMancala.getNumPebbles();
	}

	public int getlastPitLandedIn() {
		return lastPitLandedIn;
	}

	public boolean pitIsEmpty(int userChoice) {
		int pit = getCorrespondingPitIndex(userChoice);

		if (((Pit) pits[pit]).isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	private int getCorrespondingPitIndex(int choice) {
		return 6 - choice;
	}

	public void takeTurn(int userChoice) {
		int startPit, currentPit, numPebblesInPit;

		startPit = getCorrespondingPitIndex(userChoice);
		numPebblesInPit = pits[startPit].getNumPebbles();

		((Pit) pits[startPit]).emptyAllPebbles();
		currentPit = startPit;

		for (int i = 0; i < numPebblesInPit; i++) {
			currentPit = (currentPit + 1) % 13;
			pits[currentPit].depositOnePebble();
		}
		lastPitLandedIn = currentPit;

		if (landedInEmptyPitOnPlayerSide()) {
			getAllPebblesInOppositePit();
		}
	}

	private void getAllPebblesInOppositePit() {
		int oppositePit = 12-lastPitLandedIn;
		int numPebblesInOppositePit = pits[oppositePit].getNumPebbles();
		myMancala.addPebbles(numPebblesInOppositePit + 1);

		// empty both pits
		((Pit) pits[lastPitLandedIn]).emptyAllPebbles();
		((Pit) pits[oppositePit]).emptyAllPebbles();
	}

	private boolean landedInEmptyPitOnPlayerSide() {
		if (lastPitLandedIn <= 5 && pits[lastPitLandedIn].getNumPebbles() == 1)
			return true;
		else
			return false;
	}

}
