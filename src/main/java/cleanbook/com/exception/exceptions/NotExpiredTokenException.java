package cleanbook.com.exception.exceptions;

public class NotExpiredTokenException extends RuntimeException{
    public NotExpiredTokenException() {
        super("access token이 만료되지 않았습니다.");
    }

    public NotExpiredTokenException(String message) {
        super(message);
    }

    public NotExpiredTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotExpiredTokenException(Throwable cause) {
        super(cause);
    }

    protected NotExpiredTokenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
