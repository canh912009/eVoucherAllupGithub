package asia.castis.web_hook.exception.defined;

public class BadRequestException extends RuntimeException{
    public BadRequestException(Exception ex) {
        super(ex.getMessage(), ex);
    }
    public BadRequestException(String message) {
        super(message);
    }
}
