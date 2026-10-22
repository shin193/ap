package Final_project.web;

import Final_project.Book;
import jakarta.validation.constraints.*;
import java.time.Instant;
import java.time.LocalDate;

public final class ApiModels {
    private ApiModels() {}

    public enum Role { STUDENT, LIBRARIAN, MANAGER }

    public record Registration(
            @NotBlank @Size(max = 120) String name,
            @NotBlank @Pattern(regexp = "[A-Za-z0-9_-]{1,50}") String studentId,
            @NotBlank @Pattern(regexp = "[A-Za-z0-9_.-]{1,50}") String username,
            @NotBlank @Size(min = 6, max = 128) String password) {}

    public record Login(@NotNull Role role, @NotBlank @Size(max = 50) String username,
                        @NotBlank @Size(max = 128) String password) {}

    public record LoginResult(String token, String tokenType, Instant expiresAt, Profile user) {}
    public record Profile(Role role, String username, String name, String studentId, boolean active) {}
    public record StudentView(String studentId, String name, String username, boolean active) {}
    public record ActiveUpdate(@NotNull Boolean active) {}
    public record LibrarianInput(@NotBlank @Pattern(regexp = "[A-Za-z0-9_.-]{1,50}") String username,
                                 @NotBlank @Size(min = 6, max = 128) String password,
                                 @NotBlank @Size(max = 120) String name) {}
    public record BookInput(@NotBlank @Pattern(regexp = "[A-Za-z0-9_-]{1,50}") String id,
                            @NotBlank @Size(max = 200) String title,
                            @NotBlank @Size(max = 120) String author,
                            @Min(1) @Max(9999) int publishYear) {}
    public record BookUpdate(@NotBlank @Size(max = 200) String title,
                             @NotBlank @Size(max = 120) String author,
                             @Min(1) @Max(9999) int publishYear) {}
    public record BorrowInput(@Min(1) @Max(365) int days) {}

    public record BookView(String id, String title, String author, int publishYear,
                           boolean available, boolean requested) {
        static BookView of(Book book) {
            return new BookView(book.getId(), book.getTitle(), book.getAuthor(),
                    book.getPublishYear(), book.isAvailable(), book.isRequested());
        }
    }

    public record LoanView(String bookId, String title, String studentId, String status,
                           LocalDate requestDate, int borrowDays, LocalDate startDate,
                           LocalDate dueDate, LocalDate receivedDate) {
        static LoanView of(Book book) {
            return new LoanView(book.getId(), book.getTitle(),
                    book.isRequested() ? book.getRequestedByStudentId() : book.getBorrowedByStudentId(),
                    book.isRequested() ? "REQUESTED" : book.isReceived() ? "RECEIVED" : "APPROVED",
                    book.getRequestDate(), book.getBorrowDays(), book.getBorrowStartDate(),
                    book.getBorrowEndDate(), book.getReceivedDate());
        }
    }
}
