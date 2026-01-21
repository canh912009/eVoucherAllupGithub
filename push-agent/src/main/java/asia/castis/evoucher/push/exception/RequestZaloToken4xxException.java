package asia.castis.evoucher.push.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestZaloToken4xxException extends RuntimeException{
    public RequestZaloToken4xxException(String errorBody) {
      super(errorBody);
      log.error("errorBody:{}", errorBody);
    }
    public RequestZaloToken4xxException(String errorBody, String statusCode) {
        super(errorBody);
        log.error("statusCode: {}, errorBody:{}", statusCode, errorBody);
    }
}
