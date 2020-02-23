import java.util.Objects;
import java.util.Scanner;

/**
 * This class is a Yahtzee player, whose member fields are the player's hand (an ArrayList of YahtzeeDie),
 * the player's scorecard (a series of ScoreLines as well as tallying fields), and a temporary scorecard the displays
 * possible scoring to the user at the end of each turn. This class also has the DICE_IN_PLAY field for the time being,
 * which will probably be passed up to a higher class down the road.
 *
 * CPSC 224
 * Assignment #1
 * No sources to cite, other than the starter files provided by Dr. Worobec,
 * specifically for this assignment.
 *
 * @author Taha Hakkani
 * @version 1.0 2/5/2020
 * @see YahtzeeDie
 * @see ScoreCard
 */
public class YahtzeePlayer {

    private YahtzeeHand hand;
    //This ScoreCard is the player's official score card for the game
    private ScoreCard scores;

    public YahtzeePlayer(int numSides, int diceInPlay) {
        scores = new ScoreCard(diceInPlay, numSides);
        hand = new YahtzeeHand(numSides, diceInPlay);
    }

    public ScoreCard getScores() {
        return scores;
    }

    public static void main(String[] args) {
        int numSides = 6;
        int diceInPlay = 5;
        int rollsPerTurn = 3;
        YahtzeePlayer player1 = new YahtzeePlayer(numSides, diceInPlay);
        Scanner kb = new Scanner(System.in);
        String playAgain = "y";

        while (Objects.equals(playAgain, "y")) {
            player1.takeTurn(diceInPlay, rollsPerTurn);
            System.out.println("\nEnter 'y' to play again ");
            playAgain = kb.next();
        }
    }


    public void askToDisplayScorecard() {
        System.out.println("Would you like to see your scorecard?");
        Scanner kb = new Scanner(System.in);
        if (kb.nextLine().equals("y"))
            scores.displayFullCard();
    }

    public void scoreOnLine() {
        Scanner kb = new Scanner(System.in);
        boolean lineFound = false;
        while (!lineFound) {
            System.out.println("Please enter the title of the line that you would like to score on: ");
            String entry = kb.nextLine();
            ScoreLine newEntryLine = hand.getPossibleScores().findLine(entry);
            if (newEntryLine != null) {
                if (!newEntryLine.equals(hand.getPossibleScores().getYahtzeeBonus())){
                    scores.findLine(entry).setScoreValue(newEntryLine.getScoreValue());
                    newEntryLine.setUsed(true); //prevents line from being used again
                }
                else
                    scores.addBonusYahtzee();
                lineFound = true;
            } else
                System.out.println("That line was not found! (You may have scored there earlier)");
        }
    }

    public void takeTurn(int diceInPlay, int rollsPerTurn) {
        askToDisplayScorecard();
        hand.rollingPhase(diceInPlay, rollsPerTurn);
        //start scoring
        //hand needs to be sorted to check for straights
        hand.sortAndDisplayRoll();
        hand.determine_possibleScores(diceInPlay);
        hand.getPossibleScores().displayPossibilities();
        //possibleScores.displayCard();
        scoreOnLine();
        hand.clearHand();
    }
}
