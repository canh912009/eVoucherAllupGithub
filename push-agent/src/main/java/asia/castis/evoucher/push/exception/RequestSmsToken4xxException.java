package asia.castis.evoucher.push.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestSmsToken4xxException extends RuntimeException{
    public RequestSmsToken4xxException(String errorBody) {
      super(errorBody);
      log.error("errorBody:{}", errorBody);
    }
    public RequestSmsToken4xxException(String errorBody, String statusCode) {
        super(errorBody);
        log.error("statusCode: {}, errorBody:{}", statusCode, errorBody);
    }
}
