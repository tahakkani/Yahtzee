import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Array;

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
        setKept(false);
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

    /**
     * A component that displays an image
     */

    public class ImageComponent extends JComponent {

        private Image image;
        private Color color = Color.WHITE;

        public ImageComponent() {
            // The MouseListener that handles the click, etc.
            MouseListener listener = new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    isKept = !isKept;
                    if (isKept)
                        color = Color.RED;
                    else
                        color = Color.WHITE;
                    repaint();
                }
            };
            addMouseListener(listener);
            setImage();
        }

        public void setImage() {
            image = new ImageIcon((sideUp + ".gif")).getImage();
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);     // paint parent's background
            if (image == null) return;
            g.drawImage(image, 1,1,null);
            // Your custom painting codes. For example,
            // Drawing primitive shapes
            g.setColor(color);       // change the drawing color
            g.drawRect(0, 0, image.getWidth(null) + 1 , image.getHeight(null) + 1);
        }

        public Dimension getPreferredSize() {
            return new Dimension(image.getWidth(null) + 8, image.getHeight(null) + 8);
        }
    }
}
