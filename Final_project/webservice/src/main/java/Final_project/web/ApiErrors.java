package Final_project.web;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class ApiErrors {
    private static final Logger LOG = LoggerFactory.getLogger(ApiErrors.class);

    @ExceptionHandler(NoResourceFoundException.class)
    ResponseEntity<?> missing(NoResourceFoundException error) {
        return error(404, "NOT_FOUND", "Endpoint not found");
    }

    @ExceptionHandler(ApiException.class)
    ResponseEntity<?> api(ApiException error) {
        return error(error.status.value(), error.status.name(), error.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<?> validation(MethodArgumentNotValidException error) {
        Map<String, String> fields = new LinkedHashMap<>();
        error.getBindingResult().getFieldErrors().forEach(field -> fields.put(field.getField(), field.getDefaultMessage()));
        return ResponseEntity.badRequest().body(Map.of("code", "INVALID_INPUT", "message", "Check the request fields", "fields", fields));
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    ResponseEntity<?> malformed(Exception error) {
        return error(400, "INVALID_INPUT", "The JSON body or query parameters are invalid");
    }

    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<?> denied(AccessDeniedException error) {
        return error(403, "FORBIDDEN", "This role cannot perform that action");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    ResponseEntity<?> method(HttpRequestMethodNotSupportedException error) {
        return error(405, "METHOD_NOT_ALLOWED", "HTTP method is not supported for this endpoint");
    }

    @ExceptionHandler(DataAccessException.class)
    ResponseEntity<?> database(DataAccessException error) {
        Throwable cause = error;
        while (cause != null) {
            if (cause instanceof SQLException sql) {
                int code = sql.getErrorCode() & 255;
                if (code == 19) return error(409, "CONFLICT", "An account or book with this identifier already exists");
                if (code == 5 || code == 6) return error(503, "STORAGE_BUSY", "Database is busy; retry shortly");
            }
            cause = cause.getCause();
        }
        LOG.error("Database request failed", error);
        return error(503, "STORAGE_UNAVAILABLE", "Database is unavailable");
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<?> unexpected(Exception error) {
        LOG.error("Unexpected API failure", error);
        return error(500, "INTERNAL_ERROR", "The request could not be completed");
    }

    private ResponseEntity<?> error(int status, String code, String message) {
        return ResponseEntity.status(status).body(Map.of("code", code, "message", message));
    }
}
