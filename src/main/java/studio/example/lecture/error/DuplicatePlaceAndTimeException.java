package studio.example.lecture.error;

public class DuplicatePlaceAndTimeException extends RuntimeException {
    public DuplicatePlaceAndTimeException(String message) {
        super(message);
    }

    public DuplicatePlaceAndTimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
