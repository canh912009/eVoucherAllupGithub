package asia.castis.evoucherservicefe.exceptions;

public class InvalidException extends Exception {
    public InvalidException(String message) {
        super(message);
    }
    public InvalidException(String message, Throwable exception) {
        super(message, exception);
    }
}
