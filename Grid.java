package org.cis1200.wordle;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Grid {

    private int rowNum;
    private int colNum;

    private int width;
    private int height;
    private String letter = "";
    private Color color = Color.black;

    public Grid(int row, int col, int width, int height) {
        this.width = width;
        this.height = height;
        rowNum = row - 1;
        colNum = col - 1;

    }

    public void clear() {
        letter = "";
    }

    public void setLetter(String s) {
        letter = s;
    }

    public String getLetter() {
        String l = letter;
        return l;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        Color c = color;
        return c;
    }

    public void draw(Graphics g) {
        // figure out how to change color
        Color font = Color.white;
        g.setFont(g.getFont().deriveFont((float) 40));
        g.setColor(color);
        g.fillRect(width * (colNum + 1), height * (rowNum + 1), width, height);
        g.setColor(Color.darkGray);
        g.drawRect(width * (colNum + 1), height * (rowNum + 1), width, height);

        g.setColor(font);
        g.drawString(
                letter, width * (colNum + 1) + width / 2 - 10,
                height * (rowNum + 1) + height / 2 + 10
        );

    }

}