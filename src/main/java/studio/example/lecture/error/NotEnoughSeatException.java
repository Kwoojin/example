package studio.example.lecture.error;

public class NotEnoughSeatException extends RuntimeException {
    public NotEnoughSeatException(String message) {
        super(message);
    }

    public NotEnoughSeatException(String message, Throwable cause) {
        super(message, cause);
    }
}
