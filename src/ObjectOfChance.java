import java.util.Random;

/**
 * This class implements any object of chance with a
 * discrete set of possible integers starting with 1 to value of your choice (die, coin, dreidel, whatever).
 * The type of object is set using static final int NUM_SIDES. (NUM_SIDES = 2 corresponds to a coin, perhaps
 * NUM_SIDES = 6 corresponds to a standard die, NUM_SIDES = 4 corresponds to a dreidel)
 *
 * CPSC 224
 * Assignment #1
 * No sources to cite, other than the starter files provided by Dr. Worobec,
 * specifically for this assignment.
 *
 * @author Taha Hakkani
 * @version 1.0 2/5/2020
 * @see YahtzeeDie
 */

public class ObjectOfChance {

    public static final int NUM_SIDES = 6;

    private int sideUp;
    private Random randObj = new Random();

    public ObjectOfChance() {
        setSideUp();
    }

    public static void main(String[] args) {
        ObjectOfChance my = new ObjectOfChance();
        System.out.println(my.sideUp);
    }
    /**
     * @return the integer that the object of chance landed on
     */
    public int getSideUp() { return sideUp; }

    /**
     * Executes the chance using Java's Random Object, returning <code>int</code>
     *
     * @see java.util.Random
     */
    public void setSideUp(){ sideUp = randObj.nextInt(NUM_SIDES) + 1; }

}
