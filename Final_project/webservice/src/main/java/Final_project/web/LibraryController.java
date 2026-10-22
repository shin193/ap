package Final_project.web;

import static Final_project.web.ApiModels.*;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LibraryController {
    private final LibraryRepository repository;
    private final Sessions sessions;
    public LibraryController(LibraryRepository repository, Sessions sessions) {
        this.repository = repository;
        this.sessions = sessions;
    }

    @GetMapping("/health")
    public Map<String, String> health() { return Map.of("status", "UP"); }

    @PostMapping("/auth/login")
    public LoginResult login(@Valid @RequestBody Login input) { return sessions.login(input); }

    @GetMapping("/auth/me")
    public Profile me(@AuthenticationPrincipal Account user) { return user.profile(); }

    @PostMapping("/auth/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authorization) {
        sessions.logout(authorization.substring(7));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/students")
    public ResponseEntity<StudentView> register(@Valid @RequestBody Registration input) {
        return ResponseEntity.created(URI.create("/api/auth/me")).body(repository.register(input));
    }

    @GetMapping("/students")
    @PreAuthorize("hasAnyRole('LIBRARIAN','MANAGER')")
    public List<StudentView> students() { return repository.students(); }

    @PatchMapping("/students/{id}/active")
    @PreAuthorize("hasAnyRole('LIBRARIAN','MANAGER')")
    public ResponseEntity<Void> active(@PathVariable String id, @Valid @RequestBody ActiveUpdate input) {
        repository.setActive(id, input.active());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/librarians")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Profile> addLibrarian(@Valid @RequestBody LibrarianInput input) {
        return ResponseEntity.status(201).body(repository.addLibrarian(input));
    }

    @GetMapping("/books")
    public List<BookView> books(@RequestParam(required = false) String title,
                                @RequestParam(required = false) String author,
                                @RequestParam(required = false) Integer year,
                                @RequestParam(required = false) Boolean available) {
        return repository.books(title, author, year, available).stream().map(BookView::of).toList();
    }

    @GetMapping("/books/{id}")
    public BookView book(@PathVariable String id) { return BookView.of(repository.book(id)); }

    @PostMapping("/books")
    @PreAuthorize("hasAnyRole('LIBRARIAN','MANAGER')")
    public ResponseEntity<BookView> addBook(@Valid @RequestBody BookInput input) {
        return ResponseEntity.created(URI.create("/api/books/" + input.id())).body(BookView.of(repository.addBook(input)));
    }

    @PutMapping("/books/{id}")
    @PreAuthorize("hasAnyRole('LIBRARIAN','MANAGER')")
    public BookView updateBook(@PathVariable String id, @Valid @RequestBody BookUpdate input) {
        return BookView.of(repository.updateBook(id, input));
    }

    @DeleteMapping("/books/{id}")
    @PreAuthorize("hasAnyRole('LIBRARIAN','MANAGER')")
    public ResponseEntity<Void> deleteBook(@PathVariable String id) {
        repository.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/books/{id}/borrow-requests")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<BookView> request(@PathVariable String id, @AuthenticationPrincipal Account user,
                                           @Valid @RequestBody BorrowInput input) {
        return ResponseEntity.status(201).body(BookView.of(repository.request(id, user.studentId(), input.days())));
    }

    @PostMapping("/books/{id}/approve")
    @PreAuthorize("hasAnyRole('LIBRARIAN','MANAGER')")
    public BookView approve(@PathVariable String id) { return BookView.of(repository.approve(id)); }

    @PostMapping("/books/{id}/receive")
    @PreAuthorize("hasAnyRole('LIBRARIAN','MANAGER')")
    public BookView receive(@PathVariable String id) { return BookView.of(repository.receive(id)); }

    @PostMapping("/books/{id}/return")
    public BookView returnBook(@PathVariable String id, @AuthenticationPrincipal Account user) {
        return BookView.of(repository.returnBook(id, user));
    }

    @GetMapping("/loans")
    public List<LoanView> loans(@AuthenticationPrincipal Account user) { return repository.loans(user); }

    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('LIBRARIAN','MANAGER')")
    public Map<String, Object> statistics() { return repository.statistics(); }
}
