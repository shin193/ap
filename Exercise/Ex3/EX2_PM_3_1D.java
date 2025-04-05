package Exercise.Ex3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class EX2_PM_3_1D extends JFrame implements KeyListener {

    Point pacmanPoint = new Point();
    final int width = 300, height = 300, boxSize = 5;
    static int direction = 1;
    final int LEFT = 1, RIGHT = 2, TOP = 3, BOTTOM = 4;
    Point dotPoint = new Point();
    int score = 0;
    final int maxScore = 5;
    final long maxTime = 60_000;
    long startTime;

    public EX2_PM_3_1D() {
        addKeyListener(this);
        pacmanPoint.setLocation((width / boxSize) / 2, (height / boxSize) / 2);
        getNewDotPointLocation();
        setSize(width, height);
        startTime = System.currentTimeMillis();
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        g.clearRect(0, 0, width, height);
        logic();
        drawPacman(g2D);
        drawDotPoint(g2D);
        drawScore(g2D);
        setVisible(true);
    }

    private void drawPacman(Graphics2D g2d) {
        g2d.setColor(Color.BLUE);
        g2d.fillRect(pacmanPoint.x * boxSize, pacmanPoint.y * boxSize, boxSize, boxSize);
    }

    private void drawDotPoint(Graphics2D g2d) {
        g2d.setColor(Color.RED);
        g2d.fillRect(dotPoint.x * boxSize, dotPoint.y * boxSize, boxSize, boxSize);
    }

    private void drawScore(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        String s = "Score: " + score;
        g2d.drawString(s, 25, 50);
    }

    private void logic() {
        long currentTime = System.currentTimeMillis();
        long elapsed = currentTime - startTime;

        if (elapsed > maxTime) {
            JOptionPane.showMessageDialog(this, "Time's up!");
            System.exit(0);
        }

        if (score >= maxScore) {
            JOptionPane.showMessageDialog(this, "Congratulations! You've completed the game.");
            System.exit(0);
        }

        if (dotPoint.x == pacmanPoint.x && dotPoint.y == pacmanPoint.y) {
            score++;
            System.out.println("Score: " + score);
            getNewDotPointLocation();
        }

        movePacman();
    }

    private void movePacman() {
        int xMovement = 1;
        int yMovement = 0;
        switch (direction) {
            case LEFT:
                xMovement = -1;
                yMovement = 0;
                break;
            case RIGHT:
                xMovement = 1;
                yMovement = 0;
                break;
            case TOP:
                xMovement = 0;
                yMovement = -1;
                break;
            case BOTTOM:
                xMovement = 0;
                yMovement = 1;
                break;
            default:
                xMovement = yMovement = 0;
                break;
        }

        int newX = pacmanPoint.x + xMovement;
        int newY = pacmanPoint.y + yMovement;

        pacmanPoint.setLocation(newX, newY);
        handleCrossBorder();
    }

    private void handleCrossBorder() {
        int maxX = width / boxSize;
        int maxY = height / boxSize;

        if (pacmanPoint.x < 0) pacmanPoint.x = 0;
        if (pacmanPoint.y < 0) pacmanPoint.y = 0;
        if (pacmanPoint.x >= maxX) pacmanPoint.x = maxX - 1;
        if (pacmanPoint.y >= maxY) pacmanPoint.y = maxY - 1;
    }

    private void getNewDotPointLocation() {
        Random rand = new Random();
        int delta = boxSize * 2;
        dotPoint.setLocation(rand.nextInt(width / boxSize - 2 * delta) + delta,
                rand.nextInt(height / boxSize - 2 * delta) + delta);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP)
            direction = 3;
        else if (e.getKeyCode() == KeyEvent.VK_DOWN)
            direction = 4;
        else if (e.getKeyCode() == KeyEvent.VK_LEFT)
            direction = 1;
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            direction = 2;
        else if (e.getKeyCode() == KeyEvent.VK_P)
            direction = 0;
        else if (e.getKeyCode() == KeyEvent.VK_Q)
            System.exit(0);
        else
            direction = -1;

        System.out.println("direction:" + direction + "    <- e.getKeyCode()=" + e.getKeyCode());

        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] args) {
        EX2_PM_3_1D frame = new EX2_PM_3_1D();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
