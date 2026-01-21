package asia.castis.otpservice.exception;

public class InvalidOtpException extends Exception {
    public InvalidOtpException(String message, Throwable e) {
        super(message, e);
    }
    public InvalidOtpException(String message) {
        super(message);
    }
}
