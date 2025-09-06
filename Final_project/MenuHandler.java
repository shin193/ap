package Final_project;

import java.util.Scanner;

public class MenuHandler {
    private Scanner scanner;
    private LibrarySystem librarySystem;
    private Student currentUser;
    private Librarian currentLibrarian;
    private Manager currentManager;

    public MenuHandler(LibrarySystem librarySystem) {
        this.scanner = new Scanner(System.in);
        this.librarySystem = librarySystem;
        this.currentUser = null;
        this.currentLibrarian = null;
    }
    public Scanner getScanner() {
        return this.scanner;
    }
    public void displayMainMenu() {
        while (true) {
            System.out.println("\n=== University Library Management System ===");
            System.out.println("1. login as a guest");
            System.out.println("2. Student Registration");
            System.out.println("3. Student Login");
            System.out.println("4. Manager login");
            System.out.println("5. librarian login");
            System.out.println("6. Exit");
            System.out.print("Please enter your choice: ");

            int choice = getIntInput(1, 7);

            switch (choice) {
                case 1:
                    displayGuestLogin();
                    break;
                case 2:
                    handleStudentRegistration();
                    break;
                case 3:
                    handleStudentLogin();
                    break;
                case 4:
                    handleManagerLogin();
                    break;
                case 5:
                    handleLibrarianLogin();
                    break;
                case 6:
                    System.out.println("Exiting system. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
            System.out.println("___________________________");
        }
    }

    private void displayStudentCount() {
        int studentCount = librarySystem.getStudentCount();
        System.out.println("\nTotal registered students: " + studentCount);
    }

    private void handleStudentRegistration() {
        System.out.println("\n--- New Student Registration ---");

        System.out.print("Student name: ");
        String name = scanner.nextLine();

        System.out.print("Student ID: ");
        String studentId = scanner.nextLine();

        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        librarySystem.registerStudent(name, studentId, username, password);
    }

    private void handleStudentLogin() {
        System.out.println("\n--- Student Login ---");

        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        currentUser = librarySystem.authenticateStudent(username, password);

        if (currentUser != null && currentUser.isActive()) {
            System.out.println("Login successful! Welcome, " + currentUser.getName());
            displayLoggedInStudentMenu();
        }
        else if (!currentUser.isActive()){
            System.out.println("Sorry , Your Account Has Been Disabled,You Can't Login Right Now.");
        }
        else  {
            System.out.println("Invalid username or password. Please try again.");
        }
    }

    private void handleManagerLogin() {
        System.out.println("\n--- Manager Login ---");

        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        currentManager = librarySystem.authenticateManager(username, password);

        if (currentManager != null) {
            System.out.println("Login successful! Welcome, " + currentManager.getName());
            displayLoggedInManagerMenu();
        } else {
            System.out.println("Invalid username or password. Please try again.");
        }
    }


    private void displayGuestLogin() {
        while (true){
            System.out.println("\n=== Guest User ===");
            System.out.println("1. View total student count");
            System.out.println("2. view total book count");
            System.out.println("3. Search for a book");
            System.out.println("4. View loans count");
            System.out.println("5. Exit to main menu");

            String action = scanner.nextLine();
            switch (action) {
                case "1":
                    displayStudentCount();
                    break;
                case "2":
                    //FPR_2-3
                    displayBookCount();
                    break;
                case "3":
                    librarySystem.searchBooksByGuest();
                    break;
                case "4":
                    //FPR_2-3
                    librarySystem.getLoanCounts();
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Invalid action! Please try again.");
            }

        }
    }

    private void displayBookCount() {
        int bookCount = librarySystem.getBookCount();
        System.out.println("\nTotal books are : " + bookCount);
    }

    private void displayLoggedInStudentMenu() {
        while (currentUser != null) {
            System.out.println("\n=== Student Dashboard ===");
            System.out.println("1. View My Information");
            System.out.println("2. Edit My Information");
            System.out.println("3. Search For a Book");
            System.out.println("4. Request To Borrow Book");
            System.out.println("5. Check My Borrow Status");
            System.out.println("6. Return a Book");
            System.out.println("7. View Available Books");
            System.out.println("8. Logout");
            System.out.print("Please Enter Your Choice: ");

            int choice = getIntInput(1, 8);

            switch (choice) {
                case 1:
                    System.out.println("\n--- My Information ---");
                    System.out.println(currentUser);
                    break;
                case 2:
                    librarySystem.editStudentInfo(currentUser);
                    break;
                case 3:
                    librarySystem.searchBooks();
                    break;
                case 4:
                    librarySystem.requestBorrowBook(currentUser);
                    break;
                case 5:
                    librarySystem.checkBorrowStatus(currentUser);
                    break;
                case 6:
                    librarySystem.returnBook(currentUser);
                    break;
                case 7:
                    librarySystem.displayAvailableBooks();
                    break;
                case 8:
                    currentUser = null;
                    System.out.println("Logged out successfully.");
                    return;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }

    private void handleLibrarianLogin() {
        System.out.println("Please Enter Your Username: ");
        String username = scanner.nextLine();
        System.out.println("Please Enter Your Password: ");
        String password = scanner.nextLine();
        currentLibrarian = librarySystem.authenticateLibrarian(username, password);
        if (currentLibrarian != null) {
            System.out.println("Login successful! Welcome, " + currentLibrarian.getName());
            displayLoggedInLibrarianMenu();
        }
        else {
            System.out.println("Invalid username or password. Please try again.");
        }
    }

    private void displayLoggedInLibrarianMenu() {
        while (true){
            System.out.println("\n=== Librarian Menu ===");
            System.out.println("1. View My Information");
            System.out.println("2. Edit My Password");
            System.out.println("3. Add A New Book To the Library");
            System.out.println("4. Search For A Book");
            System.out.println("5. View All Books With Borrow Status");
            System.out.println("6. Edit Book Information");
            System.out.println("7. Accept Borrow Requests");
            System.out.println("8. Active Or Deactivate A Student");
            System.out.println("9. Check All Borrow Status");
            System.out.println("10. Logout");
            System.out.print("Please Enter Your Choice: ");
            int choice = getIntInput(1, 10);

            switch (choice) {
                case 1:
                    System.out.println("\n--- My Information ---");
                    System.out.println(currentLibrarian);
                    break;
                case 2:
                    librarySystem.editLibrarianPassword(currentLibrarian);
                    break;
                case 3:
                    librarySystem.addBook();
                    break;
                case 4:
                    librarySystem.searchBooks();
                    break;
                case 5:
                    librarySystem.displayAllBooks();
                    break;
                case 6:
                    librarySystem.editBookInfo();
                    break;
                case 7:
                    librarySystem.approveBorrowRequests();
                    break;
                case 8:
                    librarySystem.studentAccountActivity();
                    break;
                case 9:
                    librarySystem.checkBorrowStatusByLibrarian();
                    break;
                case 10:
                    currentLibrarian = null;
                    System.out.println("Logged out successfully.");
                    return;
                default:
                    System.out.println("Invalid option!");
                    break;
            }

        }
    }

    private void displayLoggedInManagerMenu() {
        while (currentManager != null) {
            System.out.println("\n=== Manager Dashboard ===");
            System.out.println("1. View My Information");
            System.out.println("2. Edit My Password");
            System.out.println("3. View System Statistics");
            System.out.println("4. Manage Librarians");
            System.out.println("5. View All Students");
            System.out.println("6. View All Books");
            System.out.println("7. View Borrow Status");
            System.out.println("8. View Top 10 Students with Most Delays");
            System.out.println("9. Logout");
            System.out.print("Please enter your choice: ");

            int choice = getIntInput(1, 9);

            switch (choice) {
                case 1:
                    System.out.println("\n--- My Information ---");
                    System.out.println(currentManager);
                    break;
                case 2:
                    librarySystem.editManagerPassword(currentManager);
                    break;
                case 3:
                    librarySystem.displaySystemStatistics();
                    break;
                case 4:
                    librarySystem.manageLibrarians();
                    break;
                case 5:
                    librarySystem.displayAllStudent();
                    break;
                case 6:
                    librarySystem.displayAllBooks();
                    break;
                case 7:
                    //FPR_4-4
                    librarySystem.checkBorrowStatusByLibrarian();
                    break;
                case 8:
                    //FPR_4-4
                    librarySystem.displayTop10StudentsWithMostDelays();
                    break;
                case 9:
                    currentManager = null;
                    System.out.println("Logged out successfully.");
                    return;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }

    private int getIntInput(int min, int max) {
        while (true) {
            try {
                int input = Integer.parseInt(scanner.nextLine());
                if (input >= min && input <= max) {
                    return input;
                }
                System.out.printf("Please enter a number between %d and %d: ", min, max);
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }
}