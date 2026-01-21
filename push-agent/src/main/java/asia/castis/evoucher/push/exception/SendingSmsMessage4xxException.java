package asia.castis.evoucher.push.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SendingSmsMessage4xxException extends RuntimeException{
    String errorBody;
    public SendingSmsMessage4xxException(String errorBody) {
        super(errorBody);
        this.errorBody = errorBody;
        log.error("errorBody:{}", errorBody);
    }
    public SendingSmsMessage4xxException(String errorBody, String statusCode) {
        super(errorBody);
        this.errorBody = errorBody;
        log.error("statusCode: {}, errorBody:{}", statusCode, errorBody);
    }

    public String getErrorBody() {
        return errorBody;
    }
}
