package mid_project.EX7;

import java.util.List;
import java.util.Scanner;

public class Librarian {
    private String firstName;
    private String lastName;
    private String employeeId;
    private static Scanner scanner = new Scanner(System.in);
    public Librarian(String firstName, String lastName, String employeeId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.employeeId = employeeId;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void changeInfo(String firstName, String lastName, String employeeId) {
        this.firstName=firstName;
        this.lastName=lastName;
        this.employeeId=employeeId;
    }
    public void addBookToLibrary (Library library,String title,String author,int publishDate,int bookPage) {
        Book newBook = new Book(title, author, publishDate, bookPage);
        library.addBook(newBook);
        System.out.println("the book :"+newBook+" is added to the library");
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String toString() {
        return getFullName() + " (ID: " + employeeId + ")";
    }

    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void updatePersonalInfo() {
        System.out.println("Enter new first name: ");
        String firstName = scanner.nextLine();
        System.out.println("Enter new last name: ");
        String lastName = scanner.nextLine();
        System.out.println("Enter new employee ID: ");
        String employeeId = scanner.nextLine();

        changeInfo(firstName, lastName, employeeId);
        System.out.println("Personal information updated successfully.");
    }
    public void addBook(Library library) {
        System.out.println("Enter book title: ");
        String title = scanner.nextLine();
        System.out.println("Enter book author: ");
        String author = scanner.nextLine();
        System.out.println("Enter publish year: ");
        int publishYear = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter number of pages: ");
        int pages = Integer.parseInt(scanner.nextLine());

        addBookToLibrary(library, title, author, publishYear, pages);
    }

}
