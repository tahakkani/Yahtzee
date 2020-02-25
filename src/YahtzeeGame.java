import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class YahtzeeGame {

    private static int DICE_IN_PLAY;
    private static int ROLLS_PER_TURN;
    private ArrayList<YahtzeePlayer> players = new ArrayList<>();

    public static void main(String[] args) {
        YahtzeeGame newGame = new YahtzeeGame();
        newGame.playGame();
    }

    public YahtzeeGame(){
        ArrayList<Integer> config = gameInit();
        //set values that user chose/left as is
        YahtzeeDie.NUM_SIDES = config.get(0);
        DICE_IN_PLAY = config.get(1);
        ROLLS_PER_TURN = config.get(2);
        int PLAYERS = 1;
        for (int i = 0; i < PLAYERS; i++)
            players.add(new YahtzeePlayer(YahtzeeDie.NUM_SIDES, DICE_IN_PLAY));
    }

    public void playGame(){
        boolean gameOver = false;
        //do this while players still have stuff in their scorecards
        while (!gameOver) {
            for (YahtzeePlayer player : players){
                if (!player.getScores().isFull()){
                    player.takeTurn(DICE_IN_PLAY, ROLLS_PER_TURN);
                }
                else gameOver = true;
            }
        }
    }

    /**
     * Finds the file, opens it, reads the values and
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
        }   catch (FileNotFoundException e) {
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
}
