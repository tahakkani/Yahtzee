import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class YahtzeePlayer {

    private final static int DICE_IN_PLAY = 5;
    private ArrayList<YahtzeeDie> hand = new ArrayList<>();
    private ScoreCard scores = new ScoreCard(DICE_IN_PLAY, YahtzeeDie.NUM_SIDES);
    private ScoreCard possibleScores = new ScoreCard(DICE_IN_PLAY, YahtzeeDie.NUM_SIDES);


    public static void main(String[] args) {
        YahtzeePlayer player1 = new YahtzeePlayer();
        Scanner kb = new Scanner(System.in);

        //System.out.println(player1.possibleScores);
        //System.out.println(player1.scores);
        String playAgain = "y";

        while (Objects.equals(playAgain, "y")) {
            player1.executePhase1();

            //start scoring
            //hand needs to be sorted to check for straights
            player1.sortAndDisplayHand();
            player1.determine_possibleScores();
            player1.possibleScores.displayCard(DICE_IN_PLAY);
            player1.hand.clear();
            //you must clear out possible scores every time
            System.out.println("\nEnter 'y' to play again ");
            playAgain = kb.next();
        }
    }

    public void executePhase1(){
        Scanner kb = new Scanner(System.in);
        String keep = "";
        System.out.println(keep);
        for (int i = 0; i < DICE_IN_PLAY; i++){
            hand.add(new YahtzeeDie());
            keep = keep + "n";
        }

        int roll = 1;
        while (roll < 4 && keep.contains("n")) {
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
                keep = kb.nextLine();
            }
            roll++;
        }
    }

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

    //this function returns the total value of all dice in a hand
    int totalAllDice(){
        int total = 0;
        for (YahtzeeDie yD : hand) {
            total += yD.getSideUp();
        }
        return total;
    }

    //bubble sort
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

    public void displayHand(){
        for (int dieNumber = 0; dieNumber < DICE_IN_PLAY; dieNumber++)
            System.out.print(hand.get(dieNumber).getSideUp() + " ");
        System.out.println("\n");
    }
    //this function returns the count of the die value occurring most in the hand
//but not the value itself
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

//this function returns the length of the longest
//straight found in a hand, hand must be sorted beforehand

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

    //this function returns true if the hand is a full house
//or false if it does not
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

