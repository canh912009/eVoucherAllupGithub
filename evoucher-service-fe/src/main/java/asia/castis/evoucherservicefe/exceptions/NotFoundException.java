package asia.castis.evoucherservicefe.exceptions;

public class NotFoundException extends ElasticSearchException {
    public NotFoundException(String message) {
        super(message);
    }
    public NotFoundException(String message, Throwable exception) {
        super(message, exception);
    }
}
