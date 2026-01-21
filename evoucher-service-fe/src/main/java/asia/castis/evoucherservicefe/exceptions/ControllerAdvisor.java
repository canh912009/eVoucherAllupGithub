package asia.castis.evoucherservicefe.exceptions;

import asia.castis.evoucherservicefe.common.dto.response.ResponseData;
import asia.castis.evoucherservicefe.common.utils.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ControllerAdvisor {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseData<Object> handleBadRequest(NotFoundException e) {
        return new ResponseData<>(ErrorCode.GENERAL_NOT_FOUND, e.getMessage());
    }
    @ExceptionHandler(ClientRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseData<Object> handleBadRequest(ClientRequestException e) {
        return new ResponseData<>(e.getCode(), e.getMessage());
    }
    @ExceptionHandler(ServerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseData<Object> handleServerException(ServerException e) {
        return new ResponseData<>(e.getCode(), e.getMessage());
    }
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseData<Object> handleException(Exception e) {
        return new ResponseData<>(ErrorCode.UNKNOWN_ERROR, e.getMessage());
    }
}
