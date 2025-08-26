package Final_project;

import java.util.List;

public class LibrarySystem {
    private StudentManager studentManager;
    private BookManager bookManager;
    private LibrarianManager librarianManager;
    private MenuHandler menuHandler;

    public LibrarySystem() {
        this.studentManager = new StudentManager();
        this.bookManager = new BookManager();
        this.librarianManager = new LibrarianManager();
        this.menuHandler = new MenuHandler(this);
    }

    public int getStudentCount() {
        return this.studentManager.getStudentCount();
    }
    public int getBookCount() {return this.bookManager.getNumberOfBooks();}

    public void registerStudent(String name, String studentId, String username, String password) {
        studentManager.registerStudent(name, studentId, username, password);
    }

    public Student authenticateStudent(String username, String password) {
        return studentManager.authenticateStudent(username, password);
    }
    public void editStudentInfo(Student currentUser){
        studentManager.editStudentInformation(currentUser);
    }
    public void addBook(String title, String id, String author ,int year) {
        bookManager.addBook(title, id, author , year);
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

    public void searchBooks() {
        System.out.println("\n--- Search Books ---");
        System.out.println("1 >> search by title");
        System.out.println("2 >> search by author");
        System.out.println("3 >> search by publish year");
        System.out.print("Choose an option: ");

        String choice = menuHandler.getScanner().nextLine();

        String title = "";
        String author = "";
        Integer publicationYear = null;

        switch (choice) {
            case "1":
                System.out.print("Enter the title: ");
                title = menuHandler.getScanner().nextLine();
                if (title.isEmpty()) {
                    System.out.println("Title cannot be empty!");
                    return;
                }
                break;

            case "2":
                System.out.print("Enter the author: ");
                author = menuHandler.getScanner().nextLine();
                if (author.isEmpty()) {
                    System.out.println("Author cannot be empty!");
                    return;
                }
                break;

            case "3":
                System.out.print("Enter publication year: ");
                String yearInput = menuHandler.getScanner().nextLine();
                if (yearInput.isEmpty()) {
                    System.out.println("Publication year cannot be empty!");
                    return;
                }
                try {
                    publicationYear = Integer.parseInt(yearInput);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid year format! Please enter a valid number.");
                    return;
                }
                break;

            default:
                System.out.println("Invalid option! Please choose 1, 2, or 3.");
                return;
        }
        List<Book> results = bookManager.searchBooks(
                title.isEmpty() ? null : title,
                author.isEmpty() ? null : author,
                publicationYear
        );
        if (results.isEmpty()) {
            System.out.println("No books found matching your criteria.");
        } else {
            System.out.println("\n--- Search Results (" + results.size() + " books found) ---");
            for (Book book : results) {
                System.out.println(book);
            }
        }
    }

    public void requestBorrowBook(Student student) {
        System.out.print("Enter book ID to request: ");
        String bookId = menuHandler.getScanner().nextLine();

        bookManager.requestBorrow(bookId, student);
    }

    public void returnBook(Student student) {
        System.out.print("Enter book ID to return: ");
        String bookId = menuHandler.getScanner().nextLine();
        bookManager.returnBook(bookId);
    }

    public Librarian authenticateLibrarian(String username, String password) {
        return librarianManager.authenticateLibrarian(username, password);
    }

    public void addLibrarian(String username, String password, String name) {
        librarianManager.addLibrarian(username, password, name);
    }

    public void start() {
        menuHandler.displayMainMenu();
    }


    public static void main(String[] args) {
        LibrarySystem system = new LibrarySystem();
        system.start();
    }

}