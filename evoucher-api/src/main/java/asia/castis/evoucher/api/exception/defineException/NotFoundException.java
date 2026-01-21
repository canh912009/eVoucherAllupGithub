package asia.castis.evoucher.api.exception.defineException;

public class NotFoundException extends Exception{
    public NotFoundException(String errorMessage) {
        super(errorMessage);
    }
}