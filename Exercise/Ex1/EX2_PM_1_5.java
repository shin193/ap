package Exercise.Ex1;

import java.util.Random;
import java.util.Scanner;

public class EX2_PM_1_5 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter grid size: ");
        int size = sc.nextInt();

        char[][] mat = new char[size][size];
        Random rand = new Random();
        int x = size / 2, y = size / 2;

        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                mat[i][j] = (i == 0 || i == size - 1 || j == 0 || j == size - 1) ? '*' : ' ';

        mat[x][y] = 'X';

        while (true) {
            for (char[] row : mat) {
                System.out.println(row);
            }
            System.out.println();

            int direction = rand.nextInt(4);
            System.out.println("Direction: " + (direction == 0 ? "Up" : direction == 1 ? "Right" : direction == 2 ? "Down" : "Left"));

            int newX = x + (direction == 0 ? -1 : direction == 2 ? 1 : 0);
            int newY = y + (direction == 3 ? -1 : direction == 1 ? 1 : 0);

            if (mat[newX][newY] != '*') {
                mat[x][y] = ' ';
                x = newX;
                y = newY;
                mat[x][y] = 'X';
            } else {
                System.out.println("Hitting the game wall!");
            }

            try { Thread.sleep(1000); } catch (InterruptedException e) {}
        }
    }
}
