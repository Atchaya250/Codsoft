import java.util.Random;
import java.util.Scanner;

public class NumberGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        int totalRounds = 0;
        int roundsWon = 0;
        final int MAX_ATTEMPTS = 7;
        String playAgain;

        System.out.println("Welcome to the Number Guessing Game!");

        do {
            totalRounds++;
            int secretNumber = random.nextInt(100) + 1;
            int attemptsLeft = MAX_ATTEMPTS;
            boolean guessedCorrectly = false;

            System.out.println("\n🔁 Round " + totalRounds);
            System.out.println("I have selected a number between 1 and 100.");
            System.out.println("You have " + MAX_ATTEMPTS + " attempts to guess it.");

            while (attemptsLeft > 0) {
                System.out.print("Enter your guess (1-100): ");

                int guess;
                if (scanner.hasNextInt()) {
                    guess = scanner.nextInt();
                } else {
                    System.out.println("⚠️ Invalid input. Please enter a number.");
                    scanner.next(); 
                    continue;
                }

                attemptsLeft--;

                if (guess == secretNumber) {
                    System.out.println("🎉 Correct! You guessed the number.");
                    guessedCorrectly = true;
                    roundsWon++;
                    break;
                } else if (guess < secretNumber) {
                    System.out.println("🔻 Too low.");
                } else {
                    System.out.println("🔺 Too high.");
                }

                if (attemptsLeft > 0) {
                    System.out.println("🕒 Attempts left: " + attemptsLeft);
                } else {
                    System.out.println("❌ Out of attempts! The number was: " + secretNumber);
                }
            }

            System.out.print("Play again? (yes/no): ");
            playAgain = scanner.next();
            scanner.nextLine(); 

        } while (playAgain.equalsIgnoreCase("yes") || playAgain.equalsIgnoreCase("y"));

        System.out.println("\n🏁 Game Over!");
        System.out.println("📊 Rounds played: " + totalRounds);
        System.out.println("✅ Rounds won: " + roundsWon);
        System.out.println("🎉 Thanks for playing!");

        scanner.close();
    }
}