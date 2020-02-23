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
public class YahtzeeHand {

    private ArrayList<YahtzeeDie> roll = new ArrayList<>();
    //This ScoreCard is the hand's card used to display potential moves based on the current hand
    private ScoreCard possibleScores;

    public YahtzeeHand(int numSides, int diceInPlay) {
        possibleScores = new ScoreCard(diceInPlay, numSides);
    }

    public void clearHand(){
        roll.clear();
        possibleScores.resetValues();
    }

    public ScoreCard getPossibleScores(){ return possibleScores; }

    public YahtzeeDie getFromHand(int dieIndex){
        return roll.get(dieIndex);
    }

    public void addToHand(YahtzeeDie yahtzeeDie){
        roll.add(yahtzeeDie);
    }

    /**
     * This is called after each turn of rolling to determine for the player on which lines they can score and
     * how much they will score on that line. Uses other functions in this class to determine if the hand meets
     * the criteria for a straight, full house, yahtzee, etc
     *
     * @param diceInPlay how many dice there are
     */
    public void determine_possibleScores(int diceInPlay){
        //upper scorecard
        for(ScoreLine sL : possibleScores.getUpperSection()){
            int currCount = 0;
            int dieValue = possibleScores.getUpperSection().indexOf(sL) + 1;
            for(YahtzeeDie yD : roll){
                if(yD.getSideUp() == (dieValue))
                    currCount++;
            }
            sL.setScoreValue(dieValue * currCount);
        }

        //lower scorecard
        //find max of a kind
        for (ScoreLine sL : possibleScores.getOfAKinds()){
            if (maxOfAKindFound() >= possibleScores.getOfAKinds().indexOf(sL) + 3)
                sL.setScoreValue(totalAllDice());
        }

        ///full house
        if (fullHouseFound())
            possibleScores.getFullHouse().setScoreValue(25);

        //test for all possible straights
        for (ScoreLine currLine : possibleScores.getStraights()) {
            int currIndex = possibleScores.getStraights().indexOf(currLine);
            //if max straight is greater than or equal to what we can count in the current line
            if (maxStraightFound() >= currIndex + 4)
                currLine.setScoreValue((currIndex + 3) * 10);
        }

        //test for YAHTZEE, adds score
        if (maxOfAKindFound() == diceInPlay)
            if(possibleScores.getYahtzee().isUsed())
                possibleScores.getYahtzeeBonus().setScoreValue((possibleScores.getBonusYahtzees() + 1) * 100);
            else
                possibleScores.getYahtzee().setScoreValue(50);
        possibleScores.getChance().setScoreValue(totalAllDice());
    }

    /**
     * this function returns the total value of all dice in hand
     */
    int totalAllDice(){
        int total = 0;
        for (YahtzeeDie yD : roll) {
            total += yD.getSideUp();
        }
        return total;
    }


    /**
     * This function basically implements the rolling part of the players turn. It allows player to keep dice
     * by typing in a string of 'y' and 'n's. Displays the hand after each roll.
     *
     * @param diceInPlay how many dice there are
     * @param rollsPerTurn how many rolls per turn there are
     */
    public void rollingPhase(int diceInPlay, int rollsPerTurn) {
        Scanner kb = new Scanner(System.in);
        StringBuilder keep = new StringBuilder();
        System.out.println(keep);
        for (int i = 0; i < diceInPlay; i++) {
            addToHand(new YahtzeeDie());
            keep.append("n");
        }

        //while there are still rolls left in the turn, re-roll all YahtzeeDie that were not kept
        int rollNum = 1;
        while (rollNum < rollsPerTurn && Objects.requireNonNull(keep).toString().contains("n")) {
            //roll dice not kept
            for (int dieNumber = 0; dieNumber < diceInPlay; dieNumber++)
                if (keep.charAt(dieNumber) != 'y')
                    getFromHand(dieNumber).setSideUp();
            //output roll
            System.out.print("Your roll was: ");
            displayRoll();

            //if not the last roll of the hand prompt the user for dice to keep
            System.out.println("enter dice to keep (y or n) ");
            keep = Optional.ofNullable(kb.nextLine()).map(StringBuilder::new).orElse(null);
            rollNum++;
        }
    }

    /**
     * Takes the hand and orders the YahtzeeDie's based on their sideUp values. Is needed to determine if a straight
     * is acheived.
     */
    public void sortAndDisplayRoll() {
        boolean swap;
        YahtzeeDie temp;

        do {
            swap = false;
            for (int count = 0; count < (roll.size() - 1); count++) {
                if (roll.get(count).getSideUp() > roll.get(count + 1).getSideUp()) {
                    temp = roll.get(count);
                    roll.set(count, roll.get(count + 1));
                    roll.set(count + 1, temp);
                    swap = true;
                }
            }
        } while (swap);

        System.out.println("Here is your sorted hand : ");
        displayRoll();
    }

    /**
     * Displays the sideUp values of the hand across the screen horizontally
     */
    public void displayRoll(){
        for (YahtzeeDie i : roll)
            System.out.print(i.getSideUp() + " ");
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
            for (YahtzeeDie yahtzeeDie : roll) {
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
        for(int counter = 0; counter < roll.size() - 1; counter++) {
            if (roll.get(counter).getSideUp() + 1 == roll.get(counter + 1).getSideUp()) //jump of 1
                curLength++;
            else if (roll.get(counter).getSideUp() + 1 < roll.get(counter + 1).getSideUp()) //jump of >= 2
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
            for (YahtzeeDie yahtzeeDie : roll) {
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

