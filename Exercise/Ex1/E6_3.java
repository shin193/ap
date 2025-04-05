package Exercise.Ex1;

import java.util.*;

public class E6_3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a string: ");
        String input = scanner.nextLine();

        // a. Print only uppercase letters
        StringBuilder uppercaseLetters = new StringBuilder();
        for (char ch : input.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                uppercaseLetters.append(ch);
            }
        }
        System.out.println("Uppercase letters: " + uppercaseLetters);

        // b. Print every second letter
        StringBuilder secondLetters = new StringBuilder();
        for (int i = 1; i < input.length(); i += 2) {
            secondLetters.append(input.charAt(i));
        }
        System.out.println("Every second letter: " + secondLetters);

        // c. Replace vowels with underscores
        String replacedVowels = input.replaceAll("[AEIOUaeiou]", "_");
        System.out.println("String with vowels replaced: " + replacedVowels);

        // d. Count the number of vowels
        int vowelCount = 0;
        for (char ch : input.toCharArray()) {
            if ("AEIOUaeiou".indexOf(ch) != -1) {
                vowelCount++;
            }
        }
        System.out.println("Number of vowels : " + vowelCount);

        // e. Find positions of all vowels
        List<Integer> vowelPositions = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            if ("AEIOUaeiou".indexOf(input.charAt(i)) != -1) {
                vowelPositions.add(i);
            }
        }
        System.out.println("Positions of vowels: " + vowelPositions);

        scanner.close();
    }
}
