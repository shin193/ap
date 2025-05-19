package mid_project.part_1;

import java.time.LocalDate;
import java.util.*;

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
            System.out.println("\t\t\t\t\t\t\t7. View list of books");
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t8. log out");
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
                    loansPerLibrarian(library);
                    break;
                case "6":
                    top10BorrowedBooks(library);
                    break;
                case "7":
                    showListOfBooks(library);
                    break;
                case "8":
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
            System.out.println("\t\t\t\t\t\t\t4. View list of Books");
            System.out.println("\t\t\t\t\t\t\t5. View list Borrowed Books and Overdue Books");
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t6. Log out");
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
                    showListOfBooks(library);
                    break;
                case "5":
                    showListOfBorrowed(student,library);
                    break;
                case "6":
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
            System.out.println("\t\t\t\t\t7) View list of Books");
            System.out.println("\t\t\t\t\t8) Log out");
            System.out.println("\t\t\t  *=============================================================================*");
            System.out.print("\tChoose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addBook(librarian, library);
                    break;
                case "2":
                    approveLoanRequest(library, librarian);
                    break;
                case "3":
                    approveBookReturn (library , librarian);
                    break;
                case "4":
                    viewAllLoanHistory(library);
                    break;
                case "5":
                    viewStudentLoanHistory(library);
                    break;
                case "6":
                    updatePersonalInfo(librarian);
                    break;
                case "7":
                    showListOfBooks(library);
                    break;
                case "8":
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
    private static void loansPerLibrarian(Library library) {
        System.out.println("List of loans per librarian:");
        for(Librarian librarian : library.getLibrarians()) {
            int c=0;
            for(Loan loan : library.getLoans()) {
                if(loan.getBorrowedLibrarians().equals(librarian)) {
                    c++;
                }
                System.out.println("****"+librarian.getFullName()+":"+c+"loans"+"****");
            }
        }
    }
    private static void approveLoanRequest(Library library, Librarian librarian) {
        System.out.println("List of available books:");
        List<Book> availableBooks = new ArrayList<>();
        for (Book book : library.getBooks()) {
            boolean isBorrowed = false;
            for (Loan loan : library.getLoans()) {
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
        Student student = findStudentById(library, studentId);

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
        library.addLoan(loan);
        System.out.println("Loan approved successfully. Due date: " + dueDate);
    }
    private static void approveBookReturn(Library library, Librarian librarian) {
        System.out.println("List of active loans:");
        boolean hasActiveLoans = false;
        for (Loan loan : library.getLoans()) {
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
        for (Loan loan : library.getLoans()) {
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
    private static void viewAllLoanHistory(Library library) {
        System.out.println("Loan Histories:");
        for (Loan loan : library.getLoans()) {
            System.out.println("Book: " + loan.getBook().getTitle() +
                    ", Student: " + loan.getStudent().getFullName() +
                    ", Borrowed: " + loan.getBorrowedDate() +
                    ", Due: " + loan.getDueDate() +
                    (loan.isReturned() ? ", Returned: " + loan.getReturnedDate() : ", Not returned"));
            System.out.println();
        }
    }

    private static void viewStudentLoanHistory(Library library) {
        System.out.print("Enter student ID: ");
        String studentId = scanner.nextLine();

        Student student = findStudentById(library, studentId);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.println("Loan history for " + student.getFullName() + ":");
        boolean hasLoans = false;
        for (Loan loan : library.getLoans()) {
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

    private static Student findStudentById(Library library, String studentId) {
        for (Student student : library.getStudents()) {
            if (student.getStudentId().equals(studentId)) {
                return student;
            }
        }
        return null;
    }
    private static void top10BorrowedBooks(Library library) {
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
    private static void showListOfBooks(Library library) {
        System.out.println("list of books: ");
        for (Book book : library.getBooks()) {
            System.out.println(book.getTitle());
        }
    }
    private static void showListOfBorrowed(Student student, Library library) {
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


}
