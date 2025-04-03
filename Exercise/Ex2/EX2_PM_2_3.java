package Exercise.Ex2;

import java.io.*;
import java.util.Scanner;
import java.util.Random;

public class EX2_PM_2_3 {
    private static final String SAVE_FILE = "game_save.txt";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random rand = new Random();
        int size, score = 0, totalDots;
        int x, y;
        char[][] mat;

        File saveFile = new File(SAVE_FILE);
        if (saveFile.exists()) {
            System.out.println("Loading saved game...");
            try (BufferedReader reader = new BufferedReader(new FileReader(SAVE_FILE))) {
                size = Integer.parseInt(reader.readLine());
                score = Integer.parseInt(reader.readLine());
                totalDots = Integer.parseInt(reader.readLine());
                x = Integer.parseInt(reader.readLine());
                y = Integer.parseInt(reader.readLine());
                mat = new char[size][size];
                for (int i = 0; i < size; i++) {
                    mat[i] = reader.readLine().toCharArray();
                }
            } catch (IOException e) {
                System.out.println("Error loading save file. Starting new game.");
                saveFile.delete();
                return;
            }
        } else {
            System.out.print("Enter the board size : ");
            size = sc.nextInt();
            System.out.print("Enter the number of dots: ");
            totalDots = sc.nextInt();
            if (totalDots >= (size - 2) * (size - 2)) {
                System.out.println("Error: Too many dots for the given board size.");
                return;
            }

            mat = new char[size][size];
            x = size / 2;
            y = size / 2;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    mat[i][j] = (i == 0 || i == size - 1 || j == 0 || j == size - 1) ? '*' : ' ';
                }
            }
            mat[x][y] = 'X';
            for (int i = 0; i < totalDots; i++) {
                int dotX, dotY;
                do {
                    dotX = rand.nextInt(size - 2) + 1;
                    dotY = rand.nextInt(size - 2) + 1;
                } while (mat[dotX][dotY] != ' ');
                mat[dotX][dotY] = '.';
            }
        }

        long start = System.currentTimeMillis();
        while (true) {
            for (char[] row : mat) {
                System.out.println(row);
            }
            System.out.println("Score: " + score);
            System.out.println();

            System.out.println("Enter move (w/a/s/d) or 'q' to quit and save: ");
            char input = sc.next().charAt(0);
            if (input == 'q') {
                saveGame(size, score, totalDots, x, y, mat);
                System.out.println("Game saved.");
                return;
            }

            int newX = x + (input == 'w' ? -1 : input == 's' ? 1 : 0) + (input == 'W' ? -1 : input == 'S' ? 1 : 0);
            int newY = y + (input == 'a' ? -1 : input == 'd' ? 1 : 0) + (input == 'A' ? -1 : input == 'D' ? 1 : 0);

            if (mat[newX][newY] == '*') {
                System.out.println("Hitting the game wall!");
                break;
            }
            if (mat[newX][newY] == '.') {
                score++;
                totalDots--;
            }

            mat[x][y] = ' ';
            x = newX;
            y = newY;
            mat[x][y] = 'X';

            if (totalDots == 0) {
                System.out.println("All dots collected!");
                break;
            }
        }

        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("Game Over!");
        System.out.println("Final Score: " + score);
        System.out.println("Time Elapsed: " + timeElapsed + " ms");
        saveFile.delete();
    }

    private static void saveGame(int size, int score, int totalDots, int x, int y, char[][] mat) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SAVE_FILE))) {
            writer.write(size + "\n");
            writer.write(score + "\n");
            writer.write(totalDots + "\n");
            writer.write(x + "\n");
            writer.write(y + "\n");
            for (char[] row : mat) {
                writer.write(row);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving game.");
        }
    }
}
