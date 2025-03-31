package Exercise.Ex1;

import java.util.Scanner;

public class EX2_PM_1_4 {
    public static void main(String[] args) {
        System.out.println("Enter an action: ");
        Scanner sc = new Scanner(System.in);
        while (true){
            String action = sc.nextLine();
        switch (action) {
            case "w":{
                System.out.println("up");
                break;}
            case "s":{
                System.out.println("down");
                break;}
            case "a":{
                System.out.println("left");
                break;
            }
            case "d":{
                System.out.println("right");
                break;
            }
            case "q":{
                System.out.println("quit");
                return;
            }
            default:{
                System.out.println("Invalid action ");
                break;
            }
        }
    }
    }
}
