package org.cis1200.wordle;

// imports necessary libraries for Java swing
import java.awt.*;
import javax.swing.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class RunWordle implements Runnable {
    public void run() {
        final JFrame frame = new JFrame("Wordle");
        frame.setLocation(525, 150);

        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Running...");
        status_panel.add(status);

        final GameBoard game = new GameBoard(status);
        frame.add(game, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset button, we
        // define it as an anonymous inner class that is an instance of
        // ActionListener with its actionPerformed() method overridden. When the
        // button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> game.reset());
        control_panel.add(reset);

        final JButton newWord = new JButton("New Word");
        newWord.addActionListener(e -> game.newWord());
        control_panel.add(newWord);

        // implement
        final JButton save = new JButton("Save");
        save.addActionListener(e -> game.save());
        control_panel.add(save);

        final JButton load = new JButton("Load");
        load.addActionListener(e -> game.load());
        control_panel.add(load);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        game.reset();
        JOptionPane.showMessageDialog(
                null,
                "This is Wordle! Try and guess the target\n word by guessing " +
                        "different words. " +
                        "\n Letters in the correct spot will appear green," +
                        "\n letters in the wrong location will appear yellow," +
                        "\n and letters not present in the target word will be grey. " +
                        "\nIf you play a new game with the same word, press reset." +
                        "\n If you want to save a specific game and reload it later," +
                        "\n use the save/load buttons accordingly. " +
                        "\n you win if you manage to guess the word," +
                        "\n you lose if you don't get it in 6 guesses. Have fun!",
                "Instructions", 1
        );

    }
}
