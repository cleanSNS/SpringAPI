package cleanbook.com.exception;

import cleanbook.com.exception.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MyExceptionHandeler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response reportError(UserNotFoundException exception) {
        return new Response(exception.getMessage());
    }

    @ExceptionHandler(PageNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response reportError(PageNotFoundException exception) {
        return new Response(exception.getMessage());
    }

    @ExceptionHandler(CommentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response reportError(CommentNotFoundException exception) {
        return new Response(exception.getMessage());
    }

    @ExceptionHandler(NoAuthroizationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Response reportError(NoAuthroizationException exception) {
        return new Response(exception.getMessage());
    }

    @ExceptionHandler(UserDuplicateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response reportError(UserDuplicateException exception) {
        return new Response(exception.getMessage());
    }

    @ExceptionHandler(EmptyStringException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response reportError(EmptyStringException exception) {
        return new Response(exception.getMessage());
    }

    @ExceptionHandler(EmailAuthTokenNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response reportError(EmailAuthTokenNotFoundException exception) {
        return new Response(exception.getMessage());
    }

    @ExceptionHandler(EmailAuthFailException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response reportError(EmailAuthFailException exception) {
        return new Response(exception.getMessage());
    }

    @ExceptionHandler(IllegalTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response reportError(IllegalTokenException exception) {
        return new Response(exception.getMessage());
    }

    @ExceptionHandler(IllegalAccountException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response reportError(IllegalAccountException exception) {
        return new Response(exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response reportError() {
        return new Response("????????? ???????????????.");
    }
}
