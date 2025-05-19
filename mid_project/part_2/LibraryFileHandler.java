package mid_project.part_1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LibraryFileHandler {
    public static void saveLibraryToFile(Library library) {
        saveStudents(library.getStudents());
        saveBooks(library.getBooks());
        saveLibrarians(library.getLibrarians());
        saveLoan(library.getLoans());
    }

    private static void saveLoan(List<Loan> loans) {
        try (FileWriter writer = new FileWriter("Loan.txt")){
             for (Loan loan : loans) {
                writer.write(loan.getBook().getTitle()+","+loan.getStudent().getStudentId()+","+loan.getBorrowedDate()+","+loan.getDueDate()+","+loan.getBorrowedLibrarians().getEmployeeId()+"\n");
            }
        }catch (IOException e) {
            System.out.println("Error saving loans: " + e.getMessage());
        }

    }

    private static void saveStudents(List<Student> students) {
        try (FileWriter writer = new FileWriter("Students.txt")) {
            for (Student s : students) {
                writer.write(s.getFirstName() + "," + s.getLastName() + "," + s.getStudentId() + "," + s.getMajor() + "," + s.getRegisterDate() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving students: " + e.getMessage());
        }
    }

    private static void saveBooks(List<Book> books) {
        try (FileWriter writer = new FileWriter("Books.txt")) {
            for (Book b : books) {
                writer.write(b.getTitle() + "," + b.getAuthor() + "," + b.getPublishDate() + "," + b.getBookPage() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }

    private static void saveLibrarians(List<Librarian> librarians) {
        try (FileWriter writer = new FileWriter("Librarians.txt")) {
            for (Librarian l : librarians) {
                writer.write(l.getFirstName() + "," + l.getLastName() + "," + l.getEmployeeId() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving librarians: " + e.getMessage());
        }
    }
    public static Library loadLibraryFromFile() {
        Manager manager = new Manager("arshia", "karimi", "12344321", "computer engineering");
        List<Librarian> librarians = loadLibrarians();
        librarians.add(new Librarian("mohammad","heydari","0001"));
        librarians.add(new Librarian("jamal","gondaviz","0002"));
        Library library = new Library("University Library", manager, librarians);
        library.getStudents().addAll(loadStudents());
        library.getBooks().addAll(loadBooks());
        library.getLoans().addAll(loadLoans(library));
        return library;
    }

    private static List<Loan> loadLoans(Library library) {
        List<Loan> loans = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("Loan.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                String bookTitle = tokens[0];
                String studentId = tokens[1];
                LocalDate borrowedDate = LocalDate.parse(tokens[2]);
                LocalDate dueDate = LocalDate.parse(tokens[3]);
                String librarianId = tokens[4];

                Book book = findBookByTitle(library, bookTitle);
                Student student = findStudentById(library, studentId);
                Librarian librarian = findLibrarianById(library, librarianId);

                if (book != null && student != null && librarian != null) {
                    loans.add(new Loan(book, student, librarian, borrowedDate, dueDate));
                } else {
                    System.out.println("Loan data incomplete for line: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading loans: " + e.getMessage());
        }
        return loans;
    }

    private static Book findBookByTitle(Library library, String title) {
        for (Book book : library.getBooks()) {
            if (book.getTitle().equals(title)) {
                return book;
            }
        }
        System.out.println("Book not found: " + title);
        return null;
    }

    private static Student findStudentById(Library library, String studentId) {
        for (Student student : library.getStudents()) {
            if (student.getStudentId().equals(studentId)) {
                return student;
            }
        }
        System.out.println("Student not found: " + studentId);
        return null;
    }

    private static Librarian findLibrarianById(Library library, String librarianId) {
        for (Librarian librarian : library.getLibrarians()) {
            if (librarian.getEmployeeId().equals(librarianId)) {
                return librarian;
            }
        }
        System.out.println("Librarian not found: " + librarianId);
        return null;
    }


    private static List<Student> loadStudents() {
        List<Student> students = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("Students.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                String firstName = tokens[0];
                String lastName = tokens[1];
                String password = tokens[2];
                String major = tokens[3];
                LocalDate regDate = LocalDate.parse(tokens[4]);
                students.add(new Student(firstName, lastName, password, major, regDate));
            }
        } catch (IOException e) {
            System.out.println("Error loading students: " + e.getMessage());
        }
        return students;
    }

    private static List<Book> loadBooks() {
        List<Book> books = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("Books.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                String title = tokens[0];
                String author = tokens[1];
                int publishDate = Integer.parseInt(tokens[2]);
                int page = Integer.parseInt(tokens[3]);
                books.add(new Book(title, author, publishDate, page));
            }
        } catch (IOException e) {
            System.out.println("Error loading books: " + e.getMessage());
        }
        return books;
    }

    private static List<Librarian> loadLibrarians() {
        List<Librarian> librarians = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("Librarians.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                librarians.add(new Librarian(tokens[0], tokens[1], tokens[2]));
            }
        } catch (IOException e) {
            System.out.println("Error loading librarians: " + e.getMessage());
        }
        return librarians;
    }
}
