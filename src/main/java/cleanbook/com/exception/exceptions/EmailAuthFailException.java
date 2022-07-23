package cleanbook.com.exception.exceptions;

public class EmailAuthFailException extends RuntimeException{
    public EmailAuthFailException() {
        super("이메일 인증이 완료되지 않은 계정입니다.");
    }

    public EmailAuthFailException(String message) {
        super(message);
    }

    public EmailAuthFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailAuthFailException(Throwable cause) {
        super(cause);
    }

    protected EmailAuthFailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
