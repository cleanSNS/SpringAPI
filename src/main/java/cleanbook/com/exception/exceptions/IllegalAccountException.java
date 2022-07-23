package cleanbook.com.exception.exceptions;

public class IllegalAccountException extends RuntimeException{
    public IllegalAccountException() {
        super("이메일 혹은 비밀번호가 틀립니다.");
    }

    public IllegalAccountException(String message) {
        super(message);
    }

    public IllegalAccountException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalAccountException(Throwable cause) {
        super(cause);
    }

    protected IllegalAccountException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
