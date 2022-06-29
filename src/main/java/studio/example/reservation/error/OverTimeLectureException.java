package studio.example.reservation.error;

public class OverTimeLectureException extends RuntimeException{
    public OverTimeLectureException(String message) {
        super(message);
    }

    public OverTimeLectureException(String message, Throwable cause) {
        super(message, cause);
    }
}
