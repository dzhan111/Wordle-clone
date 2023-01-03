package org.cis1200.wordle;

import org.cis1200.FileLineIterator;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Paths;
import java.util.*;


public class Wordle {

    private Grid[][] board;
    private int numTurns = 0;
    private boolean gameOver;
    private String target = "joust";

    private boolean canGuess = true;

    private int row = 0;
    private int col = 0;
    private ArrayList<String> set = new ArrayList<>();
    private Queue<String> guesses = new LinkedList<>();
    private char[] targetArray = target.toCharArray();

    public Wordle() {

        reset();
    }

    void takeTurn(String s) {
        if (numTurns <= 30 && col < 5 && !gameOver) {

            set.add(s);
            board[row][col].setLetter(set.get(col));
            col++;
            numTurns++;
        }
    }

    public void delete() {
        if (numTurns != 0 && col > 0 && !gameOver) {
            col--;
            numTurns--;
            board[row][col].setLetter("");
            set.remove(col);
        }

    }

    public void enter() {

        String guess = "";
        for (int k = 0; k < 5; k++) {
            guess = guess + board[row][k].getLetter();
        }

        if (checkValidWord(guess)) {
            // print game state
            System.out.println("-----------------------------------");
            System.out.println("guess #" + (row + 1));

            if (row < 5) {
                if (col == 5) {

                    targetArray = target.toCharArray();
                    checkGuess(row);
                    set.clear();
                    row++;
                    int i = checkWinner(row - 1);
                    System.out.println("game state: " + i);
                    col = 0;
                }
            } else if (col >= 5 && row == 5 && checkValidWord(guess)) {
                gameOver = true;
                targetArray = target.toCharArray();
                checkGuess(row);
                int i = checkWinner(row);
                System.out.println("game state: " + i);
            }
        } else {
            System.out.println("not valid word");
            if (col == 5) {
                JOptionPane.showMessageDialog(null, "Invalid Word, please try another");
            }
        }

    }

    public void checkGuess(int row) {

        String guess = "";
        for (int k = 0; k < 5; k++) {
            guess = guess + board[row][k].getLetter();
        }

        System.out.println("target: " + target);
        System.out.println("guess : " + guess);
        guesses.offer(guess);

        // iterates through list, checks greens,
        // then removes letter from array so color is permanent
        for (int i = 0; i < 5; i++) {
            char letter = guess.charAt(i);
            if (letter == (target.charAt(i))) { // doesnt get affected by target array
                targetArray[i] = '_';
                board[row][i].setColor(new Color(82, 173, 79)); // lighter green

            }

        }

        for (int i = 0; i < 5; i++) { // iterates through each of guess's letters
            for (int j = 0; j < 5; j++) { // it through each of target's letters
                if (targetArray[j] == guess.charAt(i)) {
                    targetArray[j] = '_';
                    board[row][i].setColor(new Color(201, 201, 64));// mustard yellow
                    break;
                }
            }

        }

        // check greens again,
        for (int i = 0; i < 5; i++) {
            char letter = guess.charAt(i);
            if (letter == (target.charAt(i))) { // doesnt get affected by target array
                targetArray[i] = '_';
                board[row][i].setColor(new Color(82, 173, 79)); // lighter green

            }

        }

        for (int i = 0; i < 5; i++) {
            if (board[row][i].getColor().equals(Color.black)) {
                board[row][i].setColor(Color.darkGray);
            }
        }

    }

    // 0 = guessed right word
    // 1 = ran out of guesses, w/o right word
    // 2 = wrong word, but has more guesses
    public int checkWinner(int r) {

        String guess = "";
        for (int i = 0; i < 5; i++) {
            guess = guess + board[r][i].getLetter();
        }

        if (guess.equals(target)) {
            gameOver = true;
            return 0;
        } else if (r == 5 && col >= 5 && checkValidWord(guess)) {
            gameOver = true;
            return 1;
        } else {
            return 2;
        }

    }

    public void reset() {
        board = new Grid[6][5];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                board[i][j] = new Grid(i, j, 100, 100);
            }
        }
        row = 0;
        col = 0;
        numTurns = 0;
        gameOver = false;
        canGuess = true;
        set.clear();
        guesses.clear();

        // read a random word from a word bank
    }

    public boolean checkValidWord(String s) {
        File file = Paths.get(
                "files/valid-wordle-words.txt"
        ).toFile();
        FileLineIterator fl = new FileLineIterator(file.getPath());

        while (fl.hasNext()) {
            if (fl.next().equals(s)) {
                return true;
            }
        }
        return false;
    }

    public void setTarget(String s) {
        this.target = s;
    }

    public String getCell(int r, int c) {
        return board[r][c].getLetter();
    }

    public Color getColor(int r, int c) {
        return board[r][c].getColor();
    }

    public String getTargetWord() {
        String ans = target;
        return ans;
    }

    public void randomNewWord() {
        Random rand = new Random();
        int bound = 2309;
        int random = rand.nextInt(bound);
        File file = Paths.get(
                "files/word_bank.txt"
        ).toFile();
        FileLineIterator fl = new FileLineIterator(file.getPath());

        for (int i = 0; i < random - 1; i++) {
            fl.next();
        }
        setTarget(fl.next());
        System.out.println("target: " + getTargetWord());
    }

    public Queue<String> getGuesses() {
        return new LinkedList<>(guesses);
    }



    public boolean isGameOver() {
        return gameOver;
    }

    // ------------------------------for testing only------------------------------
    public void setRow(int i) {
        this.row = i;
    }

    public int getRow() {
        int r = row;
        return r;
    }

    public ArrayList<String> getSet() {
        return new ArrayList<>(set);
    }
}
