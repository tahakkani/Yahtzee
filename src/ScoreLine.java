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
 * @version 1.0 2/5/2020
 * @see ScoreCard
 */

public class ScoreLine {

    private final String title;
    private int scoreValue;
    private boolean isUsed;

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

    public void setUsed(boolean used){ isUsed = used; }

    public boolean isUsed() { return isUsed; }
    /**
     * Displays the score on a single line in a readable and interpretable manner.
     */
    public void displayLine(){
        System.out.println("Score " + getScoreValue() + " on the " + getTitle() + " line");
    }
}