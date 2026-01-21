package asia.castis.evoucherservicefe.exceptions;

public class RetryJobException extends Exception {
    public RetryJobException(String message) {
        super(message);
    }
    public RetryJobException(String message, Throwable exception) {
        super(message, exception);
    }
}
