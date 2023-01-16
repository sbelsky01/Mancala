import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		displayWelcomeMessage();

		String[] playerNames = getPlayerNames();

		MancalaGame game = new MancalaGame(playerNames);
		game.play();
	}

	public static void displayWelcomeMessage() {
		System.out.println("------------------------------------");
		System.out.println("        Welcome to Mancala");
		System.out.println("------------------------------------");
	}

	public static String[] getPlayerNames() {
		Scanner keyboard = new Scanner(System.in);
		String[] playerNames = new String[2];

		System.out.println("\nEnter the player names");
		for (int i = 0; i < 2; i++) {
			System.out.print("Player " + (i + 1) + ": ");
			playerNames[i] = keyboard.nextLine();
		}
		System.out.println();

		return playerNames;
	}
}
