package asia.castis.evoucher.api.exception.defineException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerRuntimeException extends RuntimeException{
    public ServerRuntimeException(String message) {
        super(message);
    }
}
