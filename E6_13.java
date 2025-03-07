import java.util.Scanner;

public class E6_13 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("enter a number : ");

        int num = sc.nextInt();

        if (num ==0) {
            System.out.println(0);
        }
        else {
            while (num!=0) {
                System.out.println(num%2);
                num = num/2;
            }
        }
    }
}
