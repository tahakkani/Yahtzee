import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


class InitializeFrameTest {
    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                JFrame frame = new InitializeFrame();
                frame.setTitle("InitializeFrame.InitializeFrameTest");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}

/**
 * A frame with a sample text label and a combo box for selecting font faces.
 */
public class InitializeFrame extends JFrame {

    // make a combo box and add face names

    private JComboBox<Integer> diceInPlayCombo = new JComboBox<>();
    private JComboBox<Integer> numSidesCombo = new JComboBox<>();
    private JLabel label;
    private JButton go = new JButton("Go!");
    private static final int DEFAULT_SIZE = 24;

    private static int NUM_SIDES;
    private static int DICE_IN_PLAY;

    public InitializeFrame() {
        // add the sample text label

        diceInPlayCombo.addItem(5);
        diceInPlayCombo.addItem(6);
        diceInPlayCombo.addItem(7);
        numSidesCombo.addItem(6);
        numSidesCombo.addItem(8);
        numSidesCombo.addItem(12);

        // the combo box listener changes the label font to the selected face name

        diceInPlayCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                DICE_IN_PLAY = diceInPlayCombo.getItemAt(diceInPlayCombo.getSelectedIndex());
                System.out.println(DICE_IN_PLAY);
            }
        });

        numSidesCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                NUM_SIDES = numSidesCombo.getItemAt(numSidesCombo.getSelectedIndex());
                System.out.println(NUM_SIDES);
            }
        });
        go.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });

        // add combo box to a panel at the frame's southern border

        JPanel comboPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        comboPanel.add(new JLabel("How many dice would you like to play with? "));
        comboPanel.add(diceInPlayCombo);
        comboPanel.add(new JLabel("How many sides on each die? "));
        comboPanel.add(numSidesCombo);
        add(comboPanel, BorderLayout.NORTH);
        buttonPanel.add(go);
        add(buttonPanel, BorderLayout.SOUTH);
        pack();
    }
}