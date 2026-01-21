package asia.castis.otpservice.exception;

public class InvalidUUIDException extends Exception {
    public InvalidUUIDException(String message, Throwable e) {
        super(message, e);
    }
    public InvalidUUIDException(String message) {
        super(message);
    }
}
