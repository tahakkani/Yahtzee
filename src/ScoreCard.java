/**
 * This class implements a Yahtzee score card. It is composed of several scorelines (both in ArrayList and individual
 * form) as well as upper and lower scorecard 'total's (<code>int</code>). There are also <code>int</code>s to keep
 * track of how many bonus Yahtzees have been scored
 *
 * CPSC 224
 * Assignment #1
 * No sources to cite.
 *
 * @author Taha Hakkani
 * @version 1.0 2/5/2020
 * @see ScoreLine
 */

import java.util.ArrayList;

public class ScoreCard {

    //The following fields constitute the lines that can be scored on in a Yahtzee scorecard
    private ArrayList<ScoreLine> upperSection = new ArrayList<>();
    private ArrayList<ScoreLine> ofAKinds = new ArrayList<>();
    private ScoreLine fullHouse = new ScoreLine("Full House", 0);
    private ArrayList<ScoreLine> straights = new ArrayList<>();
    private ScoreLine yahtzee = new ScoreLine("YAHTZEE", 0);
    private ScoreLine chance = new ScoreLine("Chance", 0);

    //This field keeps track of how many extra Yahtzees are scored, adding 100 points each
    private int bonusYahtzees = 0;

    // the following fields reflect scorecard totals
    private int totalUpper = 0;
    private int totalLower = 0;
    private int grandTotal = 0;

    public ArrayList<ScoreLine> getUpperSection(){return upperSection;}
    public ArrayList<ScoreLine> getOfAKinds(){return ofAKinds;}
    public ArrayList<ScoreLine> getStraights(){return straights;}
    public ScoreLine getFullHouse(){return  fullHouse;}
    public ScoreLine getYahtzee(){return  yahtzee;}
    public ScoreLine getChance(){return chance;}

    public static void main(String[] args) {
        int dice = 10;
        ScoreCard myCard = new ScoreCard(dice, 9);

        myCard.displayCard(dice);

    }

    public ScoreCard(int diceInPlay, int sidesOnDice){
        for(int i = 1; i < sidesOnDice + 1; i++)
            upperSection.add(new ScoreLine(i + "", 0));
        for(int i = 3; i < diceInPlay; i++)
            ofAKinds.add(new ScoreLine(i + " of a Kind", 0));
        for(int i = 4; i <= Math.min(diceInPlay, sidesOnDice); i++)
            straights.add(new ScoreLine("Sequence of " + i, 0));
    }

    public void displayCard(int diceInPlay){
            //upper scorecard
            for (ScoreLine sL : getUpperSection())
                sL.displayLine();
            for (ScoreLine sL : getOfAKinds())
                sL.displayLine();
            if (diceInPlay >= 5) //if full house is possible
                getFullHouse().displayLine();
            for (ScoreLine sL : getStraights())
                sL.displayLine();
            getYahtzee().displayLine();
            getChance().displayLine();
    }

    public void calc_totalUpper(){
        for (ScoreLine sL : upperSection)
            totalUpper += sL.get_scoreValue();
        if (totalUpper > 62)
            addUpperBonus();
    }

    public void calc_totalLower(){
        for (ScoreLine sL : ofAKinds)
            totalLower += sL.get_scoreValue();
        totalLower += fullHouse.get_scoreValue();
        for (ScoreLine sL : straights)
            totalLower += sL.get_scoreValue();
        totalLower += yahtzee.get_scoreValue();
        totalLower += chance.get_scoreValue();
        for (int i = 0; i < bonusYahtzees; i++)
            addYahtzeeBonus();
    }

    public void calc_grandTotal(){
        grandTotal = totalLower + totalUpper;
    }

    private void addUpperBonus() {
        totalUpper += 35;
    }

    private void addYahtzeeBonus(){
        totalLower += 100;
    }
}
