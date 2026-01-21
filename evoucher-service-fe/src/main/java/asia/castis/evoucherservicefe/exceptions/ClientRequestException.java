package asia.castis.evoucherservicefe.exceptions;

import lombok.Getter;

@Getter
public class ClientRequestException extends RuntimeException {
    int code;
    String message;

    public ClientRequestException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
