package mid_project.part_1;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static mid_project.part_1.LibraryFileHandler.loadLibraryFromFile;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        Library library = loadLibraryFromFile();
        String choice;
        while (true) {
            System.out.println("\t\t\t  *=============================================================================*");
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\tWelcome \n");
            System.out.println("\t\t\t\t\t\t\t1)Login as a student \t\t\t2)Register as student\n ");
            System.out.println("\t\t\t\t\t\t\t3)Login as a manager \t\t\t4)Login as librarian. ");
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t5)Exit");
            System.out.println("\t\t\t  *=============================================================================*");
            System.out.print("\tChoose an option: ");
            choice= scanner.nextLine();
            switch (choice) {
                case "1":
                    Student studentL = login(library);
                    if (studentL != null) {
                        Menu.showStudentMenu(library, studentL);
                    }
                    break;
                case "2":
                    Student studentR=register(library);
                    Menu.showStudentMenu(library, studentR);
                    break;
                case "3":
                    Manager manager =loginManager(library);
                    if (manager != null) {
                        Menu.showManagerMenu(library);
                    }
                    break;
                case "4":
                   Librarian librarian=loginLibrarian(library);
                   if (librarian != null) {
                       Menu.showLibrarianMenu(librarian, library);
                   }
                   break;
                case "5":
                    LibraryFileHandler.saveLibraryToFile(library);
                    System.out.println("Library saved!.Good bye!");
                    return;
                default:
                    System.out.println("Invalid choice");
                    break;
            }

        }

    }
    private static Student login (Library library) {
        System.out.println("enter your password: ");
        String password= scanner.nextLine();
        for (Student s : library.getStudents()) {
            if (s.getStudentId().equals(password)) {
                System.out.println("Welcome back "+s.getFullName());
                return s;
            }
        }
        System.out.println("student not found");
        return null;
    }
    private static Student register(Library library) {
        System.out.println("enter your name: ");
        String firstName = scanner.nextLine();
        System.out.println("enter your last name: ");
        String lastName = scanner.nextLine();
        System.out.println("enter your password: ");
        String password= scanner.nextLine();
        System.out.println("enter your major: ");
        String major = scanner.nextLine();
        Student newStudent=new Student(firstName,lastName,password,major, LocalDate.now());
        library.addStudent(newStudent);
        System.out.println("New member successfully added . welcome "+newStudent.getFullName());
        return newStudent;
    }
    private static Manager loginManager(Library library) {
        System.out.println("enter your name:");
        String firstName = scanner.nextLine();
        System.out.println("enter your last name: ");
        String lastName = scanner.nextLine();
        if (library.getManager().getFirstName().equals(firstName) && library.getManager().getLastName().equals(lastName)) {
            System.out.println("enter your password: ");
            String password= scanner.nextLine();
            if (library.getManager().getPassword().equals(password)) {
                System.out.println("Welcome back "+library.getManager().getFullName()+" !");
                return library.getManager();
            }
            System.out.println("wrong password , please try again");
        }
        else{
            System.out.println("first name or last name not match");
        }
        return null;
    }
    private static Librarian loginLibrarian(Library library) {
        System.out.println("Enter your employee id: ");
        String employeeId = scanner.nextLine();
        for (Librarian librarian : library.getLibrarians()) {
            if (librarian.getEmployeeId().equals(employeeId)) {
                System.out.println("Welcome back "+librarian.getFullName()+" !");
                return librarian;
            }
        }
        System.out.println("wrong employee id , please try again");
        return null;
    }
}