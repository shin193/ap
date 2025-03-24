package Exercise2;

import java.util.Scanner;

public class EX2_PM_1_2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter a number : ");
        int k = sc.nextInt();

        int size = k + 2;
        char[][] mat = new char[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                mat[i][j] = (i == 0 || i == size - 1 || j == 0 || j == size - 1) ? '*' : ' ';
            }
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(mat[i][j]);
            }
            System.out.println();

        }
    }
}
