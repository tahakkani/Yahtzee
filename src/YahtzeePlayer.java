import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Scanner;

/**
 * This class is a Yahtzee player, whose member fields are the player's hand (an ArrayList of YahtzeeDie),
 * the player's scorecard (a series of ScoreLines as well as tallying fields), and a temporary scorecard the displays
 * possible scoring to the user at the end of each turn. This class also has the DICE_IN_PLAY field for the time being,
 * which will probably be passed up to a higher class down the road.
 *
 * CPSC 224
 * Assignment #1
 * No sources to cite, other than the starter files provided by Dr. Worobec,
 * specifically for this assignment.
 *
 * @author Taha Hakkani
 * @version 1.0 2/28/2020
 * @see YahtzeeDie
 * @see ScoreCard
 */
public class YahtzeePlayer {

    private YahtzeeHand hand;
    //This ScoreCard is the player's official score card for the game
    private ScoreCard scores;

    private static int DICE_IN_PLAY = 5;
    private static int ROLLS_PER_TURN = 3;
    private static int NUM_SIDES = 6;

    public YahtzeePlayer(int numSides, int diceInPlay) {
        scores = new ScoreCard(diceInPlay, numSides);
        hand = new YahtzeeHand(numSides, diceInPlay);
        DICE_IN_PLAY = diceInPlay;
        NUM_SIDES = numSides;
    }


    public ScoreCard getScores() {
        return scores;
    }

    /**
     * Asks user if they would like to display their scorecard. If so, displayFullCard() is called
     */
    public void askToDisplayScorecard() {
        System.out.println("Would you like to see your scorecard?");
        Scanner kb = new Scanner(System.in);
        if (kb.nextLine().equals("y"))
            scores.displayFullCard();
    }

    /**
     * Gets input from user (the name of the line they would like to score on). Searches scorecard for that line
     * and if it finds it, updates the score on that line from possible scores scorecard and marks that line
     * as "used". Else, it displays to the user an error message.
     */
    public void scoreOnLine() {
        Scanner kb = new Scanner(System.in);
        boolean lineFound = false;
        while (!lineFound) {
            System.out.println("Please enter the title of the line that you would like to score on: ");
            String entry = kb.nextLine();
            ScoreLine newEntryLine = hand.getPossibleScores().findLine(entry);
            if (newEntryLine != null) {
                if (!newEntryLine.equals(hand.getPossibleScores().getYahtzeeBonus())){
                    scores.findLine(entry).setScoreValue(newEntryLine.getScoreValue());
                    newEntryLine.setUsed(true); //prevents line from being used again
                }
                else
                    scores.addBonusYahtzee();
                lineFound = true;
            } else
                System.out.println("That line was not found! (You may have scored there earlier)");
        }
    }

    private void setScore(ScoreLine sL){
        if (sL.isUsed() && !sL.isGone()) {
            scores.findLine((sL.getTitle())).setScoreValue(sL.getScoreValue());
            sL.setGone(true);
        }
    }
    public void scoreOnLineGUI() {
        //upper scorecard
        for (ScoreLine sL : hand.getPossibleScores().getUpperSection())
            setScore(sL);
        //lower scorecard
        for (ScoreLine sL : hand.getPossibleScores().getOfAKinds())
            setScore(sL);
        setScore(hand.getPossibleScores().getFullHouse()); //if full house is possible
        for (ScoreLine sL : hand.getPossibleScores().getStraights())
            setScore(sL);
        if (hand.getPossibleScores().getYahtzee() != null)
            setScore(hand.getPossibleScores().getYahtzee());
        else if (hand.getPossibleScores().getYahtzee().getScoreValue() != 0)
            setScore(hand.getPossibleScores().getYahtzeeBonus());
        if (hand.getPossibleScores().getChance().isUsed())
            setScore(hand.getPossibleScores().getChance());
    }

    /**
     * Calls a series of other functions from hand and scores that execute a player's full turn
     *
     */
    public void takeTurn() {
        YahtzeeHand.HandFrame handFrame = hand.new HandFrame(ROLLS_PER_TURN);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                PlayerFrame playerFrame = new PlayerFrame();
                playerFrame.setTitle("Player");
                playerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                playerFrame.setVisible(true);
                handFrame.addComponentListener(playerFrame);

            }
        });
        hand.rollingPhaseGUI(DICE_IN_PLAY, ROLLS_PER_TURN, handFrame);

    }

    /**
     * A frame with a hand panel image component
     */
    class PlayerFrame extends JFrame implements ComponentListener {
        JPanel buttonPanel = new JPanel();
        JButton scorecard = new JButton("View scorecard");
        JButton nextRound = new JButton("Next turn");

        public PlayerFrame() {
            scorecard.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    //pull up scorecard frame
                    scoreOnLineGUI();
                    YahtzeePlayer.this.getScores().displayScoreCardGUI(false);
                }
            });

            nextRound.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    scores.displayFullCard();
                    hand.getPossibleScores().displayPossibilities();
                    dispose();
                    takeTurn();
                }
            });
            Toolkit kit =Toolkit.getDefaultToolkit();
            Dimension screenSize = kit.getScreenSize();
            setSize(screenSize.width/2, screenSize.height/2);
            setLocationByPlatform(true);
            buttonPanel.add(scorecard);
            buttonPanel.add(nextRound);
            add(buttonPanel, BorderLayout.SOUTH);
            pack();
        }

        @Override
        public void componentResized(ComponentEvent componentEvent) {

        }

        @Override
        public void componentMoved(ComponentEvent componentEvent) {

        }

        @Override
        public void componentShown(ComponentEvent componentEvent) {

        }

        @Override
        public void componentHidden(ComponentEvent componentEvent) {

        }
    }
}

