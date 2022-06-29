package studio.example.lecture.error;

public class StartTimeGoeEndTimeException extends RuntimeException {
    public StartTimeGoeEndTimeException(String message) {
        super(message);
    }

    public StartTimeGoeEndTimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
