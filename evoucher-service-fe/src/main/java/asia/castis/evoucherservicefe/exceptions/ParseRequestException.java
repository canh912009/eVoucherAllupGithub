package asia.castis.evoucherservicefe.exceptions;

public class ParseRequestException extends ElasticSearchException {
    public ParseRequestException(String message) {
        super(message);
    }
    public ParseRequestException(String message, Throwable exception) {
        super(message, exception);
    }
}
