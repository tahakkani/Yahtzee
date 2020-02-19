import java.util.ArrayList;

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

public class ScoreCard {

    //The following fields constitute the lines that can be scored on in a Yahtzee scorecard
    private ArrayList<ScoreLine> upperSection = new ArrayList<>();
    private ArrayList<ScoreLine> ofAKinds = new ArrayList<>();
    private ScoreLine fullHouse = null;
    private ArrayList<ScoreLine> straights = new ArrayList<>();
    private ScoreLine yahtzee = new ScoreLine("YAHTZEE", 0);
    private ScoreLine chance = new ScoreLine("Chance", 0);

    //This field keeps track of how many extra Yahtzees are scored, adding 100 points each
    private int bonusYahtzees = 0;

    // the following fields reflect scorecard totals
    private int totalUpper = 0;
    private int totalLower = 0;
    private int grandTotal = 0;


    /**
     * @return the lines on the upper section of the Yahtzee scorecard (as an ArrayList of ScoreLines)
     */
    public ArrayList<ScoreLine> getUpperSection(){return upperSection;}

    /**
     * @return the lines on the "_ of a Kind" portion of the Yahtzee scorecard (as an ArrayList of ScoreLines)
     */
    public ArrayList<ScoreLine> getOfAKinds(){return ofAKinds;}

    /**
     * @return the lines on the "Sequence of _" section of the Yahtzee scorecard (as an ArrayList of ScoreLines)
     */
    public ArrayList<ScoreLine> getStraights(){return straights;}

    /**
     * @return the line on the "Full House" section of the Yahtzee scorecard (as a ScoreLine)
     */
    public ScoreLine getFullHouse(){return fullHouse;}

    /**
     * @return the line on the "YAHTZEE" section of the Yahtzee scorecard (as a ScoreLine)
     */
    public ScoreLine getYahtzee(){return yahtzee;}

    /**
     * @return the line on the "Chance" section of the Yahtzee scorecard (as a ScoreLine)
     */
    public ScoreLine getChance(){return chance;}

    /**
     * Constructor for a ScoreCard. The card is constructed dynamically as a function of how many dice are in play
     * and how many sides are on a dice. These two parameters dictate what fields will appear on the card.
     *
     * @param diceInPlay is passed to this method from the YahtzeePlayer class.
     * @param sidesOnDice is passed to the method from the YahtzeeDie class.
     */
    public ScoreCard(int diceInPlay, int sidesOnDice){
        for(int i = 1; i < sidesOnDice + 1; i++)
            upperSection.add(new ScoreLine(i + "", 0));
        for(int i = 3; i < diceInPlay; i++)
            ofAKinds.add(new ScoreLine(i + " of a Kind", 0));
        for(int i = 4; i <= Math.min(diceInPlay, sidesOnDice); i++)
            straights.add(new ScoreLine("Sequence of " + i, 0));
        if (diceInPlay >= 5) //if full house is possible
            fullHouse = new ScoreLine("Full House", 0);
    }

    /**
     * Displays the entire card in an easily comprehensible format for the user.
     *
     */
    public void displayCard() {
        //upper scorecard
        for (ScoreLine sL : getUpperSection())
            sL.displayLine();
        for (ScoreLine sL : getOfAKinds())
            sL.displayLine();
        if (getFullHouse() != null) //if full house is possible
            getFullHouse().displayLine();
        for (ScoreLine sL : getStraights())
            sL.displayLine();
        getYahtzee().displayLine();
        getChance().displayLine();
    }

    /**
     * Calculates the total of the entire upper section of the scorecard, used for determining if a bonus is earned
     */
    public void calc_totalUpper(){
        for (ScoreLine sL : upperSection)
            totalUpper += sL.get_scoreValue();
        if (totalUpper > 62)
            addUpperBonus();
    }

    /**
     * Calculates the total of the entire lower section of the scorecard.
     */
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

    /**
     * Calculates the total of the entirety of the scorecard
     */
    public void calc_grandTotal(){
        grandTotal = totalLower + totalUpper;
    }

    /**
     * This method adds a bonus of 35 points to the scorecard once the card is initially totaled. Should only
     * be called at the end of the game.
     */
    private void addUpperBonus() {
        totalUpper += 35;
    }

    /**
     * This adds a bonus of 100 points for every extra Yahtzee that the player gets
     */
    private void addYahtzeeBonus(){
        totalLower += 100;
    }


    public ScoreLine findLine(String title){
        for (ScoreLine sL : getUpperSection())
            if (title.equals(sL.get_title())) return sL;
        for (ScoreLine sL : getOfAKinds())
            if (title.equals(sL.get_title())) return sL;
        if(getFullHouse() != null)
            if (title.equals(getFullHouse().get_title())) return getFullHouse();
        for (ScoreLine sL : getStraights())
            if (title.equals(sL.get_title())) return sL;
        if (title.equals(getYahtzee().get_title())) return getYahtzee();
        if (title.equals(getChance().get_title())) return getChance();
        }

    /**
     * This resets the values of the whole scorecard to 0
     */
    public void reset(){
        for (ScoreLine sL : getUpperSection())
            sL.set_scoreValue(0);
        for (ScoreLine sL : getOfAKinds())
            sL.set_scoreValue(0);
        if(getFullHouse() != null)
            getFullHouse().set_scoreValue(0);
        for (ScoreLine sL : getStraights())
            sL.set_scoreValue(0);
        getYahtzee().set_scoreValue(0);
        getChance().set_scoreValue(0);
    }
}
