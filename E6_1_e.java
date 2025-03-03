import java.util.Scanner;

public class E6_1_e {
    public static void main(String[] args) {
        System.out.print("enter a number : ");
        int num,s=0,z;
        Scanner input = new Scanner(System.in);
        num = input.nextInt();
        while ( num!=0 ){
            z=num%10;
            if(z%2==1)
                s=s+z;
            num=num/10;

        }
        System.out.println("sum of the odd digits is : "+s);
    }
}
