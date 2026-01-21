package asia.castis.evoucherservicefe.exceptions;

public class DecryptException extends Exception {
    public DecryptException(String message) {
        super(message);
    }
    public DecryptException(String message, Throwable exception) {
        super(message, exception);
    }
}
