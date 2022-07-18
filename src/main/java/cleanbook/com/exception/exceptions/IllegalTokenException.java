package cleanbook.com.exception.exceptions;

public class IllegalTokenException extends RuntimeException{
    public IllegalTokenException() {
        super("잘못된 토큰입니다.");
    }

    public IllegalTokenException(String message) {
        super(message);
    }

    public IllegalTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalTokenException(Throwable cause) {
        super(cause);
    }

    protected IllegalTokenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
