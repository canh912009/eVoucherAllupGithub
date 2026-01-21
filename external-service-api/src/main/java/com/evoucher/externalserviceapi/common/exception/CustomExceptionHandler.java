package com.evoucher.externalserviceapi.common.exception;

import com.evoucher.externalserviceapi.common.message.BaseResponse;
import com.evoucher.externalserviceapi.common.message.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to handle Exceptions and return ResponseEntity
 *
 * @author BytesTree
 */
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(CustomExceptionHandler.class);
    private static final String GENERIC_ERROR = "Oops! There was an error.";

    @ResponseBody
    @ExceptionHandler(CustomCodeException.class)
    protected ResponseEntity<?> customCodeException(CustomCodeException ex) {
        logValidationError(ex);
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .result(String.valueOf(ex.getStatus().value()))
                        .message(ex.getMessage())
                        .build(),
                ex.getStatus());
    }

    @ResponseBody
    @ExceptionHandler(ClientNotLoggedInException.class)
    protected ResponseEntity<?> unauthorizedException(ClientNotLoggedInException ex) {
        logValidationError(ex);
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .result(String.valueOf(HttpStatus.UNAUTHORIZED.value()))
                        .message(ex.getMessage())
                        .build(),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleCustomAPIException(Exception ex) {
        logValidationError(ex);
        return new ResponseEntity<>(
                BaseResponse.builder()
                        .result(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                        .message(ex.getMessage())
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        String error = "Malformed JSON request ";

        return new ResponseEntity<>(
                BaseResponse.builder()
                        .result(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                        .message(error + ex.getMessage())
                        .build(),
                HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        return handleValidateFieldRequest(status, ex.getBindingResult());
    }

    @Override
    protected ResponseEntity<Object> handleBindException(
            BindException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        return handleValidateFieldRequest(status, ex.getBindingResult());
    }

    private ResponseEntity<Object> handleValidateFieldRequest(HttpStatus status, BindingResult ex) {
        List<ErrorMessage> details = new ArrayList<>();

        for (FieldError fieldError : ex.getFieldErrors()) {
            details.add(ErrorMessage.builder()
                    .fieldName(fieldError.getField())
                    .errorMessage(fieldError.getDefaultMessage())
                    .build());
        }

        return new ResponseEntity<>(
                BaseResponse.builder()
                        .result(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                        .data(details)
                        .build(),
                HttpStatus.BAD_REQUEST);
    }

    private void logValidationError(Throwable t) {
        LOGGER.error(t.getMessage(), t);
    }
}