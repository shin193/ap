package Final_project;

/** Stops the session after a storage failure so unsaved state cannot be used. */
public class StorageException extends RuntimeException {
    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
