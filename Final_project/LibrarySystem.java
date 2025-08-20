package Final_project;

import java.util.List;
import java.util.Scanner;

public class LibrarySystem {
    private StudentManager studentManager;
    private BookManager bookManager;
    private MenuHandler menuHandler;

    public LibrarySystem() {
        this.studentManager = new StudentManager();
        this.bookManager = new BookManager();
        this.menuHandler = new MenuHandler(this);
    }

    public int getStudentCount() {
        return this.studentManager.getStudentCount();
    }

    public void registerStudent(String name, String studentId, String username, String password) {
        studentManager.registerStudent(name, studentId, username, password);
    }

    public Student authenticateStudent(String username, String password) {
        return studentManager.authenticateStudent(username, password);
    }
    public void addBook(String title, String id, String author) {
        bookManager.addBook(title, id, author);
    }

    public void displayAvailableBooks() {
        List<Book> availableBooks = bookManager.getAvailableBooks();
        if (availableBooks.isEmpty()) {
            System.out.println("No books available.");
            return;
        }
        System.out.println("\n--- Available Books ---");
        for (Book book : availableBooks) {
            System.out.println(book);
        }
    }

    public void borrowBook(Student student) {
        System.out.print("Enter book ID to borrow: ");
        String bookId = menuHandler.getScanner().nextLine();
        bookManager.borrowBook(bookId, student);
    }

    public void returnBook(Student student) {
        System.out.print("Enter book ID to return: ");
        String bookId = menuHandler.getScanner().nextLine();
        bookManager.returnBook(bookId);
    }

    public void start() {
        menuHandler.displayMainMenu();
    }


    public static void main(String[] args) {
        LibrarySystem system = new LibrarySystem();
        system.start();
    }

    public void editStudentInformation(Student currentUser) {
        System.out.println("What would you like to edit?");
        System.out.println("1. Name");
        System.out.println("2. Username");
        System.out.println("3. Password");
        System.out.println("4. Student ID");

        String choice = menuHandler.getScanner().nextLine();

        if (choice.equals("1")) {
            System.out.println("Enter your new name: ");
            String newName = menuHandler.getScanner().nextLine();
            currentUser.setName(newName);
            System.out.println("Your name has been edited.");
        }
        else if (choice.equals("2")) {
            System.out.println("Enter your new username: ");
            String newUsername = menuHandler.getScanner().nextLine();
            if (studentManager.isUsernameTaken(newUsername)){
                System.out.println("That username is already taken. try again.");
            }
            else {
                currentUser.setUsername(newUsername);
                System.out.println("Your username has been edited.");
            }
        }
        else if (choice.equals("3")) {
            System.out.println("Enter your new password: ");
            String newPassword = menuHandler.getScanner().nextLine();
            currentUser.setPassword(newPassword);
            System.out.println("Your password has been edited.");
        }
        else if (choice.equals("4")) {
            System.out.println("Enter your new student ID: ");
            String newStudentID = menuHandler.getScanner().nextLine();
            currentUser.setStudentId(newStudentID);
            System.out.println("Your student ID has been edited.");
        }
        else {
            System.out.println("Invalid action!");
        }
    }
}