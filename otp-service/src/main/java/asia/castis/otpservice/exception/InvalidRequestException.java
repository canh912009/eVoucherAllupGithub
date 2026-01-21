package asia.castis.otpservice.exception;

public class InvalidRequestException extends Exception {
    public InvalidRequestException(String message, Throwable e) {
        super(message, e);
    }
    public InvalidRequestException(String message) {
        super(message);
    }
}
