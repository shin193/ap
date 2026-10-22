package Final_project.web;

import static Final_project.web.ApiModels.*;
import static org.springframework.http.HttpStatus.*;

import Final_project.Passwords;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Sessions {
    private record Session(Role role, String username, Instant expiresAt) {}
    private final Map<String, Session> sessions = new ConcurrentHashMap<>();
    private final SecureRandom random = new SecureRandom();
    private final LibraryRepository repository;
    private final long minutes;

    public Sessions(LibraryRepository repository, @Value("${library.session.minutes}") long minutes) {
        this.repository = repository;
        if (minutes < 1 || minutes > 1440) throw new IllegalArgumentException("Session lifetime must be 1..1440 minutes");
        this.minutes = minutes;
    }

    LoginResult login(Login input) {
        Account user = repository.account(input.role(), input.username())
                .filter(account -> Passwords.matches(input.password(), account.password()))
                .orElseThrow(() -> new ApiException(UNAUTHORIZED, "Invalid credentials"));
        if (!user.active()) throw new ApiException(FORBIDDEN, "Account is disabled");
        repository.upgradePassword(user, input.password());
        sessions.entrySet().removeIf(entry -> !entry.getValue().expiresAt().isAfter(Instant.now()));
        if (sessions.size() >= 10_000) throw new ApiException(SERVICE_UNAVAILABLE, "Session capacity reached");
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        Instant expiresAt = Instant.now().plusSeconds(minutes * 60);
        sessions.put(token, new Session(user.role(), user.username(), expiresAt));
        return new LoginResult(token, "Bearer", expiresAt, user.profile());
    }

    Optional<Account> authenticate(String token) {
        Session session = sessions.get(token);
        if (session == null) return Optional.empty();
        if (!session.expiresAt().isAfter(Instant.now())) {
            sessions.remove(token);
            return Optional.empty();
        }
        Optional<Account> user = repository.account(session.role(), session.username()).filter(Account::active);
        if (user.isEmpty()) sessions.remove(token);
        return user;
    }

    void logout(String token) { sessions.remove(token); }
}
