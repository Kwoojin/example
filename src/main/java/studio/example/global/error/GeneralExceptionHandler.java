package studio.example.global.error;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler({
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(
                e.getBindingResult().getFieldErrors().stream()
                        .map(NotValidError::new)
                        .collect(Collectors.toList()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({
            InvalidFormatException.class
    })
    public ResponseEntity<?> handleInvalidFormatException(InvalidFormatException e) {
        return new ResponseEntity<>(
                new ErrorResult("Invalid input value"),
                HttpStatus.BAD_REQUEST
        );
    }

    public static class NotValidError {
        private final String message;
        private final String field;

        public String getMessage() {
            return message;
        }

        public String getField() {
            return field;
        }

        NotValidError(Throwable throwable, String field) {
            this(throwable.getMessage(), field);
        }

        NotValidError(String message, String field) {
            this.message = message;
            this.field = field;
        }

        public NotValidError(FieldError fieldError) {
            this(fieldError.getDefaultMessage(), fieldError.getField());
        }
    }
}
