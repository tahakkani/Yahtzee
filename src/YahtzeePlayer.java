import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

/**
 * This class is a Yahtzee player, whose member fields are the player's hand (an ArrayList of YahtzeeDie),
 * the player's scorecard (a series of ScoreLines as well as tallying fields), and a temporary scorecard the displays
 * possible scoring to the user at the end of each turn. This class also has the DICE_IN_PLAY field for the time being,
 * which will probably be passed up to a higher class down the road. yo
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

    protected static int DICE_IN_PLAY;
    private static int ROLLS_PER_TURN;
    private YahtzeeHand hand;
    //This ScoreCard is the player's official score card for the game
    private ScoreCard scores;


    public static void main(String[] args) {
        YahtzeePlayer player1 = new YahtzeePlayer();
        Scanner kb = new Scanner(System.in);
        String playAgain = "y";

        while (Objects.equals(playAgain, "y")) {
            player1.takeTurn();
            System.out.println("\nEnter 'y' to play again ");
            playAgain = kb.next();
        }
    }

    public void scoreOnLine() {
        Scanner kb = new Scanner(System.in);
        boolean lineFound = false;
        while (!lineFound) {
            System.out.println("Please enter the title of the line that you would like to score on: ");
            String entry = kb.nextLine();
            ScoreLine newEntryLine = scores.findLine(entry);
            if (newEntryLine != null) {
                scores.findLine(entry).setScoreValue(newEntryLine.getScoreValue());
                lineFound = true;
            } else
                System.out.println("That line was not found!");
        }
    }

    public void askToDisplayScorecard() {
        System.out.println("Would you like to see your scorecard?");
        Scanner kb = new Scanner(System.in);
        if (kb.nextLine() == "y") ;
        scores.displayPossibilities();
    }

    public void takeTurn() {
        askToDisplayScorecard();
        rollingPhase();
        //start scoring
        //hand needs to be sorted to check for straights
        hand.sortAndDisplayHand();
        hand.determine_possibleScores();
        //possibleScores.displayCard();
        scoreOnLine();
        askToDisplayScorecard();
        hand.clearHand();
    }

    public YahtzeePlayer() {
        gameInit();
        scores = new ScoreCard(DICE_IN_PLAY, YahtzeeDie.NUM_SIDES);
        hand = new YahtzeeHand(DICE_IN_PLAY);
    }

    /**
     * Finds the file, opens it, reads the values and
     */
    private void gameInit() {
        //load data from "yahtzeeConfig.txt" into an ArrayList
        Scanner inFile;
        ArrayList<Integer> configValues = new ArrayList<>();
        Scanner kb = new Scanner(System.in);

        try {
            inFile = new Scanner(new File("yahtzeeConfig.txt"));
            while (inFile.hasNextLine()) {
                configValues.add(Integer.parseInt(inFile.nextLine()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File not found");
        }

        //display data from "yahtzeeConfig.txt" in an easy-to-read manner
        System.out.println(configValues);
        System.out.println("You are playing with " + configValues.get(1) + " " + configValues.get(0) + "-sided dice");
        System.out.println("You get " + configValues.get(2) + " rolls per hand\n");
        System.out.println("Enter 'y' if you would like to change the configurations");

        String editConfig = kb.nextLine();

        //if the user wants to edit the config, allow them to enter values for die sides, die number, and
        //rolls per hand
        if (Objects.equals(editConfig, "y")) {
            configValues.clear();
            System.out.println("Enter the number of sides you want on each die: ");

            try {
                configValues.add(Integer.parseInt(kb.nextLine()));
            } catch (NumberFormatException e) {
                System.out.println("That is not a valid entry");
            }
            System.out.println("Enter the number of dice in play: ");
            configValues.add(Integer.parseInt(kb.nextLine()));
            System.out.println("Enter the number of rolls per hand: ");
            configValues.add(Integer.parseInt(kb.nextLine()));

            //write new values to txt file
            PrintStream outFile = null;
            try {
                outFile = new PrintStream(new File("yahtzeeConfig.txt"));
            } catch (FileNotFoundException e) {
                System.out.println("File not found");
                e.printStackTrace();
            }
            for (Integer config : configValues)
                Objects.requireNonNull(outFile).println(config);
            Objects.requireNonNull(outFile).close();
        }
        //set values that user chose/left as is
        YahtzeeDie.NUM_SIDES = configValues.get(0);
        DICE_IN_PLAY = configValues.get(1);
        ROLLS_PER_TURN = configValues.get(2);
    }

    /**
     * This function basically implements the rolling part of the players turn. It allows player to keep dice
     * by typing in a string of 'y' and 'n's. Displays the hand after each roll.
     */
    public void rollingPhase() {
        Scanner kb = new Scanner(System.in);
        StringBuilder keep = new StringBuilder();
        System.out.println(keep);
        for (int i = 0; i < DICE_IN_PLAY; i++) {
            hand.addToHand(new YahtzeeDie());
            keep.append("n");
        }

        //while there are still rolls left in the turn, re-roll all YahtzeeDie that were not kept
        int roll = 1;
        while (roll < ROLLS_PER_TURN && Objects.requireNonNull(keep).toString().contains("n")) {
            //roll dice not kept
            for (int dieNumber = 0; dieNumber < DICE_IN_PLAY; dieNumber++) {
                if (keep.charAt(dieNumber) != 'y')
                    hand.getFromHand(dieNumber).setSideUp();
            }
            //output roll
            System.out.print("Your roll was: ");
            hand.displayHand();

            //if not the last roll of the hand prompt the user for dice to keep
            if (roll <= ROLLS_PER_TURN) {
                System.out.println("enter dice to keep (y or n) ");
                keep = Optional.ofNullable(kb.nextLine()).map(StringBuilder::new).orElse(null);
            }
            roll++;
        }
    }
}
