package Exercise.Ex1;

import java.util.*;

public class E6_5 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter floating-point values (separated by spaces) : ");
        String[] input = scanner.nextLine().split(" ");

        double sum = 0;
        double smallest = Double.MAX_VALUE;
        double largest = Double.MIN_VALUE;
        int count = 0;

        for (String num : input) {
            boolean isNumber = true;
            for (char ch : num.toCharArray()) {
                if (!Character.isDigit(ch) && ch != '.' && ch != '-') {
                    isNumber = false;
                    break;
                }
            }

            if (isNumber && !num.isEmpty()) {
                double value = Double.parseDouble(num);
                sum += value;
                if (value < smallest) smallest = value;
                if (value > largest) largest = value;
                count++;
            }
        }

        double average = (count == 0) ? 0 : sum / count;
        double range = (count == 0) ? 0 : largest - smallest;

        System.out.println("average is: " + average);
        System.out.println("smallest number is : " + (count == 0 ? " " : smallest));
        System.out.println("largest number is : " + (count == 0 ? " " : largest));
        System.out.println("range is : " + range);

        scanner.close();
    }
}
