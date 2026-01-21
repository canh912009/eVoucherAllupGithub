package asia.castis.otpservice.exception;

public class OtpGenerationException extends Exception {
    public OtpGenerationException(String message, Throwable e) {
        super(message, e);
    }
}
