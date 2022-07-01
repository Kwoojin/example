package studio.example.reservation.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import studio.example.global.error.ErrorResult;

@Slf4j
@RestControllerAdvice("studio.example.reservation")
public class ReservationExceptionHandler {

    @ExceptionHandler({
            AlreadyMemberInLectureException.class,
            NoSuchLectureByIdException.class,
            NoSuchMemberByEmpNoException.class,
            OverTimeLectureException.class,
            IllegalArgumentException.class,
            OverlapReservationLectureTimeException.class
    })
    public ResponseEntity<ErrorResult> handleRuntimeException(RuntimeException e) {
        log.error("[{}]", e.getClass(),e);
        ErrorResult errorResult = new ErrorResult(e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }
}
