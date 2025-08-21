package Final_project;

import java.util.List;

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

}