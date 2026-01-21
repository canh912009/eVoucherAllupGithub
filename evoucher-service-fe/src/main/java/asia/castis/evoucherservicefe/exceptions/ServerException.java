package asia.castis.evoucherservicefe.exceptions;

import lombok.Getter;

@Getter
public class ServerException extends RuntimeException {
    int code;
    String message;

    public ServerException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public ServerException(String message) {
        super(message);
        this.message = message;
    }
}
