package mid_project.EX7;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SqliteStorageHandler implements StorageHandler {
    private Connection connection;

    public SqliteStorageHandler() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:library.db");
            createTables();
        } catch (Exception e) {
            System.out.println("Error initializing database: " + e.getMessage());
        }
    }

    private void createTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS students (" +
                    "first_name TEXT, last_name TEXT, student_id TEXT PRIMARY KEY, " +
                    "major TEXT, register_date TEXT)");

            stmt.execute("CREATE TABLE IF NOT EXISTS books (" +
                    "title TEXT, author TEXT, publish_date INTEGER, " +
                    "page_count INTEGER)");

            stmt.execute("CREATE TABLE IF NOT EXISTS librarians (" +
                    "first_name TEXT, last_name TEXT, employee_id TEXT PRIMARY KEY)");

            stmt.execute("CREATE TABLE IF NOT EXISTS loans (" +
                    "book_title TEXT, student_id TEXT, borrowed_date TEXT, " +
                    "due_date TEXT, borrowed_librarian_id TEXT, " +
                    "returned_date TEXT, returned_librarian_id TEXT)");
        }
    }

    @Override
    public void saveLibrary(Library library) {
        try {
            clearTables();
            saveStudents(library.getStudents());
            saveBooks(library.getBooks());
            saveLibrarians(library.getLibrarians());
            saveLoans(library.getLoans());
        } catch (SQLException e) {
            System.out.println("Error saving library: " + e.getMessage());
        }
    }

    private void clearTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM students");
            stmt.execute("DELETE FROM books");
            stmt.execute("DELETE FROM librarians");
            stmt.execute("DELETE FROM loans");
        }
    }

    private void saveStudents(List<Student> students) throws SQLException {
        String sql = "INSERT INTO students VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (Student s : students) {
                pstmt.setString(1, s.getFirstName());
                pstmt.setString(2, s.getLastName());
                pstmt.setString(3, s.getStudentId());
                pstmt.setString(4, s.getMajor());
                pstmt.setString(5, s.getRegisterDate().toString());
                pstmt.executeUpdate();
            }
        }
    }

    private void saveBooks(List<Book> books) throws SQLException {
        String sql = "INSERT INTO books VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (Book b : books) {
                pstmt.setString(1, b.getTitle());
                pstmt.setString(2, b.getAuthor());
                pstmt.setInt(3, b.getPublishDate());
                pstmt.setInt(4, b.getBookPage());
                pstmt.executeUpdate();
            }
        }
    }

    private void saveLibrarians(List<Librarian> librarians) throws SQLException {
        String sql = "INSERT INTO librarians VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (Librarian l : librarians) {
                pstmt.setString(1, l.getFirstName());
                pstmt.setString(2, l.getLastName());
                pstmt.setString(3, l.getEmployeeId());
                pstmt.executeUpdate();
            }
        }
    }

    private void saveLoans(List<Loan> loans) throws SQLException {
        String sql = "INSERT INTO loans VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (Loan loan : loans) {
                pstmt.setString(1, loan.getBook().getTitle());
                pstmt.setString(2, loan.getStudent().getStudentId());
                pstmt.setString(3, loan.getBorrowedDate().toString());
                pstmt.setString(4, loan.getDueDate().toString());
                pstmt.setString(5, loan.getBorrowedLibrarians().getEmployeeId());
                pstmt.setString(6, loan.isReturned() ? loan.getReturnedDate().toString() : null);
                pstmt.setString(7, loan.isReturned() ? loan.getReturnedLibrarians().getEmployeeId() : null);
                pstmt.executeUpdate();
            }
        }
    }

    @Override
    public Library loadLibrary() {
        try {
            Manager manager = new Manager("arshia", "karimi", "12344321", "computer engineering");
            List<Librarian> librarians = loadLibrarians();
            librarians.add(new Librarian("mohammad", "heydari", "0001"));
            librarians.add(new Librarian("jamal", "gondaviz", "0002"));

            Library library = new Library("University Library", manager, librarians);
            library.getStudents().addAll(loadStudents());
            library.getBooks().addAll(loadBooks());
            library.getLoans().addAll(loadLoans(library));
            return library;
        } catch (SQLException e) {
            System.out.println("Error loading library: " + e.getMessage());
            return createDefaultLibrary();
        }
    }

    private List<Student> loadStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                students.add(new Student(
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("student_id"),
                        rs.getString("major"),
                        LocalDate.parse(rs.getString("register_date"))
                ));
            }
        }
        return students;
    }

    private List<Book> loadBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                books.add(new Book(
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("publish_date"),
                        rs.getInt("page_count")
                ));
            }
        }
        return books;
    }

    private List<Librarian> loadLibrarians() throws SQLException {
        List<Librarian> librarians = new ArrayList<>();
        String sql = "SELECT * FROM librarians";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                librarians.add(new Librarian(
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("employee_id")
                ));
            }
        }
        return librarians;
    }

    private List<Loan> loadLoans(Library library) throws SQLException {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loans";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Book book = findBookByTitle(library, rs.getString("book_title"));
                Student student = findStudentById(library, rs.getString("student_id"));
                Librarian borrowedLibrarian = findLibrarianById(library, rs.getString("borrowed_librarian_id"));

                Loan loan = new Loan(book, student, borrowedLibrarian,
                        LocalDate.parse(rs.getString("borrowed_date")),
                        LocalDate.parse(rs.getString("due_date")));

                String returnedDate = rs.getString("returned_date");
                if (returnedDate != null) {
                    Librarian returnedLibrarian = findLibrarianById(library, rs.getString("returned_librarian_id"));
                    loan.returnBook(LocalDate.parse(returnedDate), returnedLibrarian);
                }

                loans.add(loan);
            }
        }
        return loans;
    }

    private Book findBookByTitle(Library library, String title) {
        for (Book book : library.getBooks()) {
            if (book.getTitle().equals(title)) {
                return book;
            }
        }
        return null;
    }

    private Student findStudentById(Library library, String studentId) {
        for (Student student : library.getStudents()) {
            if (student.getStudentId().equals(studentId)) {
                return student;
            }
        }
        return null;
    }

    private Librarian findLibrarianById(Library library, String librarianId) {
        for (Librarian librarian : library.getLibrarians()) {
            if (librarian.getEmployeeId().equals(librarianId)) {
                return librarian;
            }
        }
        return null;
    }

    private Library createDefaultLibrary() {
        Manager manager = new Manager("arshia", "karimi", "12344321", "computer engineering");
        List<Librarian> librarians = new ArrayList<>();
        librarians.add(new Librarian("mohammad", "heydari", "0001"));
        librarians.add(new Librarian("jamal", "gondaviz", "0002"));
        return new Library("University Library", manager, librarians);
    }

    @Override
    protected void finalize() throws Throwable {
        if (connection != null) {
            connection.close();
        }
        super.finalize();
    }
}