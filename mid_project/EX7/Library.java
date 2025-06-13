package mid_project.EX7;

import java.time.LocalDate;
import java.util.*;
import java.util.Random;


public class Library {
    private final String name;
    private final List<Book> books;
    private final List<Student> students;
    private final List<Librarian> Librarians;
    private final List<Loan> loans;
    private final Manager manager;
    private Random random;
    static Scanner scanner = new Scanner(System.in);
    public Library(String name,Manager manager,List<Librarian> librarians) {
        this.name = name;
        this.Librarians = librarians;
        this.books = new ArrayList<Book>();
        this.students = new ArrayList<>();
        this.loans = new ArrayList<>();
        this.manager = manager;
    }
    public Manager getManager() {
        return manager;
    }
    public String getName() {
        return name;
    }
    public List<Book> getBooks() {
        return books;
    }
    public List<Student> getStudents() {
        return students;
    }
    public List<Librarian> getLibrarians() {
        return Librarians;
    }
    public List<Loan> getLoans() {
        return loans;
    }
    public void addStudent(Student student) {
        students.add(student);
    }
    public void addBook(Book book) {
        books.add(book);
    }
    public void addLoan(Loan loan) {
        loans.add(loan);
    }
    public void addLibrarian(Librarian librarian) {Librarians.add(librarian);}
    public void searchBooks(Library library) {
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
    public void viewAllLoanHistory() {
        System.out.println("Loan Histories:");
        for (Loan loan : getLoans()) {
            System.out.println("Book: " + loan.getBook().getTitle() +
                    ", Student: " + loan.getStudent().getFullName() +
                    ", Borrowed: " + loan.getBorrowedDate() +
                    ", Due: " + loan.getDueDate() +
                    (loan.isReturned() ? ", Returned: " + loan.getReturnedDate() : ", Not returned"));
            System.out.println();
        }
    }
    public void approveBookReturn(Librarian librarian) {
        System.out.println("List of active loans:");
        boolean hasActiveLoans = false;
        for (Loan loan : getLoans()) {
            if (!loan.isReturned()) {
                System.out.println("Book: " + loan.getBook().getTitle() +
                        ", Student: " + loan.getStudent().getFullName() +
                        ", Due Date: " + loan.getDueDate());
                hasActiveLoans = true;
            }
        }

        if (!hasActiveLoans) {
            System.out.println("No active loans to return.");
            return;
        }

        System.out.print("Enter book title to approve return: ");
        String title = scanner.nextLine();

        System.out.print("Enter student ID: ");
        String studentId = scanner.nextLine();

        Loan foundLoan = null;
        for (Loan loan : getLoans()) {
            if (loan.getBook().getTitle().equals(title) &&
                    loan.getStudent().getStudentId().equals(studentId) &&
                    !loan.isReturned()) {
                foundLoan = loan;
                break;
            }
        }

        if (foundLoan == null) {
            System.out.println("Active loan not found.");
            return;
        }

        foundLoan.returnBook(LocalDate.now(), librarian);
        System.out.println("Book return approved successfully.");
    }
    public Student findStudentById(String studentId) {
        for (Student student : getStudents()) {
            if (student.getStudentId().equals(studentId)) {
                return student;
            }
        }
        return null;
    }
    public void showListOfBorrowed(Student student, Library library) {
        if (!library.getStudents().contains(student)) {
            System.out.println("Error: Student not registered in the library.");
            return;
        }

        System.out.println("\n**** Borrowed Books Report for " + student.getFullName() + " ****");

        List<Loan> activeLoans = new ArrayList<>();
        List<Loan> overdueLoans = new ArrayList<>();
        for (Loan loan : library.getLoans()) {
            if (loan.getStudent().equals(student) && !loan.isReturned()) {
                activeLoans.add(loan);
                if (loan.isOverdue()) {
                    overdueLoans.add(loan);
                }
            }
        }

        if (activeLoans.isEmpty()) {
            System.out.println("No currently borrowed books.");
        } else {
            System.out.println("Currently borrowed books (" + activeLoans.size() + "):");
            for (Loan loan : activeLoans) {
                String status = loan.isOverdue() ? "OVERDUE!" : "Due soon";
                System.out.println("- " + loan.getBook().getTitle() +
                        " (Borrowed: " + loan.getBorrowedDate() +
                        ", Due: " + loan.getDueDate() + ") " + status);
            }
        }

        if (!overdueLoans.isEmpty()) {
            System.out.println("\n! OVERDUE BOOKS !");
            for (Loan loan : overdueLoans) {
                long daysOverdue = LocalDate.now().until(loan.getDueDate()).getDays() * -1;
                System.out.println("- " + loan.getBook().getTitle() +
                        " (Due: " + loan.getDueDate() +
                        ", " + daysOverdue + " days overdue)");
            }
        }

        System.out.println("********************************************");
    }
    public void approveLoanRequest(Librarian librarian) {
        System.out.println("List of available books:");
        List<Book> availableBooks = new ArrayList<>();
        for (Book book : getBooks()) {
            boolean isBorrowed = false;
            for (Loan loan : getLoans()) {
                if (loan.getBook().equals(book) && !loan.isReturned()) {
                    isBorrowed = true;
                    break;
                }
            }
            if (!isBorrowed) {
                availableBooks.add(book);
                System.out.println(book);
            }
        }

        if (availableBooks.isEmpty()) {
            System.out.println("No books available for loan.");
            return;
        }

        System.out.print("Enter student ID: ");
        String studentId = scanner.nextLine();
        Student student = findStudentById(studentId);

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.print("Enter book title to approve loan: ");
        String title = scanner.nextLine();

        Book foundBook = null;
        for (Book book : availableBooks) {
            if (book.getTitle().equals(title)) {
                foundBook = book;
                break;
            }
        }

        if (foundBook == null) {
            System.out.println("Book not found or not available.");
            return;
        }

        LocalDate borrowedDate = LocalDate.now();
        LocalDate dueDate = borrowedDate.plusDays(14);
        Loan loan = new Loan(foundBook, student, librarian, borrowedDate, dueDate);
        addLoan(loan);
        System.out.println("Loan approved successfully. Due date: " + dueDate);
    }
    public void showListOfBooks() {
        System.out.println("list of books: ");
        for (Book book : getBooks()) {
            System.out.println(book.getTitle());
        }
    }
    public void top10BorrowedBooks(Library library) {
        Map<Book, Integer> borrowCounts = new HashMap<>();

        for (Loan loan : library.getLoans()) {
            Book book = loan.getBook();
            borrowCounts.put(book, borrowCounts.getOrDefault(book, 0) + 1);
        }

        Book[] books = borrowCounts.keySet().toArray(new Book[0]);
        int[] counts = new int[books.length];
        for (int i = 0; i < books.length; i++) {
            counts[i] = borrowCounts.get(books[i]);
        }
        for (int i = 0; i < books.length - 1; i++) {
            for (int j = 0; j < books.length - i - 1; j++) {
                if (counts[j] < counts[j+1]) {
                    Book tempBook = books[j];
                    books[j] = books[j+1];
                    books[j+1] = tempBook;

                    int tempCount = counts[j];
                    counts[j] = counts[j+1];
                    counts[j+1] = tempCount;
                }
            }
        }
        System.out.println("Top 10 Most Borrowed Books:");
        int limit = Math.min(10, books.length);
        for (int i = 0; i < limit; i++) {
            System.out.println((i+1) + ". " + books[i].getTitle() +
                    " by " + books[i].getAuthor() +
                    " - " + counts[i] + " borrows");
        }
    }
    public void viewStudentLoanHistory() {
        System.out.print("Enter student ID: ");
        String studentId = scanner.nextLine();

        Student student = findStudentById(studentId);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.println("Loan history for " + student.getFullName() + ":");
        boolean hasLoans = false;
        for (Loan loan : getLoans()) {
            if (loan.getStudent().equals(student)) {
                System.out.println("Book: " + loan.getBook().getTitle() +
                        ", Borrowed: " + loan.getBorrowedDate() +
                        ", Due: " + loan.getDueDate() +
                        (loan.isReturned() ? ", Returned: " + loan.getReturnedDate() : ", Not returned"));
                hasLoans = true;
            }
        }

        if (!hasLoans) {
            System.out.println("No loan history found for this student.");
        }
    }
    public Librarian getRandomLibrarian() {
        List<Librarian> librarians = getLibrarians();
        return librarians.get(random.nextInt(librarians.size()));
    }
    public void loansPerLibrarian() {
        System.out.println("List of loans per librarian:");
        for(Librarian librarian : getLibrarians()) {
            int c=0;
            for(Loan loan : getLoans()) {
                if(loan.getBorrowedLibrarians().equals(librarian)) {
                    c++;
                }
                System.out.println("****"+librarian.getFullName()+":"+c+"loans"+"****");
            }
        }
    }
    public void viewLibrarians() {
        System.out.println("List of librarians:");
        for (Librarian librarian : getLibrarians()) {
            System.out.println("- " + librarian.getFullName() + " (ID: " + librarian.getEmployeeId() + ")");
        }
    }
    public void addNewLibrarian () {
        System.out.println("Enter librarian's first name: ");
        String firstName = scanner.nextLine();
        System.out.println("Enter librarian's last name: ");
        String lastName = scanner.nextLine();
        System.out.println("Enter librarian's employee ID: ");
        String employeeId = scanner.nextLine();
        Librarian newLibrarian = new Librarian(firstName, lastName, employeeId);
        addLibrarian(newLibrarian);
        System.out.println("Librarian : "+newLibrarian.getFullName()+" successfully added.");
    }
    public void showOverdueBooks() {
        System.out.println("Overdue Books:");
        for (Loan loan : getLoans()) {
            if (loan.isOverdue()) {
                System.out.println(loan);
            }
        }
    }
    public void returnBook(Student student) {
        List<Loan> loans = getLoans();
        boolean found = false;
        for (Loan loan : loans) {
            if (loan.getStudent().equals(student) && !loan.isReturned()) {
                Librarian returnLibrarian =getRandomLibrarian();
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
    public void borrowBook(Student student) {
        System.out.print("Enter exact title of the book: ");
        String title = scanner.nextLine();
        Book foundBook = null;
        for (Book book : getBooks()) {
            if (book.getTitle().equals(title)) {
                foundBook = book;
                break;
            }
        }
        for (Loan loan : getLoans()) {
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

        Librarian librarian = getRandomLibrarian();
        LocalDate borrowedDate = LocalDate.now();
        LocalDate dueDate = borrowedDate.plusDays(14);

        Loan loan = new Loan(foundBook, student, librarian, borrowedDate, dueDate);
        addLoan(loan);
        System.out.println("Book borrowed successfully!. Due date: " + dueDate + ". Librarian: " + librarian.getFullName());
    }
    public void addNewBook() {
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
        addBook(book);
        System.out.println("Book added successfully!.");
    }

}