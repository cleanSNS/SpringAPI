package cleanbook.com.exception.exceptions;

public class NoMorePageException extends RuntimeException {
    public NoMorePageException() {
        super("더이상 게시글이 없습니다.");
    }

    public NoMorePageException(String message) {
        super(message);
    }

    public NoMorePageException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoMorePageException(Throwable cause) {
        super(cause);
    }

    protected NoMorePageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
