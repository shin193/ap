package Exercise.Ex2;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class EX2_PM_2_4 {
    public static void main(String[] args) {
        int k = 9;
        int c = 15;

        PacmanEngine pacmanEngine = new PacmanEngine(k, c);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            pacmanEngine.printMatrix();
            pacmanEngine.printScore();
            pacmanEngine.printRemainTime();

            System.out.print("Move (w=up, s=down, a=left, d=right): ");
            char move = scanner.next().charAt(0);

            int direction = -1;
            switch (move) {
                case 'w': direction = 0; break;
                case 's': direction = 1; break;
                case 'a': direction = 2; break;
                case 'd': direction = 3; break;
                case 'W': direction = 0; break;
                case 'S': direction = 1; break;
                case 'A': direction = 2; break;
                case 'D': direction = 3; break;
                default:
                    System.out.println("Invalid input!");
                    continue;
            }

            pacmanEngine.move(direction);
            pacmanEngine.save();
        }
    }

    public static class PacmanEngine {
        private int size;
        private int totalDots;
        private int score;
        private char[][] mat;
        private int x, y;
        private long startTime;
        private Random rand;

        public PacmanEngine(int size, int totalDots) {
            if (totalDots >= (size - 2) * (size - 2)) {
                throw new IllegalArgumentException("Too many dots for the board size.");
            }

            this.size = size;
            this.totalDots = totalDots;
            this.score = 0;
            this.rand = new Random();
            this.mat = new char[size][size];
            this.x = size / 2;
            this.y = size / 2;
            this.startTime = System.currentTimeMillis();

            initializeBoard();
            placeDots();
            mat[x][y] = 'X';
        }

        private void initializeBoard() {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (i == 0 || j == 0 || i == size - 1 || j == size - 1) {
                        mat[i][j] = '*';
                    } else {
                        mat[i][j] = ' ';
                    }
                }
            }
        }

        private void placeDots() {
            int placed = 0;
            while (placed < totalDots) {
                int dx = rand.nextInt(size - 2) + 1;
                int dy = rand.nextInt(size - 2) + 1;
                if (mat[dx][dy] == ' ') {
                    mat[dx][dy] = '.';
                    placed++;
                }
            }
        }

        public void printMatrix() {
            for (char[] row : mat) {
                System.out.println(new String(row));
            }
            System.out.println();
        }

        public void printScore() {
            System.out.println("Score: " + score);
        }

        public void printRemainTime() {
            long elapsed = System.currentTimeMillis() - startTime;
            System.out.println("Time Elapsed: " + elapsed + " ms");
        }

        public void move(int direction) {
            int newX = x;
            int newY = y;

            switch (direction) {
                case 0: newX--; break; // up
                case 1: newX++; break; // down
                case 2: newY--; break; // left
                case 3: newY++; break; // right
                default: return;
            }

            if (newX < 0 || newX >= size || newY < 0 || newY >= size || mat[newX][newY] == '*') {
                System.out.println("Invalid move!");
                return;
            }

            if (mat[newX][newY] == '.') {
                score++;
                totalDots--;
                if (totalDots == 0) {
                    System.out.println("All dots collected! .");
                    printScore();
                    printRemainTime();
                    save();
                    System.exit(0);
                }
            }

            mat[x][y] = ' ';
            x = newX;
            y = newY;
            mat[x][y] = 'X';
        }

        public void save() {
            try (FileWriter writer = new FileWriter("save.txt", false)) {
                long elapsed = System.currentTimeMillis() - startTime;

                writer.write("Current Game State:\n");
                writer.write("Board Size: " + size + "\n");
                writer.write("Remaining Dots: " + totalDots + "\n");
                writer.write("Score: " + score + "\n");
                writer.write("Time Elapsed: " + elapsed + " ms\n");
                writer.write("Player Position: (" + x + ", " + y + ")\n\n");

                for (char[] row : mat) {
                    writer.write(new String(row));
                    writer.write("\n");
                }

                System.out.println("Game saved to save.txt ");
            } catch (IOException e) {
                System.out.println("Error saving game: " + e.getMessage());
            }
        }
    }
}
