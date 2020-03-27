import org.jetbrains.annotations.NotNull;
import org.w3c.dom.css.RGBColor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * @version 1.33 2007-04-14
 * @author Cay Horstmann
 */
class ImageTest
{
    public static void main(String[] args)
    {

        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                JFrame frame = new HandFrame();
                frame.setTitle("ImageTest");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}

/**
 * A frame with a hand panel image component
 */
class HandFrame extends JFrame {
    private static final int NUM_SIDES = 6;
    private static final int DICE_IN_PLAY = 5;

    YahtzeeHand yahtzeeHand = new YahtzeeHand(NUM_SIDES,DICE_IN_PLAY);
    public HandFrame() {
        for (int i = 0; i < DICE_IN_PLAY; i++) {
            yahtzeeHand.addToHand(new YahtzeeDie());
        }
        Toolkit kit =Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        setSize(screenSize.width/2, screenSize.height/2);
        setLocationByPlatform(true);
        add(yahtzeeHand.new HandPanel());
        pack();
    }
}

