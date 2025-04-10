package Exercise.Ex2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class EX2_PM_3_2 extends JFrame implements KeyListener {

    private final int gridSize;
    private final char[][] grid;
    private int x, y;
    private final int cellSize = 30;

    public EX2_PM_3_2(int size) {
        this.gridSize = size;
        this.grid = new char[size][size];
        this.x = size / 2;
        this.y = size / 2;

        setTitle("Grid Game");
        setSize(size * cellSize + 20, size * cellSize + 40);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addKeyListener(this);
        initGrid();
    }

    private void initGrid() {
        for (int i = 0; i < gridSize; i++)
            for (int j = 0; j < gridSize; j++)
                grid[i][j] = (i == 0 || i == gridSize - 1 || j == 0 || j == gridSize - 1) ? '*' : ' ';

        grid[x][y] = 'X';
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        drawGrid(g);
    }

    private void drawGrid(Graphics g) {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                int px = j * cellSize + 10;
                int py = i * cellSize + 30;

                if (grid[i][j] == '*') {
                    g.setColor(Color.BLACK);
                    g.fillRect(px, py, cellSize, cellSize);
                } else if (grid[i][j] == 'X') {
                    g.setColor(Color.BLUE);
                    g.fillRect(px, py, cellSize, cellSize);
                } else {
                    g.setColor(Color.WHITE);
                    g.fillRect(px, py, cellSize, cellSize);
                }

                g.setColor(Color.GRAY);
                g.drawRect(px, py, cellSize, cellSize);
            }
        }
    }

    private void move(char input) {
        int newX = x + (input == 'w' ? -1 : input == 's' ? 1 : 0) + (input == 'W' ? -1 : input == 'S' ? 1 : 0);
        int newY = y + (input == 'a' ? -1 : input == 'd' ? 1 : 0) + (input == 'A' ? -1 : input == 'D' ? 1 : 0);

        if (grid[newX][newY] != '*') {
            grid[x][y] = ' ';
            x = newX;
            y = newY;
            grid[x][y] = 'X';
        } else {
            JOptionPane.showMessageDialog(this, "Hitting the wall!", "Game Over", JOptionPane.WARNING_MESSAGE);
            System.exit(0);
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        char c = e.getKeyChar();
        if ("wasd".indexOf(c) >= 0) {
            move(c);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        int size = 10;
        EX2_PM_3_2 game = new EX2_PM_3_2(size);
        game.setVisible(true);
    }
}
