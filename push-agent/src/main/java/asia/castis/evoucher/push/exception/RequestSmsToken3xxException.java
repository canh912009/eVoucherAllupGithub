package asia.castis.evoucher.push.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestSmsToken3xxException extends RuntimeException{
    public RequestSmsToken3xxException(String errorBody) {
      super(errorBody);
      log.error("errorBody:{}", errorBody);
    }
    public RequestSmsToken3xxException(String errorBody, String statusCode) {
        super(errorBody);
        log.error("statusCode: {}, errorBody:{}", statusCode, errorBody);
    }
}
