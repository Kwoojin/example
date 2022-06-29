package studio.example.lecture.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import studio.example.global.error.ErrorResult;

@Slf4j
@RestControllerAdvice("studio.example.lecture")
public class LectureExceptionHandler {

    @ExceptionHandler({
            StartTimeGoeEndTimeException.class,
            DuplicatePlaceAndTimeException.class
    })
    public ResponseEntity<ErrorResult> handleStartTimeGoeEndTimeException(RuntimeException e) {
        log.error("[handleStartTimeGoeEndTimeException]", e);
        ErrorResult errorResult = new ErrorResult(e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }
}
