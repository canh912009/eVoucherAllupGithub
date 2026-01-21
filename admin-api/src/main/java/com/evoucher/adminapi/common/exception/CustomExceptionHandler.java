package com.evoucher.adminapi.common.exception;

import com.evoucher.adminapi.common.message.ErrorMessage;
import com.evoucher.adminapi.common.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to handle Exceptions and return ResponseEntity
 *
 * @author BytesTree
 */
@ControllerAdvice
@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String GENERIC_ERROR = "Oops! There was an error.";

    @ResponseBody
    @ExceptionHandler(CustomCodeException.class)
    protected ResponseEntity<GenericError> customCodeException(CustomCodeException ex) {
        logValidationError(ex);
        return new ResponseEntity<>(
                GenericError.builder()
                        .timestamp(LocalDateTime.now(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern(Constant.Common.COMMON_DATETIME_FORMAT)))
                        .status(ex.getStatus().value())
                        .errorCode(GENERIC_ERROR)
                        .message(ex.getMessage())
                        .build(),
                ex.getStatus());
    }
    @ResponseBody
    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<GenericError> customCodeException(EntityNotFoundException ex) {
        logValidationError(ex);
        return new ResponseEntity<>(
                GenericError.builder()
                        .timestamp(LocalDateTime.now(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern(Constant.Common.COMMON_DATETIME_FORMAT)))
                        .status(HttpStatus.BAD_REQUEST.value())
                        .errorCode(GENERIC_ERROR)
                        .message(ex.getMessage())
                        .build(),
                HttpStatus.BAD_REQUEST);
    }
    @ResponseBody
    @ExceptionHandler(DatabaseException.class)
    protected ResponseEntity<GenericError> handleDataBaseException(DatabaseException ex) {
        logValidationError(ex);
        return new ResponseEntity<>(
                GenericError.builder()
                        .timestamp(LocalDateTime.now(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern(Constant.Common.COMMON_DATETIME_FORMAT)))
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .errorCode(GENERIC_ERROR)
                        .message(ex.getMessage())
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleCustomAPIException(Exception ex) {
        logValidationError(ex);
        return new ResponseEntity<>(
                GenericError.builder()
                        .timestamp(LocalDateTime.now(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern(Constant.Common.COMMON_DATETIME_FORMAT)))
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .errorCode(GENERIC_ERROR)
                        .message(ex.getMessage())
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @NotNull
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            @NotNull HttpHeaders headers,
            @NotNull HttpStatus status,
            @NotNull WebRequest request) {
        String error = "Malformed JSON request ";

        return new ResponseEntity<>(
                GenericError.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .errorCode(GENERIC_ERROR)
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
                GenericError.builder()
                        .timestamp(LocalDateTime.now(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern(Constant.Common.COMMON_DATETIME_FORMAT)))
                        .status(HttpStatus.BAD_REQUEST.value())
                        .errorCode(GENERIC_ERROR)
                        .listMessage(details)
                        .build(),
                HttpStatus.BAD_REQUEST);
    }

    private void logValidationError(Throwable t) {
        log.error(t.getMessage(), t);
    }
}