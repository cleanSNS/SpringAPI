package cleanbook.com.exception.exceptions;

public class EmptyStringException extends RuntimeException{
    public EmptyStringException() {
        super("빈 문자열입니다.");
    }

    public EmptyStringException(String message) {
        super(message);
    }

    public EmptyStringException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyStringException(Throwable cause) {
        super(cause);
    }

    protected EmptyStringException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
