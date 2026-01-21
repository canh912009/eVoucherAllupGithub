package asia.castis.web_hook.exception;

public class InvalidCredentialException extends SecurityException {
    public InvalidCredentialException(String msg) {
        super(msg);
    }
}