package Final_project;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LibrarySystem {
    private StudentManager studentManager;
    private BookManager bookManager;
    private LibrarianManager librarianManager;
    private MenuHandler menuHandler;
    private ManagerManager managerManager;

    public LibrarySystem() {
        this.studentManager = new StudentManager();
        this.bookManager = new BookManager();
        this.librarianManager = new LibrarianManager();
        this.menuHandler = new MenuHandler(this);
        this.managerManager = new ManagerManager();
    }

    public Manager authenticateManager(String username, String password) {
        return managerManager.authenticateManager(username, password);
    }

    public void editManagerPassword(Manager currentManager) {
        managerManager.editManagerPassword(currentManager);
    }

    public Manager getManager() {
        return managerManager.getManager();
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
    public void studentAccountActivity() {
        System.out.println("******Student Account Status******");
        studentManager.displayStudentsAccountStatus();
    }
    public void editStudentInfo(Student currentUser){
        studentManager.editStudentInformation(currentUser);
    }
    public void addBook() {
        System.out.println("-Enter The Title: ");
        String title = menuHandler.getScanner().nextLine();
        System.out.println("-Enter The Author: ");
        String author = menuHandler.getScanner().nextLine();
        System.out.println("-Enter The Book ID: ");
        String id = menuHandler.getScanner().nextLine();
        System.out.println("-Enter The Published Date: ");
        int year = menuHandler.getScanner().nextInt();
        bookManager.addBook(title, id, author , year);
    }

    public void getLoanCounts() {
        System.out.println("Total Loans are : "+bookManager.getBorrowedBooksCount());
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

    public void displayAllBooks() {
        System.out.println("\n****** All Books ******");
        for (Book book : bookManager.getAllBooks()){
            System.out.println(book);
        }
    }

    public void searchBooksByGuest() {
        System.out.println(">>Enter The Book Name : ");
        String bookName = menuHandler.getScanner().nextLine();
        bookManager.searchBooksByGuest (bookName);
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
    public void editBookInfo() {
        System.out.println("Enter The Book ID to edit: ");
        String bookId = menuHandler.getScanner().nextLine();
        bookManager.editBookInfo(bookId);
    }

    public void requestBorrowBook(Student student) {
        System.out.println("\n******List Of The Books******");
        List<Book> books = bookManager.getAllBooks();
        for (int i = 0; i <books.size(); i++) {
            if (!books.get(i).isRequested() && books.get(i).isAvailable()) {
                Book book = books.get(i);
                System.out.println("-"+(i+1)+">>"+book);
            }
        }
        System.out.println("Enter the number of books to request: ");
        try {
            int choice = Integer.parseInt(menuHandler.getScanner().nextLine());
            if (choice >0 && choice <= books.size()) {
                Book book = books.get(choice - 1);
                bookManager.requestBorrow(book.getId() , student);
            }
            else {
                System.out.println("invalid choice. Please try again.");
            }
        }catch (NumberFormatException e) {
            System.out.println("Invalid number format! Please enter a valid number.");
        }
    }

    public void approveBorrowRequests() {
        List<Book> requestedBook = bookManager.showBorrowedRequests();
        if (requestedBook.isEmpty()) {
            System.out.println("No borrow request found.");
            return;
        }
        System.out.println("\n****** Pending Borrow Requests ******");
        for (int i=0; i<requestedBook.size(); i++) {
            Book book = requestedBook.get(i);
            System.out.println("-"+(i+1)+">>"+book.getTitle()+": "+" Requested by: "+book.getRequestedByStudentId()+" for "+book.getBorrowDays()+" Days"+"\n==================");
        }
        System.out.println("Enter the number of request to approve: ");
        try {
            int choice = Integer.parseInt(menuHandler.getScanner().nextLine());
            if (choice > 0 && choice <= requestedBook.size()) {
                Book book = requestedBook.get(choice - 1);
                bookManager.approveBorrowRequest(book.getId());
            }
            else {
                System.out.println("invalid choice!.");
            }
        }catch (NumberFormatException e) {
            System.out.println("Invalid choice number! Please enter a valid number.");
        }
    }

    public void checkBorrowStatus(Student student) {
        System.out.println("\n******** My Borrow Status *******");
        bookManager.checkBorrowStatus(student);
    }

    public void checkBorrowStatusByLibrarian() {
        System.out.println("\n******** Students Borrow Status *******");
        bookManager.checkBorrowStatusByLibrarian();
    }

    public void returnBook(Student student) {
        System.out.print("Enter book ID to return: ");
        String bookId = menuHandler.getScanner().nextLine();
        bookManager.returnBook(bookId);
    }

    public Librarian authenticateLibrarian(String username, String password) {
        return librarianManager.authenticateLibrarian(username, password);
    }

    public void editLibrarianPassword(Librarian currentlibrarian) {
        librarianManager.editLibrarianPassword(currentlibrarian);
    }

    public void addNewLibrarian() {
        System.out.println("\n--- Add New Librarian ---");

        System.out.print("Enter username: ");
        String username = menuHandler.getScanner().nextLine();

        System.out.print("Enter password: ");
        String password = menuHandler.getScanner().nextLine();

        System.out.print("Enter full name: ");
        String name = menuHandler.getScanner().nextLine();

        librarianManager.addLibrarian(username, password, name);
    }

    public void manageLibrarians() {
        while (true) {
            System.out.println("\n=== Librarian Management ===");
            System.out.println("1. Add New Librarian");
            System.out.println("2. View All Librarians");
            System.out.println("3. Back to Manager Menu");
            System.out.print("Please enter your choice: ");

            try {
                int choice = Integer.parseInt(menuHandler.getScanner().nextLine());

                switch (choice) {
                    case 1:
                        addNewLibrarian();
                        break;
                    case 2:
                        displayAllLibrarians();
                        break;
                    case 3:
                        return;
                    default:
                        System.out.println("Invalid option! Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    public void displayAllLibrarians() {
        System.out.println("\n--- All Librarians ---");
        List<Librarian> librarians = librarianManager.getLibrarians();

        if (librarians.isEmpty()) {
            System.out.println("No librarians found.");
            return;
        }

        for (int i = 0; i < librarians.size(); i++) {
            Librarian librarian = librarians.get(i);
            System.out.println((i + 1) + ". " + librarian.getName() +
                    " (Username: " + librarian.getUsername() + ")");
        }
    }


    public void displaySystemStatistics() {
        System.out.println("\n=== System Statistics ===");
        System.out.println("Total Students: " + getStudentCount());
        System.out.println("Total Books: " + getBookCount());
        System.out.println("Total Librarians: " + librarianManager.getLibrarians().size());
        System.out.println("Active Borrows: " + bookManager.getBorrowedBooksCount());
        System.out.println("Pending Requests: " + bookManager.getRequestBooksCount());
    }

    public void start() {
        menuHandler.displayMainMenu();
    }


    public static void main(String[] args) {
        LibrarySystem system = new LibrarySystem();
        system.start();
    }

    public void displayAllStudent() {
        System.out.println("\n--- All Students ---");
        List<Student> students = studentManager.getAllStudents();

        if (students.isEmpty()) {
            System.out.println("No Student found.");
            return;
        }

        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            System.out.println((i + 1) + ". " + student.getName() +
                    " (Username: " + student.getUsername() + ")");
        }
    }
    public void displayTop10StudentsWithMostDelays() {
        List<Map.Entry<String, Long>> topDelayedStudents = bookManager.getTop10StudentsWithMostDelays();

        System.out.println("\n=== Top 10 Students with Most Delays ===");

        if (topDelayedStudents.isEmpty()) {
            System.out.println("No students with delayed books found.");
            return;
        }

        for (int i = 0; i < topDelayedStudents.size(); i++) {
            Map.Entry<String, Long> entry = topDelayedStudents.get(i);
            String studentId = entry.getKey();
            Long totalDelayDays = entry.getValue();


            String studentName = "Unknown";
            Student student = findStudentById(studentId);
            if (student != null) {
                studentName = student.getName();
            }

            System.out.println((i + 1) + ". " + studentName +
                    " (ID: " + studentId +
                    ") - Total Delay: " + totalDelayDays + " days");
        }
    }
    private Student findStudentById(String studentId) {
        return studentManager.getAllStudents().stream()
                .filter(s -> s.getStudentId().equals(studentId))
                .findFirst()
                .orElse(null);
    }

}