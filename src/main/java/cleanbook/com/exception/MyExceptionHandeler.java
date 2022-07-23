package cleanbook.com.exception;

import cleanbook.com.exception.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MyExceptionHandeler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse reportError(UserNotFoundException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(PageNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse reportError(PageNotFoundException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(CommentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse reportError(CommentNotFoundException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(NoAuthroizationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse reportError(NoAuthroizationException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(UserDuplicateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse reportError(UserDuplicateException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(EmptyStringException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse reportError(EmptyStringException exception) {
        return new ErrorResponse(exception.getMessage());
    }
}
