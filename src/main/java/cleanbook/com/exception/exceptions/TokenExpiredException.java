package cleanbook.com.exception.exceptions;

public class TokenExpiredException extends RuntimeException{
    public TokenExpiredException() {
        super("refresh token이 만료되었습니다.");
    }

    public TokenExpiredException(String message) {
        super(message);
    }

    public TokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenExpiredException(Throwable cause) {
        super(cause);
    }

    protected TokenExpiredException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
