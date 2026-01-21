package asia.castis.evoucherservicefe.exceptions;

public class ElasticSearchException extends Exception {
    public ElasticSearchException(String message) {
        super(message);
    }
    public ElasticSearchException(String message, Throwable exception) {
        super(message, exception);
    }
}
