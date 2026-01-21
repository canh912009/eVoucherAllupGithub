package asia.castis.evoucher.api.exception;

public class InvalidException extends Exception {
    public InvalidException(String errorMessage) {
        super(errorMessage);
    }

    public InvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}