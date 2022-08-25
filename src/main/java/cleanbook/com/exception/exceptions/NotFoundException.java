package cleanbook.com.exception.exceptions;

public class NotFoundException extends RuntimeException{
    public NotFoundException() {
        super("존재하지 않는 ㅁㅁ입니다.");
    }

    public NotFoundException(String target) {
        super("존재하지 않는 " + target + "입니다.");
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }

    protected NotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
