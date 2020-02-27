import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;

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
    private ScoreLine upperBonus = new ScoreLine("Upper Bonus:",0);
    private ArrayList<ScoreLine> ofAKinds = new ArrayList<>();
    private ScoreLine fullHouse = null;
    private ArrayList<ScoreLine> straights = new ArrayList<>();
    private ScoreLine yahtzee = new ScoreLine("YAHTZEE", 0);
    private ScoreLine chance = new ScoreLine("Chance", 0);
    private ScoreLine yahtzeeBonus = new ScoreLine("YAHTZEE BONUS", 0);

    //This field keeps track of how many extra Yahtzees are scored, adding 100 points each
    private int bonusYahtzees = 0;
    // the following fields reflect scorecard totals
    private int totalUpper = 0;
    private int totalLower = 0;
    private int grandTotal = 0;

    //used to delimit parts of ScoreCard text file
    private String delim = ",";

    public int getBonusYahtzees(){ return bonusYahtzees; }

    public void addBonusYahtzee(){ bonusYahtzees += 1; }

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
     * for each line
     */
    public void readScorecardTxt (){
        //load data from "scorecard.txt" into a Scorecard
        Scanner inFile;

        try {
            inFile = new Scanner(new File("scorecard.txt")).useDelimiter("\\r\\n");
            Pattern p = Pattern.compile("[\\w\\s]*,[a-zA-Z_0-9]*,[0-9]*");
            for(ScoreLine scoreLine : upperSection) {
                String[] my = inFile.next(p).split(delim);
                upperSection.set(upperSection.indexOf(scoreLine), new ScoreLine(my[0],
                        Boolean.parseBoolean(my[1]), Integer.parseInt(my[2])));
            }
            //System.out.println(inFile.next(p).split(delim));
            for(ScoreLine scoreLine : ofAKinds) {
                String[] lns = inFile.next(p).split(delim);
                ofAKinds.set(ofAKinds.indexOf(scoreLine), new ScoreLine(lns[0],
                        Boolean.parseBoolean(lns[1]), Integer.parseInt(lns[2])));
            }
            String[] fH = inFile.next(p).split(delim);
            fullHouse = new ScoreLine(fH[0], Boolean.parseBoolean(fH[1]), Integer.parseInt(fH[2]));
            for(ScoreLine scoreLine : straights) {
                String[] strght = inFile.next(p).split(delim);
                straights.set(straights.indexOf(scoreLine), new ScoreLine(strght[0],
                        Boolean.parseBoolean(strght[1]), Integer.parseInt(strght[2])));
            }
            String[] yaht = inFile.next(p).split(delim);
            yahtzee = new ScoreLine(yaht[0], Boolean.parseBoolean(yaht[1]), Integer.parseInt(yaht[2]));
            String[] chnc = inFile.next(p).split(delim);
            chance = new ScoreLine(chnc[0], Boolean.parseBoolean(chnc[1]), Integer.parseInt(chnc[2]));

        }   catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File not found");
        }
    }

    public void updateScorecardTxt(){
        //load data from scores into "scorecard.txt"
        PrintStream outFile = null;

        try {
            outFile = new PrintStream(new File("scorecard.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //maybe clear file before writing
        for (ScoreLine sL : getUpperSection())
            Objects.requireNonNull(outFile).println(sL.getTitle() + delim + sL.isUsed() + delim + sL.getScoreValue());
        for (ScoreLine sL : getOfAKinds())
            Objects.requireNonNull(outFile).println(sL.getTitle() + delim + sL.isUsed() + delim + sL.getScoreValue());
        if(getFullHouse() != null)
            Objects.requireNonNull(outFile).println(getFullHouse().getTitle() + delim + getFullHouse().isUsed()
                    + delim + getFullHouse().getScoreValue());
        for (ScoreLine sL : getStraights())
            Objects.requireNonNull(outFile).println(sL.getTitle() + delim + sL.isUsed() + delim + sL.getScoreValue());

        Objects.requireNonNull(outFile).println(getYahtzee().getTitle() + delim + getYahtzee().isUsed()
                + delim + getYahtzee().getScoreValue());
        Objects.requireNonNull(outFile).println(getChance().getTitle() + delim + getChance().isUsed()
                + delim + getChance().getScoreValue());
        Objects.requireNonNull(outFile).close();
    }
    public boolean isFull(){
        for (ScoreLine sL : getUpperSection())
            if (!sL.isUsed()) return false;
        for (ScoreLine sL : getOfAKinds())
            if (!sL.isUsed()) return false;
        if(getFullHouse() != null)
            if (!getFullHouse().isUsed()) return false;
        for (ScoreLine sL : getStraights())
            if (!sL.isUsed()) return false;
        if (!getYahtzee().isUsed()) return false;
        return getChance().isUsed();
    }
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
     * Displays the entire card in an easily comprehensible format for the user.
     *
     */
    public void displayPossibilities() {
        //upper scorecard
        for (ScoreLine sL : getUpperSection())
            if (!sL.isUsed())
                sL.displayLineOffer();
        for (ScoreLine sL : getOfAKinds())
            if (!sL.isUsed())
                sL.displayLineOffer();
        if (getFullHouse() != null && !getFullHouse().isUsed()) //if full house is possible
            getFullHouse().displayLineOffer();
        for (ScoreLine sL : getStraights())
            if (!sL.isUsed())
                sL.displayLineOffer();
        if (!getYahtzee().isUsed())
            getYahtzee().displayLineOffer();
        else
            getYahtzeeBonus().displayLineOffer();
        if (!getChance().isUsed())
            getChance().displayLineOffer();
    }

    public void displayFullCard(){
        String divider = "------------------------";
        String longDivider = "---------------------------------";
        System.out.println("\n" + longDivider);
        System.out.println("UPPER SECTION");
        System.out.println(divider);
        for (ScoreLine sL : getUpperSection())
            sL.displayLineForFullCard();
        upperBonus.displayLineForFullCard();
        calcTotalUpper();
        System.out.printf(ScoreLine.displayFormat, "UPPER TOTAL:", totalUpper);
        System.out.println(divider);
        System.out.println("\nLOWER SECTION");
        System.out.println(divider);
        for (ScoreLine sL : getOfAKinds())
            sL.displayLineForFullCard();
        if (getFullHouse() != null) //if full house is possible
            getFullHouse().displayLineForFullCard();
        for (ScoreLine sL : getStraights())
            sL.displayLineForFullCard();
        getYahtzee().displayLineForFullCard();
        getChance().displayLineForFullCard();
        calcYahtzeeBonus();
        getYahtzeeBonus().displayLineForFullCard();
        calcTotalLower();
        System.out.printf(ScoreLine.displayFormat,"LOWER TOTAL:", totalLower);
        System.out.println(divider);
        calcGrandTotal();
        System.out.printf(ScoreLine.displayFormat,"GRAND TOTAL:", grandTotal);
        System.out.println(longDivider + "\n");
    }

    public void calcYahtzeeBonus(){
        yahtzeeBonus.setScoreValue(bonusYahtzees * 100);
    }
    /**
     * Calculates the total of the entire upper section of the scorecard, used for determining if a bonus is earned
     */
    public void calcTotalUpper(){
        int total = 0;
        for (ScoreLine sL : upperSection)
            total += sL.getScoreValue();
        totalUpper = total;
        if (totalUpper > 62)
            totalUpper+= 35;
    }

    /**
     * Calculates the total of the entire lower section of the scorecard.
     */
    public void calcTotalLower(){
        int total = 0;
        for (ScoreLine sL : ofAKinds)
            total += sL.getScoreValue();
        total += fullHouse.getScoreValue();
        for (ScoreLine sL : straights)
            total += sL.getScoreValue();
        total += yahtzee.getScoreValue();
        total += chance.getScoreValue();
        for (int i = 0; i < bonusYahtzees; i++)
            total += 100;
        totalLower = total;
    }

    /**
     * Calculates the total of the entirety of the scorecard
     */
    public void calcGrandTotal(){
        grandTotal = totalLower + totalUpper;
    }

    public ScoreLine findLine(String title){
        for (ScoreLine sL : getUpperSection())
            if (title.equals(sL.getTitle()) && !sL.isUsed()) return sL;
        for (ScoreLine sL : getOfAKinds())
            if (title.equals(sL.getTitle()) && !sL.isUsed()) return sL;
        if(getFullHouse() != null)
            if (title.equals(getFullHouse().getTitle()) && !getFullHouse().isUsed()) return getFullHouse();
        for (ScoreLine sL : getStraights())
            if (title.equals(sL.getTitle()) && !sL.isUsed()) return sL;
        if (title.equals(getYahtzee().getTitle()) && !getYahtzee().isUsed()) return getYahtzee();
        if (title.equals(getChance().getTitle()) && !getChance().isUsed()) return getChance();
        if (title.equals(getYahtzeeBonus().getTitle())) return getYahtzeeBonus();
        return null;
    }

    /**
     * This resets the values of the whole scorecard to 0 AND resets
     */
    public void reset(boolean resetUsed){
        for (ScoreLine sL : getUpperSection())
            sL.reset(resetUsed);
        for (ScoreLine sL : getOfAKinds())
            sL.reset(resetUsed);
        if(getFullHouse() != null)
            getFullHouse().reset(resetUsed);
        for (ScoreLine sL : getStraights()){
            sL.reset(resetUsed);
        }
        getYahtzee().reset(resetUsed);
        getChance().reset(resetUsed);
    }

    public ScoreLine getYahtzeeBonus() {
        return yahtzeeBonus;
    }
}
