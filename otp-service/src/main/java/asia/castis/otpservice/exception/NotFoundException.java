package asia.castis.otpservice.exception;

public class NotFoundException extends Exception {
    public NotFoundException(String message, Throwable e) {
        super(message, e);
    }
    public NotFoundException(String message) {
        super(message);
    }
}
