package mid_project.part_1;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Menu {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();

    public static void showManagerMenu(Library library) {
        while (true) {
            System.out.println("\t\t\t\t\t\t*===================================================================*");
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t  --- Manager Menu ---");
            System.out.println("\t\t\t\t\t\t\t1. Add new book");
            System.out.println("\t\t\t\t\t\t\t2. View overdue books");
            System.out.println("\t\t\t\t\t\t\t3. Add new librarian");
            System.out.println("\t\t\t\t\t\t\t4. View all the librarians");
            System.out.println("\t\t\t\t\t\t\t5. View number of loans per librarian");
            System.out.println("\t\t\t\t\t\t\t6. View top 10 borrowed books");
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t7. log out");
            System.out.println("\t\t\t\t\t\t*===================================================================*");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    addNewBook(library);
                    break;
                case "2":
                    showOverdueBooks(library);
                    break;
                case "3":
                    addNewLibrarian(library);
                    break;
                case "4":
                    viewLibrarians(library);
                    break;
                case "5":
                    System.out.println("This operation is not available for now");
                    break;
                case "6":
                    System.out.println("This operation is not available for now");
                    break;
                case "7":
                    return;
                default:
                    System.out.println("Invalid choice");
                    break;
            }


        }
    }

    public static void showStudentMenu(Library library, Student student) {
        while (true) {
            System.out.println("\t\t\t\t\t\t*===================================================================*");
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t  --- Student Menu ---");
            System.out.println("\t\t\t\t\t\t\t1. Search books");
            System.out.println("\t\t\t\t\t\t\t2. Borrow a book");
            System.out.println("\t\t\t\t\t\t\t3. Return a book");
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t4. Exit");
            System.out.println("\t\t\t\t\t\t*===================================================================*");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    searchBooks(library);
                    break;
                case "2":
                    borrowBook(library, student);
                    break;
                case "3":
                    returnBook(library, student);
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }
    }
    public static void showLibrarianMenu(Librarian librarian, Library library) {
        while (true) {
            System.out.println("\t\t\t  *=============================================================================*");
            System.out.println("\t\t\t\t\t\t\t\t\t\tLibrarian Menu\n");
            System.out.println("\t\t\t\t\t1) Add a new book to the library");
            System.out.println("\t\t\t\t\t2) Approve student loan request");
            System.out.println("\t\t\t\t\t3) Approve book return");
            System.out.println("\t\t\t\t\t4) View all loan history");
            System.out.println("\t\t\t\t\t5) View student's loan history");
            System.out.println("\t\t\t\t\t6) Update personal information");
            System.out.println("\t\t\t\t\t7) Log out");
            System.out.println("\t\t\t  *=============================================================================*");
            System.out.print("\tChoose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addBook(librarian, library);
                    break;
                case "2":
                    System.out.println("this option is not available yet");
                    break;
                case "3":
                    System.out.println("this option is not available yet");
                    break;
                case "4":
                    System.out.println("this option is not available yet");
                    break;
                case "5":
                    System.out.println("this option is not available yet");
                    break;
                case "6":
                    updatePersonalInfo(librarian);
                    break;
                case "7":
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
                    break;
            }
        }
    }

    private static void addBook(Librarian librarian, Library library) {
        System.out.println("Enter book title: ");
        String title = scanner.nextLine();
        System.out.println("Enter book author: ");
        String author = scanner.nextLine();
        System.out.println("Enter publish year: ");
        int publishYear = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter number of pages: ");
        int pages = Integer.parseInt(scanner.nextLine());

        librarian.addBookToLibrary(library, title, author, publishYear, pages);
    }

    private static void updatePersonalInfo(Librarian librarian) {
        System.out.println("Enter new first name: ");
        String firstName = scanner.nextLine();
        System.out.println("Enter new last name: ");
        String lastName = scanner.nextLine();
        System.out.println("Enter new employee ID: ");
        String employeeId = scanner.nextLine();

        librarian.changeInfo(firstName, lastName, employeeId);
        System.out.println("Personal information updated successfully.");
    }


    private static void addNewBook(Library library) {
        System.out.print("Title: ");
        String title = scanner.nextLine();
        System.out.print("Author: ");
        String author = scanner.nextLine();
        System.out.print("Publish Year: ");
        int year = scanner.nextInt();
        System.out.print("Page Count: ");
        int pages = scanner.nextInt();
        scanner.nextLine();

        Book book = new Book (title, author,year, pages);
        library.addBook(book);
        System.out.println("Book added successfully!.");
    }

    private static void searchBooks(Library library) {
        System.out.print("Enter title or part of it: ");
        String query = scanner.nextLine().toLowerCase();
        List<Book> books = library.getBooks();
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(query)) {
                System.out.println(book);
                return;
            }
        }
        System.out.println("book not found!");
    }

    private static void borrowBook(Library library, Student student) {
        System.out.print("Enter exact title of the book: ");
        String title = scanner.nextLine();
        Book foundBook = null;
        for (Book book : library.getBooks()) {
            if (book.getTitle().equals(title)) {
                foundBook = book;
                break;
            }
        }
        for (Loan loan : library.getLoans()) {
            if (loan.getBook().getTitle().equals(title) &&
                    loan.getStudent().equals(student) &&
                    loan.getReturnedDate() == null) {
                System.out.println("You already borrowed this book.");
                return;
            }
        }
        if (foundBook == null) {
            System.out.println("Book not found.");
            return;
        }

        Librarian librarian = getRandomLibrarian(library);
        LocalDate borrowedDate = LocalDate.now();
        LocalDate dueDate = borrowedDate.plusDays(14);

        Loan loan = new Loan(foundBook, student, librarian, borrowedDate, dueDate);
        library.addLoan(loan);
        System.out.println("Book borrowed successfully!. Due date: " + dueDate + ". Librarian: " + librarian.getFullName());
    }

    private static void returnBook(Library library, Student student) {
        List<Loan> loans = library.getLoans();
        boolean found = false;
        for (Loan loan : loans) {
            if (loan.getStudent().equals(student) && !loan.isReturned()) {
                Librarian returnLibrarian = getRandomLibrarian(library);
                loan.returnBook(LocalDate.now(), returnLibrarian);
                System.out.println("Book returned to librarian : "+returnLibrarian.getFullName()+" successfully!.");
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("No borrowed books to return.");
        }
    }

    private static void showOverdueBooks(Library library) {
        System.out.println("Overdue Books:");
        for (Loan loan : library.getLoans()) {
            if (loan.isOverdue()) {
                System.out.println(loan);
            }
        }
    }

    private static Librarian getRandomLibrarian(Library library) {
        List<Librarian> librarians = library.getLibrarians();
        return librarians.get(random.nextInt(librarians.size()));
    }
    private static void addNewLibrarian (Library library) {
        System.out.println("Enter librarian's first name: ");
        String firstName = scanner.nextLine();
        System.out.println("Enter librarian's last name: ");
        String lastName = scanner.nextLine();
        System.out.println("Enter librarian's employee ID: ");
        String employeeId = scanner.nextLine();
        Librarian newLibrarian = new Librarian(firstName, lastName, employeeId);
        library.addLibrarian(newLibrarian);
        System.out.println("Librarian : "+newLibrarian.getFullName()+" successfully added.");
    }
    private static void viewLibrarians(Library library) {
        System.out.println("List of librarians:");
        for (Librarian librarian : library.getLibrarians()) {
            System.out.println("- " + librarian.getFullName() + " (ID: " + librarian.getEmployeeId() + ")");
        }
    }

    private static Student findStudentById(Library library, String studentId) {
        for (Student student : library.getStudents()) {
            if (student.getStudentId().equals(studentId)) {
                return student;
            }
        }
        return null;
    }
}
