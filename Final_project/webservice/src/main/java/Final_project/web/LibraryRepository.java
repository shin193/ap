package Final_project.web;

import static Final_project.web.ApiModels.*;
import static org.springframework.http.HttpStatus.*;

import Final_project.Book;
import Final_project.Passwords;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/** Uses row-level SQL updates so requests never replace a whole table snapshot. */
@Repository
public class LibraryRepository {
    private final JdbcTemplate jdbc;
    public LibraryRepository(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    Optional<Account> account(Role role, String username) {
        String table = table(role);
        List<Account> matches = jdbc.query("SELECT * FROM " + table + " WHERE username = ?", (rs, row) ->
                new Account(role, rs.getString("username"), rs.getString("password"), rs.getString("name"),
                        role == Role.STUDENT ? rs.getString("student_id") : null,
                        role != Role.STUDENT || rs.getInt("active") == 1), username);
        return matches.stream().findFirst();
    }

    void upgradePassword(Account account, String rawPassword) {
        if (!Passwords.isHashed(account.password())) {
            jdbc.update("UPDATE " + table(account.role()) + " SET password = ? WHERE username = ? AND password = ?",
                    Passwords.hash(rawPassword), account.username(), account.password());
        }
    }

    private String table(Role role) {
        return switch (role) {
            case STUDENT -> "students";
            case LIBRARIAN -> "librarians";
            case MANAGER -> "manager";
        };
    }

    StudentView register(Registration input) {
        jdbc.update("INSERT INTO students(student_id,name,username,password,active) VALUES(?,?,?,?,1)",
                input.studentId(), input.name(), input.username(), Passwords.hash(input.password()));
        return new StudentView(input.studentId(), input.name(), input.username(), true);
    }

    List<StudentView> students() {
        return jdbc.query("SELECT student_id,name,username,active FROM students ORDER BY student_id",
                (rs, row) -> new StudentView(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4) == 1));
    }

    void setActive(String id, boolean active) {
        if (jdbc.update("UPDATE students SET active = ? WHERE student_id = ?", active ? 1 : 0, id) == 0)
            throw new ApiException(NOT_FOUND, "Student not found");
    }

    Profile addLibrarian(LibrarianInput input) {
        jdbc.update("INSERT INTO librarians(username,name,password) VALUES(?,?,?)",
                input.username(), input.name(), Passwords.hash(input.password()));
        return new Profile(Role.LIBRARIAN, input.username(), input.name(), null, true);
    }

    List<Book> books(String title, String author, Integer year, Boolean available) {
        // Parameterized LIKE searches treat user input as literal text.
        return jdbc.query("SELECT * FROM books WHERE (? IS NULL OR instr(lower(title), lower(?)) > 0) " +
                        "AND (? IS NULL OR instr(lower(author), lower(?)) > 0) " +
                        "AND (? IS NULL OR publish_year = ?) AND (? IS NULL OR is_available = ?) ORDER BY id",
                this::mapBook, title, title, author, author, year, year, available,
                available == null ? null : available ? 1 : 0);
    }

    Book book(String id) {
        return jdbc.query("SELECT * FROM books WHERE id = ?", this::mapBook, id).stream().findFirst()
                .orElseThrow(() -> new ApiException(NOT_FOUND, "Book not found"));
    }

    Book addBook(BookInput input) {
        jdbc.update("INSERT INTO books(id,title,author,publish_year) VALUES(?,?,?,?)",
                input.id(), input.title(), input.author(), input.publishYear());
        return book(input.id());
    }

    Book updateBook(String id, BookUpdate input) {
        if (jdbc.update("UPDATE books SET title = ?, author = ?, publish_year = ? WHERE id = ?",
                input.title(), input.author(), input.publishYear(), id) == 0)
            throw new ApiException(NOT_FOUND, "Book not found");
        return book(id);
    }

    void deleteBook(String id) {
        book(id);
        changed(jdbc.update("DELETE FROM books WHERE id = ? AND is_available = 1 AND borrow_requested = 0", id),
                "A requested or borrowed book cannot be deleted");
    }

