package Exercise.Ex1;

import java.util.Scanner;

public class E6_8 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter a sentence : ");
        String sen = sc.nextLine();
        for (int i = 0; i < sen.length(); i++) {
            System.out.println(sen.charAt(i));

        }
    }
}
