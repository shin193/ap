package mid_project.EX7;

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
                    library.addNewBook();
                    break;
                case "2":
                    library.showOverdueBooks();
                    break;
                case "3":
                    library.addNewLibrarian();
                    break;
                case "4":
                    library.viewLibrarians();
                    break;
                case "5":
                    library.loansPerLibrarian();
                    break;
                case "6":
                    library.top10BorrowedBooks(library);
                    break;
                case "7":
                    library.showListOfBooks();
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
                    library.searchBooks(library);
                    break;
                case "2":
                    library.borrowBook(student);
                    break;
                case "3":
                   library.returnBook(student);
                    break;
                case "4":
                    library.showListOfBooks();
                    break;
                case "5":
                    library.showListOfBorrowed(student,library);
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
                    library.addNewBook();
                    break;
                case "2":
                    library.approveBookReturn(librarian);
                    break;
                case "3":
                    library.approveBookReturn(librarian);
                    break;
                case "4":
                    library.viewAllLoanHistory();
                    break;
                case "5":
                    library.viewStudentLoanHistory();
                    break;
                case "6":
                    librarian.updatePersonalInfo();
                    break;
                case "7":
                    library.showListOfBooks();
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
}
