package studio.example.reservation.error;

public class NoSuchLectureByIdException extends RuntimeException{
    public NoSuchLectureByIdException(String message) {
        super(message);
    }

    public NoSuchLectureByIdException(String message, Throwable cause) {
        super(message, cause);
    }
}
