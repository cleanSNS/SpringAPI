package cleanbook.com.exception.exceptions;

public class EmailAuthTokenNotFoundException extends RuntimeException{
    public EmailAuthTokenNotFoundException() {
        super("유효한 토큰이 아닙니다.");
    }

    public EmailAuthTokenNotFoundException(String message) {
        super(message);
    }

    public EmailAuthTokenNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailAuthTokenNotFoundException(Throwable cause) {
        super(cause);
    }

    protected EmailAuthTokenNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
