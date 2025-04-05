package Exercise.Ex1;

import java.util.Arrays;
import java.util.Scanner;

public class E5_18 {
    public static void main(String[] args) {
        System.out.println("enter tree string");
        Scanner input = new Scanner(System.in);
        String[] s = new String[3];
        for (int i = 0; i < s.length; i++) {
            s[i] = input.nextLine();
        }
        Arrays.sort(s);

        for (int i = 0; i < s.length; i++) {
            System.out.println(s[i]);
        }
    }
}
