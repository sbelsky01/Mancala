

import java.util.Scanner;
import java.util.InputMismatchException;

public class MancalaGame {
	private Player[] players = new Player[2];
	private GenericPit[] gamePits = new GenericPit[13];
	final int MANCALA_POSITION = 6;
	final int BOTH_SIDES_HAVE_PEBBLES = 3;
	final int NEITHER_SIDE_HAS_PEBBLES = 0;

	public MancalaGame(String[] playerNames) {
		// instantiate 12 Pits for the pits array
		for (int i = 0; i < 13; i++) {
			if (i != MANCALA_POSITION)
				gamePits[i] = new Pit();
		}

		for (int playerNumber = 0; playerNumber < 2; playerNumber++) {
			players[playerNumber] = new Player(playerNames[playerNumber], playerNumber, gamePits);
		}
	}

	public void play() {
		displayInstructions();
		displayBoard("Initial Setup:");

		playGame();

		displayGameOverMessage();
		allocateRemainingPebbles();
		displayBoard("Final Board:");
		displayFinalScore();
	}

	private void displayInstructions() {
		System.out.println("This diagram represents the mancala board.");
		System.out.println("Players move pebbles around the board in a counterclockwise direction.");
		System.out.println("The inner rows represent the number of pebbles in the pits.");
		System.out.println("The numbers in the outer row are used to refer to a specific pit.");
		System.out.println("The center row shows the total pebbles in the two Mancalas.");
		System.out.println("The Mancala on the left belongs to player 2");
		System.out.println("and the Mancala on the right belongs to player 1.");
	}

	private void displayBoard(String description) {

		System.out.println("\n " + description + "\n");

		displayPlayerName(2);
		numberPlayer2Pits();
		displayPlayerPits(2);

		displayBothMancalas();

		displayPlayerPits(1);
		numberPlayer1Pits();
		displayPlayerName(1);
		
		System.out.println();
	}

	private void displayPlayerName(int playerNum) {
		int playerIndex = playerNum - 1;
		String playerName = players[playerIndex].getName();

		int numSpaces = determineNumberOfPrecedingSpaces(playerName);
		for (int i = 0; i < numSpaces; i++) {
			System.out.print(" ");
		}
		System.out.println(playerName);
	}

	private int determineNumberOfPrecedingSpaces(String textToDisplay) {
		int lengthOfText = textToDisplay.length();
		int halfOfBoardWidth = 19;
		int numSpacesNeeded = halfOfBoardWidth - (int) (lengthOfText / 2);
		return numSpacesNeeded;
	}

	private void numberPlayer2Pits() {
		System.out.println("         1   2   3   4   5   6");
		System.out.println("  ----------------------------------");
	}

	private void numberPlayer1Pits() {
		System.out.println("  ----------------------------------");
		System.out.println("         6   5   4   3   2   1");
	}

	private void displayPlayerPits(int playerNum) {
		System.out.print(" |    |");

		if (playerNum == 2)
			displayNumberOfPebblesInPlayer2Pits();
		else
			displayNumberOfPebblesInPlayer1Pits();

		System.out.println("|    |");
	}

	private void displayNumberOfPebblesInPlayer2Pits() {
		for(int i=12; i>6; i--) {
			String formattedNumPebbles = String.format("%2d", gamePits[i].getNumPebbles());
			System.out.print(" " + formattedNumPebbles + " ");
		}
	}

	private void displayNumberOfPebblesInPlayer1Pits() {
		for (int i = 0; i < 6; i++) {
			String formattedNumPebbles = String.format("%2d", gamePits[i].getNumPebbles());
			System.out.print(" " + formattedNumPebbles + " ");
		}
	}

	private void displayBothMancalas() {
		String player1Mancala = String.format("%2d", players[1].numPebblesInMancala());
		String player2Mancala = String.format("%2d", players[0].numPebblesInMancala());
		System.out.println(" | " + player1Mancala + " |------------------------| " + player2Mancala + " |");
	}

	private void playGame() {
		int currPlayer = 0;
		int chosenStartingPit = 0;
		int playerWithPebblesLeft;
		boolean playerGetsAnotherTurn = false;
		boolean gameOver;

		do {
			chosenStartingPit = getStartingPit(players[currPlayer]);

			players[currPlayer].takeTurn(chosenStartingPit);

			displayBoard("Current Board:");

			playerGetsAnotherTurn = checkIfPlayerLandedInMancala(currPlayer);
			playerWithPebblesLeft = checkWhoHasPebblesLeft();
			gameOver = checkIfGameOver(playerWithPebblesLeft);

			if (!gameOver) {
				if (playerGetsAnotherTurn)
					System.out.println("You landed in your Mancala! You get to go again!\n");
				else
					currPlayer = (currPlayer + 1) % 2;
			}

		} while (!gameOver);
	}

