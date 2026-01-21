package asia.castis.evoucher.push.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestSmsToken5xxException extends RuntimeException{
    public RequestSmsToken5xxException(String errorBody) {
      super(errorBody);
      log.error("errorBody:{}", errorBody);
    }
    public RequestSmsToken5xxException(String errorBody, String statusCode) {
        super(errorBody);
        log.error("statusCode: {}, errorBody:{}", statusCode, errorBody);
    }
}
