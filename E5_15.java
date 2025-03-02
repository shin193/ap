import java.util.Scanner;

public class E5_15 {
    public static void main(String[] args) {
        String value;
        char flag1,flag2;
        float num;
        System.out.print("enter the letter score : ");
        Scanner input = new Scanner(System.in);
        value = input.nextLine();
        flag1 = value.charAt(0);
        flag2 = (value.length()>1) ? value.charAt(1) :' ';

        switch (flag1){
            case 'a':
            case 'A': num =4.0f;break;
            case 'b':
            case 'B': num =3.0f;break;
            case 'c':
            case 'C': num =2.0f;break;
            case 'd':
            case 'D': num =1.0f;break;
            case 'f':
            case 'F': num =0.0f;break;
            default:throw new IllegalArgumentException("Invalid letter grade.");
        }
    if (flag2 =='+' || flag2 =='-'){
        if (num == 4.0f){
            if (flag2 =='+') {
                System.out.println("the score is invalid");
            }
            else {
                System.out.println("your number score is 3.7");
            }
        }
        else if (num!=0.0)
            if (flag2=='+') {
                num = num + 0.3f;
                System.out.println("your number score is " + num);
            }
            else {
                num = num - 0.3f;
                System.out.println("your number score is " + num );
            }
    }
    else if (flag2==' ')
        System.out.println("your number score is " + num);
    else
        System.out.println("invalid action");
    }


}
