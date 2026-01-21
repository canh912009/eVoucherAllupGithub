package asia.castis.evoucher.push.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestZaloToken5xxException extends RuntimeException{
    public RequestZaloToken5xxException(String errorBody) {
      super(errorBody);
      log.error("errorBody:{}", errorBody);
    }
    public RequestZaloToken5xxException(String errorBody, String statusCode) {
        super(errorBody);
        log.error("statusCode: {}, errorBody:{}", statusCode, errorBody);
    }
}
