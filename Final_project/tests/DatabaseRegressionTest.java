package Final_project.tests;

import Final_project.*;
import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

/** Integration checks against a real SQLite file, run in isolated directories. */
public class DatabaseRegressionTest {
    private static final Student STUDENT = new Student("دانشجو O'Brien", "s1", "user", "secret");

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    private static void expectFailure(Runnable action) {
        try {
            action.run();
        } catch (RuntimeException e) {
            return;
        }
        throw new AssertionError("Storage operation reported success despite failure");
    }

    private static void serialize(String path, Object value) throws Exception {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path))) {
            out.writeObject(value);
        }
    }

    private static List<Book> sampleBooks() {
        Book available = new Book("Available", "b1", "Author", 2020);
        Book requested = new Book("Requested", "b2", "Author", 2021);
        requested.setBorrowRequested(true, "s1", 7);
        Book received = new Book("Received", "b3", "Author", 2022);
        received.setBorrowRequested(true, "s1", 10);
        received.approveBorrowRequest();
        received.markAsReceived();
        Book returned = new Book("Returned", "b4", "Author", 2023);
        returned.setBorrowRequested(true, "s1", 3);
        returned.approveBorrowRequest();
        returned.markAsReceived();
        returned.returnBook();
        return Arrays.asList(available, requested, received, returned);
    }

    private static void verifyBooks() {
        Map<String, Book> books = new HashMap<>();
        for (Book book : Database.loadBooks()) books.put(book.getId(), book);
        check(books.size() == 4 && books.get("b1").isAvailable(), "Available book lost");
        Book requested = books.get("b2");
        check(requested.isRequested() && !requested.isAvailable() &&
                "s1".equals(requested.getRequestedByStudentId()) &&
                LocalDate.now().equals(requested.getRequestDate()) && requested.getBorrowDays() == 7,
                "Pending request did not survive restart");
        Book received = books.get("b3");
        check(!received.isAvailable() && !received.isRequested() && received.isReceived() &&
                "s1".equals(received.getBorrowedByStudentId()) &&
                LocalDate.now().equals(received.getBorrowStartDate()) &&
                LocalDate.now().plusDays(10).equals(received.getBorrowEndDate()) &&
                LocalDate.now().equals(received.getReceivedDate()), "Loan did not survive restart");
        check(books.get("b4").isAvailable() && books.get("b4").isReturned(), "Return lost");
    }

    public static void main(String[] args) throws Exception {
        switch (args[0]) {
            case "write":
                STUDENT.setActive(false);
                Database.saveStudents(Arrays.asList(STUDENT));
                Database.saveBooks(sampleBooks());
                Database.saveLibrarians(Arrays.asList(new Librarian("lib", "pw", "Librarian")));
                Database.saveManager(new Manager("admin", "pw", "Manager"));
                break;
            case "read":
                check(Database.loadStudents().size() == 1 && !Database.loadStudents().get(0).isActive() &&
                        STUDENT.getName().equals(Database.loadStudents().get(0).getName()), "Student lost");
                verifyBooks();
                check("lib".equals(Database.loadLibrarians().get(0).getUsername()), "Librarian lost");
                check("admin".equals(Database.loadManager().getUsername()), "Manager lost");
                break;
            case "read-migration":
                check(Database.loadStudents().size() == 1, "Migrated student lost on restart");
                verifyBooks();
                check(Database.loadLibrarians().size() == 1 && Database.loadManager() != null,
                        "Migrated staff lost on restart");
                break;
            case "manager-atomic":
                Database.saveManager(new Manager("admin", "old", "Manager"));
                expectFailure(() -> Database.saveManager(new Manager("admin", "new", null)));
                check("old".equals(Database.loadManager().getPassword()), "Failed update deleted manager");
                Database.saveManager(new Manager("admin", "new", "Manager"));
                check("new".equals(Database.loadManager().getPassword()), "Manager update failed");
                break;
            case "rollback":
                Database.saveStudents(Arrays.asList(STUDENT));
                expectFailure(() -> Database.saveStudents(Arrays.asList(STUDENT, STUDENT)));
                check(Database.loadStudents().size() == 1, "Failed batch erased students");
                Book book = sampleBooks().get(0);
                Database.saveBooks(Arrays.asList(book));
                expectFailure(() -> Database.saveBooks(Arrays.asList(book, book)));
                check(Database.loadBooks().size() == 1, "Failed batch erased books");
                Librarian librarian = new Librarian("lib", "pw", "Librarian");
                Database.saveLibrarians(Arrays.asList(librarian));
                expectFailure(() -> Database.saveLibrarians(Arrays.asList(librarian, librarian)));
                check(Database.loadLibrarians().size() == 1, "Failed batch erased librarians");
                break;
            case "load-failure":
                Database.loadStudents();
                try (Connection conn = DriverManager.getConnection("jdbc:sqlite:library.db");
                     Statement st = conn.createStatement()) {
                    st.execute("DROP TABLE students");
                }
                expectFailure(Database::loadStudents);
                break;
            case "bad-date":
                Database.saveBooks(sampleBooks());
                try (Connection conn = DriverManager.getConnection("jdbc:sqlite:library.db");
                     Statement st = conn.createStatement()) {
                    st.execute("UPDATE books SET request_date='invalid' WHERE id='b2'");
                }
                expectFailure(Database::loadBooks);
                break;
            case "migration":
            case "rename-collision":
                serialize("students.dat", Arrays.asList(STUDENT));
                serialize("Books.dat", sampleBooks());
                serialize("librarians.dat", Arrays.asList(new Librarian("lib", "pw", "Librarian")));
                serialize("manager.dat", new Manager("admin", "pw", "Manager"));
                if (args[0].equals("rename-collision")) {
                    Files.write(Paths.get("students.dat.migrated"), new byte[]{1, 2, 3});
                }
                check(Database.loadStudents().size() == 1, "Migration lost students");
                verifyBooks();
                check(Database.loadLibrarians().size() == 1 && Database.loadManager() != null, "Migration lost staff");
                if (args[0].equals("rename-collision")) {
                    check(Files.exists(Paths.get("students.dat")), "Rename failure deleted original file");
                    check(Arrays.equals(Files.readAllBytes(Paths.get("students.dat.migrated")), new byte[]{1, 2, 3}),
                            "Existing backup overwritten");
                } else {
                    for (String name : Arrays.asList("students", "Books", "librarians", "manager")) {
                        check(!Files.exists(Paths.get(name + ".dat")) &&
                                Files.exists(Paths.get(name + ".dat.migrated")), "Migration backup missing");
                    }
                }
                break;
            case "invalid-migration":
                serialize("students.dat", Arrays.asList(STUDENT, STUDENT));
                try {
                    Database.loadStudents();
                    throw new AssertionError("Failed migration allowed startup");
                } catch (ExceptionInInitializerError expected) {
                    check(Files.exists(Paths.get("students.dat")), "Failed migration lost original");
                    check(!Files.exists(Paths.get("students.dat.migrated")), "Failed migration marked successful");
                }
                try (Connection conn = DriverManager.getConnection("jdbc:sqlite:library.db");
                     Statement st = conn.createStatement();
                     ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM students")) {
                    check(rs.next() && rs.getInt(1) == 0, "Partial migration committed");
                }
                break;
            default:
                throw new AssertionError("Unknown scenario");
        }
        System.out.println("PASS " + args[0]);
    }
}
