package asia.castis.web_hook.exception;

public class InvalidSignatureException extends SecurityException {
    public InvalidSignatureException(String msg) {
        super(msg);
    }
}