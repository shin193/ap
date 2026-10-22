package Final_project.web;

import static org.junit.jupiter.api.Assertions.*;

import Final_project.Passwords;
import Final_project.StudentManager;
import com.fasterxml.jackson.databind.JsonNode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LibraryApiTest {
    private static final Path TEMP = temporaryDirectory();
    @Autowired TestRestTemplate client;
    @Autowired JdbcTemplate jdbc;

    private static Path temporaryDirectory() {
        try { return Files.createTempDirectory("library-api-test-"); }
        catch (Exception error) { throw new IllegalStateException(error); }
    }

    @DynamicPropertySource
    static void database(DynamicPropertyRegistry properties) {
        properties.add("library.database.path", () -> TEMP.resolve("library.db").toString());
    }

    @AfterAll
    static void cleanup() throws Exception {
        Files.deleteIfExists(TEMP.resolve("library.db"));
        Files.deleteIfExists(TEMP);
    }

    @BeforeEach
    void reset() {
        jdbc.update("DELETE FROM books");
        jdbc.update("DELETE FROM students");
        jdbc.update("DELETE FROM librarians WHERE username <> 'abc'");
    }

    private ResponseEntity<JsonNode> call(HttpMethod method, String path, Object body, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (token != null) headers.setBearerAuth(token);
        return client.exchange(path, method, new HttpEntity<>(body, headers), JsonNode.class);
    }

    private JsonNode expect(int status, HttpMethod method, String path, Object body, String token) {
        ResponseEntity<JsonNode> response = call(method, path, body, token);
        assertEquals(status, response.getStatusCode().value(), () -> String.valueOf(response.getBody()));
        return response.getBody();
    }

    private String login(String role, String username, String password) {
        return expect(200, HttpMethod.POST, "/api/auth/login",
                Map.of("role", role, "username", username, "password", password), null).get("token").asText();
    }

    private String student(String id) {
        JsonNode response = expect(201, HttpMethod.POST, "/api/students",
                Map.of("studentId", id, "name", "دانشجو " + id, "username", id, "password", "secret123"), null);
        assertFalse(response.has("password"));
        return login("STUDENT", id, "secret123");
    }

    private String librarian() { return login("LIBRARIAN", "abc", "123456"); }

    private Map<String, Object> book(String id) {
        return Map.of("id", id, "title", "کتاب O'Brien", "author", "Author", "publishYear", 2024);
    }

    @Test
    void publicCatalogAndStaffCrud() {
        expect(200, HttpMethod.GET, "/api/health", null, null);
        expect(401, HttpMethod.POST, "/api/books", book("b1"), null);
        String token = librarian();
        JsonNode created = expect(201, HttpMethod.POST, "/api/books", book("b1"), token);
        assertTrue(created.get("available").asBoolean());
        assertFalse(created.has("borrowedByStudentId"));
        assertEquals(1, expect(200, HttpMethod.GET, "/api/books?author=Author&year=2024&available=true", null, null).size());
        assertEquals(0, expect(200, HttpMethod.GET, "/api/books?title=missing", null, null).size());
        expect(409, HttpMethod.POST, "/api/books", book("b1"), token);
        expect(200, HttpMethod.PUT, "/api/books/b1",
                Map.of("title", "Updated", "author", "New Author", "publishYear", 2025), token);
        expect(204, HttpMethod.DELETE, "/api/books/b1", null, token);
        expect(404, HttpMethod.GET, "/api/books/b1", null, null);
    }

    @Test
    void registrationStoresHashAndConsoleCanAuthenticate() {
        String token = student("s1");
        String stored = jdbc.queryForObject("SELECT password FROM students WHERE student_id='s1'", String.class);
        assertNotEquals("secret123", stored);
        assertTrue(Passwords.matches("secret123", stored));
        assertFalse(Passwords.matches("wrong", stored));
        assertNotNull(new StudentManager(new Scanner("")).authenticateStudent("s1", "secret123"));
        JsonNode me = expect(200, HttpMethod.GET, "/api/auth/me", null, token);
        assertEquals("s1", me.get("studentId").asText());
        assertFalse(me.has("password"));
        expect(409, HttpMethod.POST, "/api/students",
                Map.of("studentId", "s1", "name", "Duplicate", "username", "other", "password", "secret123"), null);
    }

    @Test
    void legacyPasswordsUpgradeOnLoginWithoutLosingConsoleAccess() {
        jdbc.update("INSERT INTO students(student_id,name,username,password,active) VALUES('old','Old','old','legacy',1)");
        login("STUDENT", "old", "legacy");
        String stored = jdbc.queryForObject("SELECT password FROM students WHERE student_id='old'", String.class);
        assertTrue(Passwords.isHashed(stored));
        assertNotNull(new StudentManager(new Scanner("")).authenticateStudent("old", "legacy"));
    }

    @Test
    void malformedAndInvalidInputDoNotWriteData() {
        expect(400, HttpMethod.POST, "/api/students", Map.of("username", "x"), null);
        expect(400, HttpMethod.POST, "/api/students", "{broken", null);
        expect(400, HttpMethod.POST, "/api/auth/login",
                Map.of("role", "ROOT", "username", "x", "password", "x"), null);
        String token = librarian();
        expect(400, HttpMethod.POST, "/api/books",
                Map.of("id", "b1", "title", "", "author", "Author", "publishYear", -1), token);
        expect(400, HttpMethod.GET, "/api/books?year=bad", null, null);
        expect(400, HttpMethod.POST, "/api/books",
                Map.of("id", "b1", "title", "Book", "author", "Author", "publishYear", 2024.5), token);
        assertEquals(0, jdbc.queryForObject("SELECT count(*) FROM books", Integer.class));
    }

    @Test
    void loginLogoutAndRolePermissions() {
        expect(401, HttpMethod.POST, "/api/auth/login",
                Map.of("role", "MANAGER", "username", "ADMIN", "password", "wrong"), null);
        String token = student("s1");
        expect(403, HttpMethod.POST, "/api/books", book("b1"), token);
        expect(403, HttpMethod.GET, "/api/students", null, token);
        expect(401, HttpMethod.GET, "/api/auth/me", null, "invalid-token");
        expect(204, HttpMethod.POST, "/api/auth/logout", null, token);
        expect(401, HttpMethod.GET, "/api/auth/me", null, token);
        expect(403, HttpMethod.POST, "/api/librarians",
                Map.of("username", "newlib", "name", "Librarian", "password", "secret123"), librarian());
        String manager = login("MANAGER", "ADMIN", "1111");
        expect(201, HttpMethod.POST, "/api/librarians",
                Map.of("username", "newlib", "name", "Librarian", "password", "secret123"), manager);
        login("LIBRARIAN", "newlib", "secret123");
    }

    @Test
    void completeLoanLifecycleAndOwnership() {
        String owner = student("s1");
        String other = student("s2");
        String staff = librarian();
        expect(201, HttpMethod.POST, "/api/books", book("b1"), staff);
        expect(400, HttpMethod.POST, "/api/books/b1/borrow-requests", Map.of("days", 0), owner);
        expect(201, HttpMethod.POST, "/api/books/b1/borrow-requests", Map.of("days", 7), owner);
        expect(409, HttpMethod.POST, "/api/books/b1/borrow-requests", Map.of("days", 7), other);
        expect(409, HttpMethod.DELETE, "/api/books/b1", null, staff);
        expect(403, HttpMethod.POST, "/api/books/b1/approve", null, owner);
        expect(409, HttpMethod.POST, "/api/books/b1/receive", null, staff);
        expect(200, HttpMethod.POST, "/api/books/b1/approve", null, staff);
        assertEquals("APPROVED", expect(200, HttpMethod.GET, "/api/loans", null, owner).get(0).get("status").asText());
        assertEquals(0, expect(200, HttpMethod.GET, "/api/loans", null, other).size());
        expect(200, HttpMethod.POST, "/api/books/b1/receive", null, staff);
        expect(403, HttpMethod.POST, "/api/books/b1/return", null, other);
        JsonNode returned = expect(200, HttpMethod.POST, "/api/books/b1/return", null, owner);
        assertTrue(returned.get("available").asBoolean());
        assertEquals(0, expect(200, HttpMethod.GET, "/api/loans", null, owner).size());
        assertNull(jdbc.queryForObject("SELECT borrowed_by_student_id FROM books WHERE id='b1'", String.class));
        assertEquals(1, expect(200, HttpMethod.GET, "/api/books?available=true", null, null).size());
    }

    @Test
    void disablingAccountInvalidatesExistingToken() {
        String student = student("s1");
        String staff = librarian();
        expect(204, HttpMethod.PATCH, "/api/students/s1/active", Map.of("active", false), staff);
        expect(401, HttpMethod.GET, "/api/auth/me", null, student);
        expect(403, HttpMethod.POST, "/api/auth/login",
                Map.of("role", "STUDENT", "username", "s1", "password", "secret123"), null);
    }

    @Test
    void concurrentRequestsHaveOnlyOneWinner() throws Exception {
        String first = student("s1");
        String second = student("s2");
        expect(201, HttpMethod.POST, "/api/books", book("race"), librarian());
        var executor = Executors.newFixedThreadPool(2);
        try {
            var responses = executor.invokeAll(List.of(
                    () -> call(HttpMethod.POST, "/api/books/race/borrow-requests", Map.of("days", 5), first).getStatusCode().value(),
                    () -> call(HttpMethod.POST, "/api/books/race/borrow-requests", Map.of("days", 5), second).getStatusCode().value()));
            assertEquals(Set.of(201, 409), Set.of(responses.get(0).get(), responses.get(1).get()));
        } finally { executor.shutdownNow(); }
        assertEquals(1, jdbc.queryForObject("SELECT count(*) FROM books WHERE borrow_requested=1", Integer.class));
    }
}
