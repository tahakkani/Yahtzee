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


    public static void main(String[] args) {
        ScoreLine mysL = new ScoreLine("Yooo", 15);
        mysL.displayLine();
    }

    /**
     * Explicit value constructor for ScoreLine when no score is provided.
     *
     * @param aTitle title of the line (e.g. "Yahtzee" or "Three of a Kind")
     */
    public ScoreLine (String aTitle){
        title = aTitle; scoreValue = 0;
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
    }

    /**
     * Setter for the <code>int</code> scoreValue.
     *
     * @param	aScore this is the score that will be assigned.
     * @see	"No Borrowed Code"
     */
    public void set_scoreValue(int aScore){
        scoreValue = aScore;
    }

    /**
     * Getter for <code>int</code> scoreValue.
     *
     * @return the score at this line.
     */
    public int get_scoreValue(){
        return scoreValue;
    }

    /**
     * Getter for <code>String</code> title.
     *
     * @return the title of this score line.
     */
    public String get_title(){
        return title;
    }

    public void displayLine(){
        System.out.println("Score " + get_scoreValue() + " on the " + get_title() + " line");
    }
}