package Exercise.Ex2;

import java.util.Scanner;
import java.util.Random;

public class EX2_PM_2_2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random rand = new Random();

        System.out.print("Enter the board size : ");
        int size = sc.nextInt();

        System.out.print("Enter the number of dots: ");
        int totalDots = sc.nextInt();

        if (totalDots >= (size - 2) * (size - 2)) {
            System.out.println("Error: Too many dots.");
            return;
        }

        int score = 0;
        long start = System.currentTimeMillis();

        char[][] mat = new char[size][size];
        int x = size / 2, y = size / 2;

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

        while (true) {
            for (char[] row : mat) {
                System.out.println(row);
            }
            System.out.println("Score: " + score);
            System.out.println();

            char input = sc.next().charAt(0);
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
    }
}