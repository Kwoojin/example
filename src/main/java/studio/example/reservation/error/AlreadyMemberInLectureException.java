package studio.example.reservation.error;

public class AlreadyMemberInLectureException extends RuntimeException {
    public AlreadyMemberInLectureException(String message) {
        super(message);
    }

    public AlreadyMemberInLectureException(String message, Throwable cause) {
        super(message, cause);
    }
}
