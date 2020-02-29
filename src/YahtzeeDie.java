/**
 * This class extends ObjectOfChance to apply specifically to Yahtzee dice. Yahtzee dice need the additional
 * field to determine whether to keep the die or not (isKept). This class comes with setters and getters for the
 * isKept boolean
 *
 * CPSC 224
 * Assignment #1
 * No sources to cite, other than the starter files provided by Dr. Worobec,
 * specifically for this assignment.
 *
 * @author Taha Hakkani
 * @version 1.0 2/28/2020
 * @see ObjectOfChance
 */
public class YahtzeeDie extends ObjectOfChance{

    private boolean isKept = false;

    public YahtzeeDie(){
        setKept(true);
    }
    /**
     * Getter for <code>boolean</code> isKept.
     *
     * @return if the YahtzeeDie is to be kept.
     */
    public boolean isKept() { return isKept; }

    /**
     * Setter for <code>boolean</code> isKept.
     *
     * @param kept this is the boolean that will be assigned.
     */
    public void setKept(boolean kept) { isKept = kept; }

}
