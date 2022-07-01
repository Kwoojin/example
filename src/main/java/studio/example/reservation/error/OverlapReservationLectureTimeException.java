package studio.example.reservation.error;

public class OverlapReservationLectureTimeException extends RuntimeException {
    public OverlapReservationLectureTimeException(String message) {
        super(message);
    }

    public OverlapReservationLectureTimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
