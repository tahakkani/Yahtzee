/**
 * This class is the backbone of the ScoreCard class. Having a separate ScoreLine class allows for a dynamic
 * construction of score cards based on a number of factors (number of sides on the YahtzeeDie, how many dice are
 * in play, etc.). This class offers different constructors based on whether the score will be provided.
 *
 * CPSC 224
 * Assignment #1
 * No sources to cite.
 *
 * @author Taha Hakkani
 * @version 1.0 2/28/2020
 * @see ScoreCard
 */

public class ScoreLine {

    private final String title;
    private int scoreValue;
    private boolean isUsed;
    protected static final String displayFormat = "%-20s %d %n";

    /**
     * Explicit value constructor for ScoreLine when no score is provided.
     *
     * @param aTitle title of the line (e.g. "Yahtzee" or "Three of a Kind")
     */
    public ScoreLine (String aTitle){
        title = aTitle; scoreValue = 0; isUsed = false;
    }

    /**
     * Explicit value constructor for ScoreLine. Accepts whats needed for a ScoreLine (<code>String</code>
     * and <code>int</code>)
     *
     * @param aTitle This is the title of the line ("Yahtzee", "Three of a Kind", "Ones", etc.)
     * @param aScore This is the score at that line
     */
    public ScoreLine (String aTitle, int aScore){
        title = aTitle;
        scoreValue = aScore;
        isUsed = false;
    }

    /**
     * Explicit value constructor for ScoreLine. Accepts whats needed for a ScoreLine (<code>String</code>,
     * <code>boolean</code> and <code>int</code>)
     *
     * @param aTitle is the name of the line, displayed to user
     * @param used keeps track of if the line has been used already
     * @param aScore tracks the score at this line
     */
    public ScoreLine (String aTitle,  boolean used, int aScore){
        title = aTitle;
        scoreValue = aScore;
        isUsed = used;
    }

    /**
     * Setter for the <code>int</code> scoreValue.
     *
     * @param	aScore this is the score that will be assigned.
     * @see	"No Borrowed Code"
     */
    public void setScoreValue(int aScore){
        scoreValue = aScore;
    }

    /**
     * Getter for <code>int</code> scoreValue.
     *
     * @return the score at this line.
     */
    public int getScoreValue(){
        return scoreValue;
    }

    /**
     * Getter for <code>String</code> title.
     *
     * @return the title of this score line.
     */
    public String getTitle(){
        return title;
    }

    /**
     * Resets the scoreValue field to 0, can reset isUsed field to false, if needed
     *
     * @param resetUsed true if you want to reset the isUsed field, false otherwise
     */
    public void reset(boolean resetUsed){
        scoreValue = 0;
        if (resetUsed)
            isUsed = false;
    }

    /**
     * Setter for isUsed field.
     *
     * @param used is a boolean dictating whether the line is used or not
     */
    public void setUsed(boolean used){ isUsed = used;}

    /**
     * Getter for isUsed field.
     *
     * @return whether the current line has already been used
     */
    public boolean isUsed() { return isUsed; }
    /**
     * Displays the possible score on a single line in a readable and interpretable manner.
     *
     * @see ScoreCard displayPossibilities()
     */
    public void displayLineOffer(){
        System.out.println("Score " + scoreValue + " on the " + title + " line");
    }

    /**
     * Used for displaying the player's official scorecard.
     *
     * @see ScoreCard displayFullCard()
     */
    public void displayLineForFullCard(){
        System.out.printf(displayFormat,title, scoreValue);
    }


}