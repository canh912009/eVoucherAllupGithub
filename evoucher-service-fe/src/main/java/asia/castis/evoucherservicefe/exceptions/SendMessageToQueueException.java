package asia.castis.evoucherservicefe.exceptions;

public class SendMessageToQueueException extends Exception {
    public SendMessageToQueueException(String message) {
        super(message);
    }
    public SendMessageToQueueException(String message, Throwable exception) {
        super(message, exception);
    }
}
