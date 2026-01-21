package asia.castis.evoucher.push.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SendingZaloMessageException extends RuntimeException{
    String errorBody;
    public SendingZaloMessageException(String errorBody) {
        super(errorBody);
        this.errorBody = errorBody;
        log.error("errorBody:{}", errorBody);
    }
    public SendingZaloMessageException(String errorBody, String statusCode) {
        super(errorBody);
        this.errorBody = errorBody;
        log.error("statusCode, {}, errorBody:{}", statusCode, errorBody);
    }

    public String getErrorBody() {
        return errorBody;
    }
}
