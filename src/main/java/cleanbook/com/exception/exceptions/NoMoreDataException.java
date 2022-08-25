package cleanbook.com.exception.exceptions;

public class NoMoreDataException extends RuntimeException{
    public NoMoreDataException() {
        super("더이상 ㅁㅁ이 없습니다.");
    }

    public NoMoreDataException(String target) {
        super("더이상 " + target + "이 없습니다.");
    }

    public NoMoreDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoMoreDataException(Throwable cause) {
        super(cause);
    }

    protected NoMoreDataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
