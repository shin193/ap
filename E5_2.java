import java.util.Scanner;

public class E5_2 {
    public static void main(String[] args) {
        double num;
        System.out.print("enter a floating point number: ");
        Scanner print = new Scanner(System.in);
        num = print.nextDouble();
        if(num == 0){
            System.out.println("zero");
        }
        else if (num > 0 ){
            if (num < 1 )
                System.out.println("small positive number");
            else if (num >=1000000)
                System.out.println("large positive number ");
            else {
                System.out.println("positive number");
            }

        }
        else {
            if (Math.abs(num) >=1000000)
                System.out.println("large negative number ");
            else if (Math.abs(num) < 1)
                System.out.println("small negative number");
            else
                System.out.println("negetive number");
        }
    }
}

