package Final_project;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * SQLite persistence layer of the library system.
 * All data (students, books, librarians, manager) is stored in a
 * single lightweight database file: library.db
 */
public class Database {
    private static final String DB_URL = "jdbc:sqlite:library.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new StorageException("SQLite driver not found on the classpath", e);
        }
        try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS students (" +
                    "student_id TEXT PRIMARY KEY," +
                    "name TEXT NOT NULL," +
                    "username TEXT NOT NULL UNIQUE," +
                    "password TEXT NOT NULL," +
                    "active INTEGER NOT NULL DEFAULT 1)");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS books (" +
                    "id TEXT PRIMARY KEY," +
                    "title TEXT NOT NULL," +
                    "author TEXT NOT NULL," +
                    "publish_year INTEGER NOT NULL," +
                    "is_available INTEGER NOT NULL DEFAULT 1," +
                    "borrow_requested INTEGER NOT NULL DEFAULT 0," +
                    "requested_by_student_id TEXT," +
                    "request_date TEXT," +
                    "borrow_days INTEGER NOT NULL DEFAULT 0," +
                    "borrowed_by_student_id TEXT," +
                    "borrow_start_date TEXT," +
                    "borrow_end_date TEXT," +
                    "received_date TEXT," +
                    "is_received INTEGER NOT NULL DEFAULT 0," +
                    "is_returned INTEGER NOT NULL DEFAULT 0)");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS librarians (" +
                    "username TEXT PRIMARY KEY," +
                    "name TEXT NOT NULL," +
                    "password TEXT NOT NULL)");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS manager (" +
                    "id INTEGER PRIMARY KEY CHECK (id = 1)," +
                    "username TEXT NOT NULL," +
                    "password TEXT NOT NULL," +
                    "name TEXT NOT NULL)");
            migrateLegacyData();
        } catch (SQLException e) {
            throw new StorageException("Could not initialize library.db", e);
        }
    }

    private Database() {
    }

    private static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL);
        try (Statement st = conn.createStatement()) {
            st.execute("PRAGMA busy_timeout = 5000");
            return conn;
        } catch (SQLException e) {
            conn.close();
            throw e;
        }
    }

    //student handling
    public static boolean hasStudentLoans(String studentId) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT 1 FROM books WHERE requested_by_student_id = ? " +
                             "OR borrowed_by_student_id = ? LIMIT 1")) {
            ps.setString(1, studentId);
            ps.setString(2, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new StorageException("Could not check student loans", e);
        }
    }

    public static void saveStudents(List<Student> students) {
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            try (Statement st = conn.createStatement();
                 PreparedStatement ps = conn.prepareStatement(
                         "INSERT INTO students (student_id, name, username, password, active) VALUES (?,?,?,?,?)")) {
                st.executeUpdate("DELETE FROM students");
                for (Student student : students) {
                    ps.setString(1, student.getStudentId());
                    ps.setString(2, student.getName());
                    ps.setString(3, student.getUsername());
                    ps.setString(4, student.getPassword());
                    ps.setInt(5, student.isActive() ? 1 : 0);
                    ps.addBatch();
                }
                ps.executeBatch();
                conn.commit();
            } catch (SQLException | RuntimeException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new StorageException("Could not save students", e);
        }
    }

    public static List<Student> loadStudents() {
        List<Student> students = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM students")) {
            while (rs.next()) {
                Student student = new Student(rs.getString("name"), rs.getString("student_id"),
                        rs.getString("username"), rs.getString("password"));
                student.setActive(rs.getInt("active") == 1);
                students.add(student);
            }
        } catch (SQLException e) {
            throw new StorageException("Could not load students", e);
        }
        return students;
    }

    //book handling
    public static void saveBooks(List<Book> books) {
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            try (Statement st = conn.createStatement();
                 PreparedStatement ps = conn.prepareStatement(
                         "INSERT INTO books (id, title, author, publish_year, is_available," +
                                 " borrow_requested, requested_by_student_id, request_date, borrow_days," +
                                 " borrowed_by_student_id, borrow_start_date, borrow_end_date," +
                                 " received_date, is_received, is_returned) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)")) {
                st.executeUpdate("DELETE FROM books");
                for (Book book : books) {
                    ps.setString(1, book.getId());
                    ps.setString(2, book.getTitle());
                    ps.setString(3, book.getAuthor());
                    ps.setInt(4, book.getPublishYear());
                    setBoolean(ps, 5, book.isAvailable());
                    setBoolean(ps, 6, book.isRequested());
                    ps.setString(7, book.getRequestedByStudentId());
                    ps.setString(8, toDateString(book.getRequestDate()));
                    ps.setInt(9, book.getBorrowDays());
                    ps.setString(10, book.getBorrowedByStudentId());
                    ps.setString(11, toDateString(book.getBorrowStartDate()));
                    ps.setString(12, toDateString(book.getBorrowEndDate()));
                    ps.setString(13, toDateString(book.getReceivedDate()));
                    setBoolean(ps, 14, book.isReceived());
                    setBoolean(ps, 15, book.isReturned());
                    ps.addBatch();
                }
                ps.executeBatch();
                conn.commit();
            } catch (SQLException | RuntimeException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new StorageException("Could not save books", e);
        }
    }

    public static List<Book> loadBooks() {
        List<Book> books = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM books")) {
            while (rs.next()) {
                Book book = new Book(rs.getString("title"), rs.getString("id"),
                        rs.getString("author"), rs.getInt("publish_year"));
                book.restoreState(
                        rs.getInt("is_available") == 1,
                        fromDateString(rs.getString("borrow_start_date")),
                        fromDateString(rs.getString("borrow_end_date")),
                        fromDateString(rs.getString("request_date")),
                        rs.getString("borrowed_by_student_id"),
                        rs.getInt("borrow_requested") == 1,
                        rs.getString("requested_by_student_id"),
                        rs.getInt("borrow_days"),
                        fromDateString(rs.getString("received_date")),
                        rs.getInt("is_received") == 1,
                        rs.getInt("is_returned") == 1);
                books.add(book);
            }
        } catch (SQLException | DateTimeParseException e) {
            throw new StorageException("Could not load books", e);
        }
        return books;
    }

    //librarian handling
    public static void saveLibrarians(List<Librarian> librarians) {
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            try (Statement st = conn.createStatement();
                 PreparedStatement ps = conn.prepareStatement(
                         "INSERT INTO librarians (username, name, password) VALUES (?,?,?)")) {
                st.executeUpdate("DELETE FROM librarians");
                for (Librarian librarian : librarians) {
                    ps.setString(1, librarian.getUsername());
                    ps.setString(2, librarian.getName());
                    ps.setString(3, librarian.getPassword());
                    ps.addBatch();
                }
                ps.executeBatch();
                conn.commit();
            } catch (SQLException | RuntimeException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new StorageException("Could not save librarians", e);
        }
    }

    public static List<Librarian> loadLibrarians() {
        List<Librarian> librarians = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM librarians")) {
            while (rs.next()) {
                librarians.add(new Librarian(rs.getString("username"), rs.getString("password"), rs.getString("name")));
            }
        } catch (SQLException e) {
            throw new StorageException("Could not load librarians", e);
        }
        return librarians;
    }

    //manager handling
    public static void saveManager(Manager manager) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO manager (id, username, password, name) VALUES (1,?,?,?) " +
                             "ON CONFLICT(id) DO UPDATE SET username=excluded.username, " +
                             "password=excluded.password, name=excluded.name")) {
            ps.setString(1, manager.getUsername());
            ps.setString(2, manager.getPassword());
            ps.setString(3, manager.getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new StorageException("Could not save manager", e);
        }
    }

    public static Manager loadManager() {
        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM manager WHERE id = 1")) {
            if (rs.next()) {
                return new Manager(rs.getString("username"), rs.getString("password"), rs.getString("name"));
            }
        } catch (SQLException e) {
            throw new StorageException("Could not load manager", e);
        }
        return null;
    }

    //one-time migration of the old binary .dat files (if any) into library.db
    private static void migrateLegacyData() {
        migrateLegacyStudents();
        migrateLegacyBooks();
        migrateLegacyLibrarians();
        migrateLegacyManager();
    }

    @SuppressWarnings("unchecked")
    private static void migrateLegacyStudents() {
        File legacy = new File("students.dat");
        if (!legacy.exists() || !isTableEmpty("students")) {
            return;
        }
        try {
            List<Student> students;
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(legacy))) {
                students = (List<Student>) ois.readObject();
            }
            saveStudents(students);
            renameMigratedFile(legacy);
            System.out.println("Migrated " + students.size() + " students from students.dat into library.db");
        } catch (Exception e) {
            throw new StorageException("Could not migrate students.dat; original file preserved", e);
        }
    }

    @SuppressWarnings("unchecked")
    private static void migrateLegacyBooks() {
        File legacy = new File("Books.dat");
        if (!legacy.exists() || !isTableEmpty("books")) {
            return;
        }
        try {
            List<Book> books;
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(legacy))) {
                books = (List<Book>) ois.readObject();
            }
            saveBooks(books);
            renameMigratedFile(legacy);
            System.out.println("Migrated " + books.size() + " books from Books.dat into library.db");
        } catch (Exception e) {
            throw new StorageException("Could not migrate Books.dat; original file preserved", e);
        }
    }

    @SuppressWarnings("unchecked")
    private static void migrateLegacyLibrarians() {
        File legacy = new File("librarians.dat");
        if (!legacy.exists() || !isTableEmpty("librarians")) {
            return;
        }
        try {
            List<Librarian> librarians;
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(legacy))) {
                librarians = (List<Librarian>) ois.readObject();
            }
            saveLibrarians(librarians);
            renameMigratedFile(legacy);
            System.out.println("Migrated " + librarians.size() + " librarians from librarians.dat into library.db");
        } catch (Exception e) {
            throw new StorageException("Could not migrate librarians.dat; original file preserved", e);
        }
    }

    private static void migrateLegacyManager() {
        File legacy = new File("manager.dat");
        if (!legacy.exists() || !isTableEmpty("manager")) {
            return;
        }
        try {
            Manager manager;
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(legacy))) {
                manager = (Manager) ois.readObject();
            }
            saveManager(manager);
            renameMigratedFile(legacy);
            System.out.println("Migrated manager from manager.dat into library.db");
        } catch (Exception e) {
            throw new StorageException("Could not migrate manager.dat; original file preserved", e);
        }
    }

    private static boolean isTableEmpty(String table) {
        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM " + table)) {
            return rs.next() && rs.getInt(1) == 0;
        } catch (SQLException e) {
            throw new StorageException("Could not check legacy migration for " + table, e);
        }
    }

    private static void renameMigratedFile(File legacy) {
        try {
            Files.move(legacy.toPath(), legacy.toPath().resolveSibling(legacy.getName() + ".migrated"));
        } catch (IOException e) {
            System.err.println("Data imported, but could not rename " + legacy +
                    "; original file preserved: " + e.getMessage());
        }
    }

    private static void setBoolean(PreparedStatement ps, int index, boolean value) throws SQLException {
        ps.setInt(index, value ? 1 : 0);
    }

    private static String toDateString(LocalDate date) {
        return date == null ? null : date.toString();
    }

    private static LocalDate fromDateString(String date) {
        return date == null ? null : LocalDate.parse(date);
    }
}
