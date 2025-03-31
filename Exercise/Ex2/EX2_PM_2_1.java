package Exercise.Ex2;

import java.util.Scanner;

public class EX2_PM_2_1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter grid size: ");
        int size = sc.nextInt();

        char[][] mat = new char[size][size];
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

            char input = sc.next().charAt(0);

            int newX = x + (input == 'w' ? -1 : input == 's' ? 1 : 0);
            int newY = y + (input == 'a' ? -1 : input == 'd' ? 1 : 0);

            if (mat[newX][newY] != '*') {
                mat[x][y] = ' ';
                x = newX;
                y = newY;
                mat[x][y] = 'X';
            } else {
                System.out.println("Hitting the game wall!");
                return;
            }

            try { Thread.sleep(100); } catch (InterruptedException e) {}
        }
    }
}
