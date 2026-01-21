package com.castis.publishservice.exception;


import com.castis.publishservice.dto.response.BaseResponse;
import com.castis.publishservice.exception.defineException.BadRequestException;
import com.castis.publishservice.exception.defineException.CustomCodeException;
import com.castis.publishservice.exception.defineException.NotFoundException;
import com.castis.publishservice.exception.defineException.ServerRuntimeException;
import com.castis.publishservice.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.format.DateTimeParseException;


@RestControllerAdvice
@Slf4j
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> handleMissingReportIdException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new BaseResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({HttpClientErrorException.Unauthorized.class})
    public ResponseEntity<Object> handleUnauthorizedException(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new BaseResponse(HttpStatus.UNAUTHORIZED.value(), "Access denied"), new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    public ResponseEntity<Object> handleBadRequestException(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new BaseResponse(HttpStatus.BAD_REQUEST.value(), "Bad Request"), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public ResponseEntity<Object> handleNotFoundException(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new BaseResponse(HttpStatus.NOT_FOUND.value(), "Page Not Found"), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpClientErrorException.MethodNotAllowed.class)
    public ResponseEntity<Object> handleMethodNotAllowedException(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new BaseResponse(HttpStatus.METHOD_NOT_ALLOWED.value(), "Method Not Allowed"), new HttpHeaders(), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new BaseResponse(Constants.ERROR_CODE.INTERNAL_ERROR_CODE, ex.getMessage()), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnknownError.class)
    public ResponseEntity<Object> handleUnknownError(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new BaseResponse(HttpStatus.NO_CONTENT.value(), ex.getMessage()), new HttpHeaders(), HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new BaseResponse(HttpStatus.METHOD_NOT_ALLOWED.value(), "Request Method Not Supported"), new HttpHeaders(), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new BaseResponse(HttpStatus.BAD_REQUEST.value(), "Method Argument Not Valid"), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new BaseResponse(HttpStatus.NOT_FOUND.value(), "Not Found"), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Object> handleDuplicateKey(DuplicateKeyException ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new BaseResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ServerRuntimeException.class)
    public ResponseEntity<Object> handleServerError(ServerRuntimeException ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new BaseResponse(ex.getCode() == null ? Constants.ERROR_CODE.INTERNAL_ERROR_CODE : ex.getCode(), ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new BaseResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<Object> handleDateTimeParseException(DateTimeParseException ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new BaseResponse(Constants.ERROR_CODE.INTERNAL_ERROR_CODE, ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InterruptedException.class)
    public ResponseEntity<Object> handleInterruptedException(InterruptedException ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new BaseResponse(Constants.ERROR_CODE.INTERNAL_ERROR_CODE, ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CustomCodeException.class)
    public ResponseEntity<Object> handleCustomCodeException(CustomCodeException ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(
                GenericError.builder()
                .status(ex.getStatus().value())
                    .code(ex.getErrorCode())
                    .message(ex.getMessage())
                        .build()
                    ,
            ex.getStatus());
    }
}
