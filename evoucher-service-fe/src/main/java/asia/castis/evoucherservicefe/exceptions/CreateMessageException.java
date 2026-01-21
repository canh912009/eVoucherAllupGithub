package asia.castis.evoucherservicefe.exceptions;

public class CreateMessageException extends Exception {
    public CreateMessageException(String message) {
        super(message);
    }
    public CreateMessageException(String message, Throwable exception) {
        super(message, exception);
    }
}
