package asia.castis.evoucher.api.exception.defineException;

public class CompareException extends RuntimeException{
    public CompareException() {
        super("error when compare two custom object");
    }

    public CompareException(String message) {
        super(message);
    }
}