    Book request(String id, String studentId, int days) {
        book(id);
        changed(jdbc.update("UPDATE books SET is_available=0, borrow_requested=1, requested_by_student_id=?, " +
                        "request_date=?, borrow_days=?, is_returned=0, is_received=0, received_date=NULL " +
                        "WHERE id=? AND is_available=1 AND borrow_requested=0 " +
                        "AND EXISTS(SELECT 1 FROM students WHERE student_id=? AND active=1)",
                studentId, LocalDate.now().toString(), days, id, studentId), "Book is unavailable or account is disabled");
        return book(id);
    }

    Book approve(String id) {
        Book book = book(id);
        if (!book.isRequested() || book.getBorrowDays() <= 0)
            throw new ApiException(CONFLICT, "No valid pending request for this book");
        // The same Book domain transition is used by the console application.
        book.approveBorrowRequest();
        changed(jdbc.update("UPDATE books SET borrow_requested=0, borrowed_by_student_id=requested_by_student_id, " +
                        "requested_by_student_id=NULL, borrow_start_date=?, borrow_end_date=?, is_available=0, " +
                        "is_received=0, received_date=NULL, is_returned=0 WHERE id=? AND borrow_requested=1 " +
                        "AND requested_by_student_id=? AND borrow_days=?",
                book.getBorrowStartDate().toString(), book.getBorrowEndDate().toString(), id,
                book.getBorrowedByStudentId(), book.getBorrowDays()), "Request already handled");
        return book(id);
    }

    Book receive(String id) {
        book(id);
        changed(jdbc.update("UPDATE books SET is_received=1, received_date=? WHERE id=? AND is_available=0 " +
                        "AND borrow_requested=0 AND borrowed_by_student_id IS NOT NULL AND is_received=0",
                LocalDate.now().toString(), id), "Only an approved loan waiting for collection can be received");
        return book(id);
    }

    Book returnBook(String id, Account user) {
        Book book = book(id);
        if (user.role() == Role.STUDENT && !user.studentId().equals(book.getBorrowedByStudentId()))
            throw new ApiException(FORBIDDEN, "You can only return your own loan");
        changed(jdbc.update("UPDATE books SET is_available=1, borrow_requested=0, requested_by_student_id=NULL, " +
                        "request_date=NULL, borrow_days=0, borrowed_by_student_id=NULL, borrow_start_date=NULL, " +
                        "borrow_end_date=NULL, received_date=NULL, is_received=0, is_returned=1 " +
                        "WHERE id=? AND is_available=0 AND borrow_requested=0 AND borrowed_by_student_id IS NOT NULL " +
                        "AND (? IS NULL OR borrowed_by_student_id=?)",
                id, user.role() == Role.STUDENT ? user.studentId() : null, user.studentId()), "No active loan to return");
        return book(id);
    }

    List<LoanView> loans(Account user) {
        String id = user.role() == Role.STUDENT ? user.studentId() : null;
        return jdbc.query("SELECT * FROM books WHERE is_available=0 AND " +
                        "(? IS NULL OR borrowed_by_student_id=? OR requested_by_student_id=?) ORDER BY id",
                this::mapBook, id, id, id).stream().map(LoanView::of).toList();
    }

    Map<String, Object> statistics() {
        return jdbc.queryForMap("SELECT (SELECT count(*) FROM students) AS students, " +
                "(SELECT count(*) FROM books) AS books, (SELECT count(*) FROM librarians) AS librarians, " +
                "(SELECT count(*) FROM books WHERE is_available=0 AND borrow_requested=0) AS activeLoans, " +
                "(SELECT count(*) FROM books WHERE borrow_requested=1) AS pendingRequests");
    }

    private void changed(int rows, String message) {
        if (rows == 0) throw new ApiException(CONFLICT, message);
    }

    private Book mapBook(ResultSet rs, int row) throws SQLException {
        Book book = new Book(rs.getString("title"), rs.getString("id"), rs.getString("author"), rs.getInt("publish_year"));
        book.restoreState(rs.getInt("is_available") == 1, date(rs, "borrow_start_date"), date(rs, "borrow_end_date"),
                date(rs, "request_date"), rs.getString("borrowed_by_student_id"), rs.getInt("borrow_requested") == 1,
                rs.getString("requested_by_student_id"), rs.getInt("borrow_days"), date(rs, "received_date"),
                rs.getInt("is_received") == 1, rs.getInt("is_returned") == 1);
        return book;
    }

    private LocalDate date(ResultSet rs, String name) throws SQLException {
        String value = rs.getString(name);
        return value == null ? null : LocalDate.parse(value);
    }
}
