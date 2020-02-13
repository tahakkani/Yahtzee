import java.io.File;
import java.io.FileNotFoundException;
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

    private final static int DICE_IN_PLAY = 5;
    private ArrayList<YahtzeeDie> hand = new ArrayList<>();
    //This ScoreCard is the player's official score card for the game
    private ScoreCard scores = new ScoreCard(DICE_IN_PLAY, YahtzeeDie.NUM_SIDES);
    //This ScoreCard is the player's card used to display potential moves based on the current hand
    private ScoreCard possibleScores = new ScoreCard(DICE_IN_PLAY, YahtzeeDie.NUM_SIDES);


    public static void main(String[] args) {
        YahtzeePlayer player1 = new YahtzeePlayer();
        Scanner kb = new Scanner(System.in);
        /////
        Scanner inFile = null;
        ArrayList<String> configValues = new ArrayList<>();


        try {
            inFile = new Scanner(new File("yahtzeeConfig.txt"));
            while (inFile.hasNextLine())    {
                String line = inFile.nextLine();
                configValues.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File not found");
        }
            /////
        String playAgain = "y";

        while (Objects.equals(playAgain, "y")) {
            player1.rollingPhase();

            //start scoring
            //hand needs to be sorted to check for straights
            player1.sortAndDisplayHand();
            player1.determine_possibleScores();
            player1.possibleScores.displayCard(DICE_IN_PLAY);
            player1.hand.clear();
            player1.possibleScores.reset();
            System.out.println("\nEnter 'y' to play again ");
            playAgain = kb.next();
        }
    }

    /**
     * Finds the file, opens it, reads the values and
     */

    /**
     * This function basically implements the rolling part of the players turn. It allows player to keep dice
     * by typing in a string of 'y' and 'n's. Displays the hand after each roll.
     */
    public void rollingPhase(){
        Scanner kb = new Scanner(System.in);
        StringBuilder keep = new StringBuilder();
        System.out.println(keep);
        for (int i = 0; i < DICE_IN_PLAY; i++){
            hand.add(new YahtzeeDie());
            keep.append("n");
        }

        int roll = 1;
        while (roll < 4 && keep.toString().contains("n")) {
            //roll dice not kept
            for (int dieNumber = 0; dieNumber < DICE_IN_PLAY; dieNumber++) {
                if (keep.charAt(dieNumber) != 'y')
                    hand.get(dieNumber).setSideUp();
            }
            //output roll
            System.out.print("Your roll was: ");
            displayHand();

            //if not the last roll of the hand prompt the user for dice to keep
            if (roll < 3) {
                System.out.println("enter dice to keep (y or n) ");
                keep = Optional.ofNullable(kb.nextLine()).map(StringBuilder::new).orElse(null);
            }
            roll++;
        }
    }


    /**
     * This is called after each turn of rolling to determine for the player on which lines they can score and
     * how much they will score on that line. Uses other functions in this class to determine if the hand meets
     * the criteria for a straight, full house, yahtzee, etc
     *
     */
    public void determine_possibleScores(){
        //upper scorecard
        for(ScoreLine sL : possibleScores.getUpperSection()){
            int currCount = 0;
            int dieValue = possibleScores.getUpperSection().indexOf(sL) + 1;
            for(YahtzeeDie yD : hand){
                if(yD.getSideUp() == (dieValue))
                    currCount++;
            }
            sL.set_scoreValue(dieValue * currCount);
        }

        //lower scorecard
        //find max of a kind
        for (ScoreLine sL : possibleScores.getOfAKinds()){
            if (maxOfAKindFound() >= possibleScores.getOfAKinds().indexOf(sL) + 3)
                sL.set_scoreValue(totalAllDice());
        }

        ///full house
        if (fullHouseFound())
            possibleScores.getFullHouse().set_scoreValue(25);

        //test for all possible straights
        for (ScoreLine currLine : possibleScores.getStraights()) {
            int currIndex = possibleScores.getStraights().indexOf(currLine);
            //if max straight is greater than or equal to what we can count in the current line
            if (maxStraightFound() >= currIndex + 4)
                currLine.set_scoreValue((currIndex + 3) * 10);
        }

        //test for YAHTZEE, adds score
        if (maxOfAKindFound() == DICE_IN_PLAY)
            possibleScores.getYahtzee().set_scoreValue(50);

        possibleScores.getChance().set_scoreValue(totalAllDice());
    }

    /**
     * this function returns the total value of all dice in hand
     */
    int totalAllDice(){
        int total = 0;
        for (YahtzeeDie yD : hand) {
            total += yD.getSideUp();
        }
        return total;
    }

    /**
     * Takes the hand and orders the YahtzeeDie's based on their sideUp values. Is needed to determine if a straight
     * is acheived.
     */
    public void sortAndDisplayHand() {
        boolean swap;
        YahtzeeDie temp;

        do {
            swap = false;
            for (int count = 0; count < (hand.size() - 1); count++) {
                if (hand.get(count).getSideUp() > hand.get(count + 1).getSideUp()) {
                    temp = hand.get(count);
                    hand.set(count, hand.get(count + 1));
                    hand.set(count + 1, temp);
                    swap = true;
                }
            }
        } while (swap);

        System.out.println("Here is your sorted hand : ");
        displayHand();
    }

    /**
     * Displays the sideUp values of the hand across the screen horizontally
     */
    public void displayHand(){
        for (int dieNumber = 0; dieNumber < DICE_IN_PLAY; dieNumber++)
            System.out.print(hand.get(dieNumber).getSideUp() + " ");
        System.out.println("\n");
    }

    /**
     * this function returns the count of the die value occurring most in the hand but not the value itself
     *
     * @return the highest number of a single YahtzeeDie value in the hand
     */

    public int maxOfAKindFound() {
        int maxCount = 0;
        int currentCount;

        for (int dieValue = 1; dieValue <= YahtzeeDie.NUM_SIDES; dieValue++) {
            currentCount = 0;
            for (YahtzeeDie yahtzeeDie : hand) {
                if (yahtzeeDie.getSideUp() == dieValue)
                    currentCount++;
            }
            if (currentCount > maxCount)
                maxCount = currentCount;
        }
        return maxCount;
    }

    /**
     * this function returns the length of the longest
     * straight found in a hand, hand must be sorted beforehand
     *
     * @return the highest straight found in the hand
     */
    public int maxStraightFound() {
        int maxLength = 1;
        int curLength = 1;
        for(int counter = 0; counter < hand.size() - 1; counter++) {
            if (hand.get(counter).getSideUp() + 1 == hand.get(counter + 1).getSideUp()) //jump of 1
                curLength++;
            else if (hand.get(counter).getSideUp() + 1 < hand.get(counter + 1).getSideUp()) //jump of >= 2
                curLength = 1;
            if (curLength > maxLength)
                maxLength = curLength;
        }
        return maxLength;
    }

    /**
     * this function returns true if the hand is a full house
     * or false if it does not
     *
     * @return whether a full house was found or not
     */

    public boolean fullHouseFound() {
        boolean foundFH = false;
        boolean found3K = false;
        boolean found2K = false;
        int currentCount ;
        for (int dieValue = 1; dieValue <= YahtzeeDie.NUM_SIDES; dieValue++) {
            currentCount = 0;
            for (YahtzeeDie yahtzeeDie : hand) {
                if (yahtzeeDie.getSideUp() == dieValue)
                    currentCount++;
            }
            if (currentCount == 2)
                found2K = true;
            if (currentCount == 3)
                found3K = true;
        }
        if (found2K && found3K)
            foundFH = true;
        return foundFH;
    }
}