	private int getStartingPit(Player currPlayer) {
		Scanner keyboard = new Scanner(System.in);
		boolean invalidValueEntered;
		int chosenStartingPit = 0;

		System.out.print(
				currPlayer.getName() + ", choose a pit from your side of the board" + " (enter a number from 1-6): ");

		do {
			invalidValueEntered = false;
			try {
				chosenStartingPit = keyboard.nextInt();

				if (!validPitNumber(chosenStartingPit)) {
					System.out.print("Please enter a valid choice: ");
					invalidValueEntered = true;
				} else if (currPlayer.pitIsEmpty(chosenStartingPit)) {
					System.out.println("The selected pit is currently empty.");
					System.out.print("Please choose a pit with at least 1 pebble in it: ");
					invalidValueEntered = true;
				}
			} catch (InputMismatchException e) {
				keyboard.nextLine();
				System.out.print("Please enter an integer: ");
				invalidValueEntered = true;
			}

		} while (invalidValueEntered);

		return chosenStartingPit;
	}

	private boolean validPitNumber(int choice) {
		if (choice < 1 || choice > 6)
			return false;
		else
			return true;
	}

	private boolean checkIfPlayerLandedInMancala(int playerIndex) {
		if (players[playerIndex].getlastPitLandedIn() == MANCALA_POSITION)
			return true;
		else
			return false;
	}

	private int checkWhoHasPebblesLeft() {
		int sideWithPebbles = 0;

		if (player1HasPebbles())
			sideWithPebbles += 1;

		if (player2HasPebbles())
			sideWithPebbles += 2;

		return sideWithPebbles;
	}

	private boolean player1HasPebbles() {

		int totalOnSide = 0;

		for (int i = 0; i < 6; i++) {
			totalOnSide += gamePits[i].getNumPebbles();
		}

		if (totalOnSide > 0)
			return true;
		else
			return false;
	}

	private boolean player2HasPebbles() {
		int totalOnSide = 0;

		for (int i = 7; i < 13; i++) {
			totalOnSide += gamePits[i].getNumPebbles();
		}

		if (totalOnSide > 0)
			return true;
		else
			return false;
	}

	private boolean checkIfGameOver(int pebblesOnSides) {
		if (pebblesOnSides == BOTH_SIDES_HAVE_PEBBLES)
			return false;
		else
			return true;
	}

	private void displayGameOverMessage() {
		System.out.println("------------------------------------\n");
		System.out.println("            Game Over");
		System.out.println("\n------------------------------------\n");
	}

	// at the end of the game, the player who still has pebbles left on their
	// side gets all of those pebbles
	private void allocateRemainingPebbles() {
		int playerWhoHasPebblesLeft = checkWhoHasPebblesLeft();

		if (playerWhoHasPebblesLeft != NEITHER_SIDE_HAS_PEBBLES) {
			int totalPebblesRemaining = calculateRemainingPebbles();
			Player playerWhoGetsRemainingPebbles = players[playerWhoHasPebblesLeft - 1];
			playerWhoGetsRemainingPebbles.addPebblesToMancala(totalPebblesRemaining);

			System.out.println(playerWhoGetsRemainingPebbles.getName() + " gets the remaining pebbles!");
		}
	}

	private int calculateRemainingPebbles() {
		Pit currentPit;

		int totalRemaining = 0;
		for (int i = 0; i < 13; i++) {
			if (i != MANCALA_POSITION) {
				currentPit = (Pit) gamePits[i];
				totalRemaining += currentPit.getNumPebbles();
				currentPit.emptyAllPebbles();
			}
		}

		return totalRemaining;
	}

	private void displayFinalScore() {
		displayNumberOfPebblesEachPlayerHas();
		String winningPlayerMessage = createWinnerMessage();
		displayWinnerMessage(winningPlayerMessage);
	}

	private void displayNumberOfPebblesEachPlayerHas() {
		System.out.println(" Final Score:\n");
		for (int i = 0; i < players.length; i++) {
			System.out.println("\t" + players[i].getName() + ": " + players[i].numPebblesInMancala());
		}
	}

	private String createWinnerMessage() {
		int winner = determineWinner();

		if (winner == 0) {
			return "It's a tie!";
		} else if (winner == 1) {
			String player1 = players[0].getName();
			return "~~ " + player1 + " wins! ~~";
		} else {
			String player2 = players[1].getName();
			return "~~ " + player2 + " wins! ~~";
		}
	}

	private int determineWinner() {
		int differenceBetweenMancalas = players[0].numPebblesInMancala() - players[1].numPebblesInMancala();
		
		if (differenceBetweenMancalas == 0)
			return 0;
		else if (differenceBetweenMancalas > 0)
			return 1;
		else
			return 2;
	}

	private void displayWinnerMessage(String message) {
		System.out.println("\n======================================");

		int numSpaces = determineNumberOfPrecedingSpaces(message);
		for (int i = 0; i < numSpaces; i++) {
			System.out.print(" ");
		}
		System.out.println(message);

		System.out.println("======================================");
	}
}
