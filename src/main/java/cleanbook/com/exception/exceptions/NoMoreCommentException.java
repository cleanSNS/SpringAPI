package cleanbook.com.exception.exceptions;

public class NoMoreCommentException extends RuntimeException {
    public NoMoreCommentException() {
        super("더이상 댓글이 없습니다.");
    }

    public NoMoreCommentException(String message) {
        super(message);
    }

    public NoMoreCommentException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoMoreCommentException(Throwable cause) {
        super(cause);
    }

    protected NoMoreCommentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
