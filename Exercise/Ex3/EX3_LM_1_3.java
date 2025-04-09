import java.util.Scanner;

public static void EX3_LM_1_3 (Student[] students) {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Enter student name to search: ");
    String nameToSearch = scanner.nextLine();

    Student result = null;
    for (Student student : students) {
        if (student.name.equalsIgnoreCase(nameToSearch)) {
            result = student;
            break;
        }
    }

    if (result != null) {
        System.out.println("Student found: " + result.name + " - ID: " + result.id);
    } else {
        System.out.println("Student not found.");
    }

    scanner.close();
}
