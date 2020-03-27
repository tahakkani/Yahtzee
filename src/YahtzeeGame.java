
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

/**
 * This class is a Yahtzee game, whose member fields are the number of dice in play, rolls per turn, and
 * YahtzeePlayers (an ArrayList of YahtzeeDie). This class is responsible for initializing the game using outside
 * text files.
 *
 * CPSC 224
 * Assignment #3
 * No sources to cite, other than the starter files provided by Dr. Worobec,
 * specifically for this assignment.
 *
 * @author Taha Hakkani
 * @version 1.0 2/28/2020
 * @see YahtzeeDie
 * @see ScoreCard
 */
public class YahtzeeGame {

    private static int DICE_IN_PLAY = 5;
    private static int ROLLS_PER_TURN = 3;
    private static int NUM_SIDES = 6;
    private ArrayList<YahtzeePlayer> players = new ArrayList<>();

    public static void main(String[] args) {
        YahtzeeGame newGame = new YahtzeeGame();
        //newGame.playGame();
    }

    /**
     * Constructor for a Yahtzee Game. Initializes the game by setting and creates new YahtzeePlayers.
     */
    public YahtzeeGame(){
        //ArrayList<Integer> config = gameInit();
        gameInitGUI();
    }

    /**
     * Has each player take a turn in order until each scorecard is full.
     */
    public void playGame(){
        YahtzeeDie.NUM_SIDES = NUM_SIDES;
        int PLAYERS = 1;
        for (int i = 0; i < PLAYERS; i++)
            players.add(new YahtzeePlayer(YahtzeeDie.NUM_SIDES, DICE_IN_PLAY));
        boolean gameOver = false;
        //do this while players still have stuff in their scorecards
        for (YahtzeePlayer player : players) {
            if (!player.getScores().isFull())
                player.takeTurn(DICE_IN_PLAY, ROLLS_PER_TURN);
        }
        for (YahtzeePlayer plyr: players)
            plyr.getScores().reset(true);
    }

    private void gameInitGUI(){
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
    /**
     * Finds the file, opens it, reads the config values and asks user of they would like to change them. If so
     *
     * @return the config values to be passed, as an ArrayList
     */
    private ArrayList<Integer> gameInit() {
        //load data from "yahtzeeConfig.txt" into an ArrayList
        Scanner inFile;
        ArrayList<Integer> configValues = new ArrayList<>();
        Scanner kb = new Scanner(System.in);

        try {
            inFile = new Scanner(new File("yahtzeeConfig.txt"));
            while (inFile.hasNextLine()) {
                configValues.add(Integer.parseInt(inFile.nextLine()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File not found");
        }

        //display data from "yahtzeeConfig.txt" in an easy-to-read manner
        System.out.println(configValues);
        System.out.println("You are playing with " + configValues.get(1) + " " + configValues.get(0) + "-sided dice");
        System.out.println("You get " + configValues.get(2) + " rolls per hand\n");
        System.out.println("Enter 'y' if you would like to change the configurations");

        String editConfig = kb.nextLine();

        //if the user wants to edit the config, allow them to enter values for die sides, die number, and
        //rolls per hand
        if (Objects.equals(editConfig, "y")) {
            configValues.clear();
            System.out.println("Enter the number of sides you want on each die: ");

            try {
                configValues.add(Integer.parseInt(kb.nextLine()));
            } catch (NumberFormatException e) {
                System.out.println("That is not a valid entry");
            }
            System.out.println("Enter the number of dice in play: ");
            configValues.add(Integer.parseInt(kb.nextLine()));
            System.out.println("Enter the number of rolls per hand: ");
            configValues.add(Integer.parseInt(kb.nextLine()));

            //write new values to txt file
            PrintStream outFile = null;
            try {
                outFile = new PrintStream(new File("yahtzeeConfig.txt"));
            } catch (FileNotFoundException e) {
                System.out.println("File not found");
                e.printStackTrace();
            }
            for (Integer config : configValues)
                Objects.requireNonNull(outFile).println(config);
            Objects.requireNonNull(outFile).close();
        }
        return configValues;
    }

    /**
     * A frame with a sample text label and a combo box for selecting font faces.
     */
    public class InitializeFrame extends JFrame {

        // make a combo box and add face names

        private JComboBox<Integer> diceInPlayCombo = new JComboBox<>();
        private JComboBox<Integer> numSidesCombo = new JComboBox<>();
        private JButton go = new JButton("Go!");

        private static final int DEFAULT_SIZE = 24;

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
                    dispose();
                    playGame();
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
}
