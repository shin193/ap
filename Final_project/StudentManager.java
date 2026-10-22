package Final_project;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StudentManager {
    private List<Student> students=new ArrayList<>();
    private Scanner sc;

    public StudentManager() {
        this(new Scanner(System.in));
    }

    public StudentManager(Scanner sc) {
        this.students=Database.loadStudents();
        this.sc=sc;
    }

    public void registerStudent(String name, String studentId, String username, String password) {
        if (studentId == null || studentId.trim().isEmpty() || isStudentIdTaken(studentId)) {
            System.out.println("Student ID must be non-empty and unique.");
            return;
        }
        if (isUsernameTaken(username)) {
            System.out.println("This username already exists. Please choose a different username.");
            return;
        }

        Student newStudent = new Student(name, studentId, username, Passwords.hash(password));
        students.add(newStudent);
        Database.saveStudents(students);
        System.out.println("Student registration completed successfully.");
    }

    public Student authenticateStudent(String username, String password) {
        return students.stream()
                .filter(s -> s.getUsername().equals(username) && Passwords.matches(password, s.getPassword()))
                .findFirst()
                .orElse(null);
    }

    public void displayStudentsAccountStatus() {
        if (students.isEmpty()) {
            System.out.println("No students have registered yet.");
            return;
        }

        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            System.out.println("-"+(i+1)+">>"+"Name: "+student.getName()+"| ID : "+student.getStudentId()+"| Account Status : "+ (student.isActive() ? "Enable " : "Disable ") +"\n---------------------------------------");
        }
        System.out.println("Enter the number of student to change His Account Status :");

        try {
            int choice = Integer.parseInt(sc.nextLine());
            if (choice > 0 && choice <= students.size()) {
                students.get(choice - 1).setActive(!students.get(choice - 1).isActive());
                Database.saveStudents(students);
                System.out.println("Student account status updated.");
            }
            else {
                System.out.println("Please enter a valid choice");
            }
        }
        catch (NumberFormatException e) {
            System.out.println("Please enter a valid choice");
        }
    }

    public List<Student> getAllStudents() {
        return students;
    }

    public void editStudentInformation(Student currentUser) {
        System.out.println("What would you like to edit?");
        System.out.println("1. Name");
        System.out.println("2. Username");
        System.out.println("3. Password");
        System.out.println("4. Student ID");

        String choice = sc.nextLine();

        if (choice.equals("1")) {
            System.out.println("Enter your new name: ");
            String newName = sc.nextLine();
            currentUser.setName(newName);
            Database.saveStudents(students);
            System.out.println("Your name has been edited.");
        }
        else if (choice.equals("2")) {
            System.out.println("Enter your new username: ");
            String newUsername = sc.nextLine();
            if (isUsernameTaken(newUsername)){
                System.out.println("That username is already taken. try again.");
            }
            else {
                currentUser.setUsername(newUsername);
                Database.saveStudents(students);
                System.out.println("Your username has been edited.");
            }
        }
        else if (choice.equals("3")) {
            System.out.println("Enter your new password: ");
            String newPassword = sc.nextLine();
            currentUser.setPassword(Passwords.hash(newPassword));
            Database.saveStudents(students);
            System.out.println("Your password has been edited.");
        }
        else if (choice.equals("4")) {
            System.out.println("Enter your new student ID: ");
            String newStudentID = sc.nextLine();
            if (newStudentID.equals(currentUser.getStudentId())) {
                return;
            }
            if (newStudentID.trim().isEmpty() || isStudentIdTaken(newStudentID)) {
                System.out.println("Student ID must be non-empty and unique.");
                return;
            }
            if (Database.hasStudentLoans(currentUser.getStudentId())) {
                System.out.println("Student ID cannot change while you have pending requests or active loans.");
                return;
            }
            currentUser.setStudentId(newStudentID);
            Database.saveStudents(students);
            System.out.println("Your student ID has been edited.");
        }
        else {
            System.out.println("Invalid action!");
        }
    }


    public boolean isUsernameTaken(String username) {
        return students.stream().anyMatch(s -> s.getUsername().equals(username));
    }

    private boolean isStudentIdTaken(String studentId) {
        return students.stream().anyMatch(s -> s.getStudentId().equals(studentId));
    }

    public int getStudentCount() {
        return students.size();
    }
}
