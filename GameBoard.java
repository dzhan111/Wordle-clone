package org.cis1200.wordle;

import org.cis1200.FileLineIterator;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Paths;
import java.util.TreeSet;
import javax.swing.*;

public class GameBoard extends JPanel {

    final static private int NUM_ROWS = 6;
    final static private int NUM_COLS = 5;
    private int turns = 0;
    public static final int COURT_WIDTH = 500;
    public static final int COURT_HEIGHT = 600;

    private JLabel status;

    private TreeSet<String> words; // the words the users have put
    private TreeSet<String> letters; // the current letters inputted
    final private boolean gameOver;
    private Wordle wordle;
    final private Grid[][] grid = new Grid[6][5];

    public GameBoard(JLabel status) {

        setBorder(BorderFactory.createLineBorder(Color.darkGray));

        this.status = status;
        gameOver = false;

        wordle = new Wordle(); // initializes model for the game
        wordle.randomNewWord();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() <= 'z' && e.getKeyChar() >= 'a') {
                    String s = String.valueOf(e.getKeyChar());
                    wordle.takeTurn(s);
                    // updateStatus();
                    repaint();

                } else if (e.getKeyChar() == '\u0008' || e.getKeyChar() == '\u0008') {
                    wordle.delete();
                    // updateStatus();
                    repaint();
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    wordle.enter();
                    updateStatus();
                    repaint();
                }
            }
        });
        reset();

    }

    public void reset() {
        wordle.reset();
        status.setText("game running");
        repaint();
        requestFocusInWindow();

    }

    private void updateStatus() {
        if (wordle.getRow() > 0 && wordle.checkWinner(wordle.getRow() - 1) == 0) {
            status.setText("You won!");
        } else if (wordle.getRow() == 5) {
            if (wordle.checkWinner(wordle.getRow()) == 1) {
                status.setText("You lost! The word was " + wordle.getTargetWord());
            }
            if (wordle.checkWinner(wordle.getRow()) == 0) {
                status.setText("You won! (barely)");
            }
        } else {
            status.setText("game running");
        }
        System.out.println("game over = " + wordle.isGameOver());
        System.out.println("-----------------------------------");
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {

                String state = wordle.getCell(i, j);
                Color color = wordle.getColor(i, j);
                grid[i][j] = new Grid(i, j, COURT_WIDTH / 5, (COURT_HEIGHT) / 6);
                grid[i][j].setColor(color);
                grid[i][j].setLetter(state);
                grid[i][j].draw(g);

            }
        }

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }

    public void save() {
        File file = Paths.get("files/save").toFile();
        BufferedWriter bw;

        try {
            bw = new BufferedWriter(new FileWriter(file, false));
            // all guesses + current word
            // figure out how to access word
            bw.write(wordle.getTargetWord());
            bw.newLine();
            for (String s : wordle.getGuesses()) {
                bw.write(s);
                bw.newLine();
                System.out.println("Saved: " + s);
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        status.setText("save successful");

    }

    public void load() {
        File file = Paths.get("files/save").toFile();
        FileLineIterator fl = new FileLineIterator(file.getPath());

        wordle.reset();
        try {
            wordle.setTarget(fl.next());
            System.out.println("target set");
            while (fl.hasNext()) {
                String s = fl.next();
                for (char c : s.toCharArray()) {
                    wordle.takeTurn(String.valueOf(c));
                }
                wordle.enter();
                repaint();


            }
            status.setText("game loaded");
        }catch (RuntimeException e){
            throw new RuntimeException();
        }

        repaint();
        requestFocusInWindow();

    }

    public void newWord() {
        wordle.reset();
        wordle.randomNewWord();
        status.setText("game running");
        System.out.println("New word: " + wordle.getTargetWord());
        repaint();
        requestFocusInWindow();

    }

    // -------------------JUnit testing purposes only!!-------------------
    public Wordle getWordle() {
        return wordle;
    }
}
