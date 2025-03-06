import java.util.Scanner;

class Grade {
    public static double getNumericGrade(String letterGrade) {
        if (letterGrade == null || letterGrade.isEmpty()) {
            throw new IllegalArgumentException("Invalid grade input.");
        }

        // استخراج بخش اصلی نمره (A, B, C, D, F) و علامت (+ یا -)
        char baseGrade = letterGrade.charAt(0);
        char modifier = (letterGrade.length() > 1) ? letterGrade.charAt(1) : ' ';

        // مقداردهی اولیه برای نمرات اصلی
        double numericValue;
        switch (baseGrade) {
            case 'A': numericValue = 4.0; break;
            case 'B': numericValue = 3.0; break;
            case 'C': numericValue = 2.0; break;
            case 'D': numericValue = 1.0; break;
            case 'F': numericValue = 0.0; break;
            default: throw new IllegalArgumentException("Invalid letter grade.");
        }

        // اعمال تغییرات + و - (به جز F که تغییر نمی‌کند)
        if (baseGrade != 'F') {
            if (modifier == '+') {
                numericValue = (baseGrade == 'A') ? 4.0 : numericValue + 0.3;
            } else if (modifier == '-') {
                numericValue -= 0.3;
            }
        }

        return numericValue;
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a letter grade: ");
        String letterGrade = scanner.next().toUpperCase(); // تبدیل به حروف بزرگ برای سازگاری
        scanner.close();

        try {
            double numericValue = Grade.getNumericGrade(letterGrade);
            System.out.println("The numeric value is " + numericValue);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
