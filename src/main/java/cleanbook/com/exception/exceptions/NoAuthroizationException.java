package cleanbook.com.exception.exceptions;

public class NoAuthroizationException extends RuntimeException {
    public NoAuthroizationException() {
        super("권한이 없습니다.");
    }

    public NoAuthroizationException(String message) {
        super(message);
    }

    public NoAuthroizationException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoAuthroizationException(Throwable cause) {
        super(cause);
    }

    protected NoAuthroizationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
