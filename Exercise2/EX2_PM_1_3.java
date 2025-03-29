package Exercise2;

import java.util.Scanner;
import java.util.Random;

public class EX2_PM_1_3 {
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
        int s=k*k;
        int c;
        do {
            System.out.print("Enter the c : (max of the c is :" + s + ") ");
            c = sc.nextInt();
            if (c > k * k) {
                System.out.println("Error,try again!");
            }
        }
        while (c > k * k);
        Random rand = new Random();
        for (int i = 0; i < c;) {
            int x = rand.nextInt(k) + 1;
            int j = rand.nextInt(k) + 1;
            if (mat[x][j] == ' ') {
                mat[x][j] = '.';
                i++;
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
