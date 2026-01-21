package asia.castis.otpservice.exception;

public class RedisConnectorException extends Exception {
    public RedisConnectorException(String message, Throwable e) {
        super(message, e);
    }
}
