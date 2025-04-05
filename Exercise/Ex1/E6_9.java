package Exercise.Ex1;

import java.util.Scanner;

public class E6_9 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("enter a sentence : ");
        String sen = sc.nextLine();
        for (int i = sen.length()-1 ; i >= 0; i--) {
            System.out.print(sen.charAt(i));

        }
    }
}
