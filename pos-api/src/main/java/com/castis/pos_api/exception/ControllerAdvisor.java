package com.castis.pos_api.exception;


import com.castis.pos_api.dto.response.ResponseData;
import com.castis.pos_api.utils.CustomResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;


@RestControllerAdvice
@Slf4j
public class ControllerAdvisor extends ResponseEntityExceptionHandler {
    @Value("${contact}")
    String contact;

    @NotNull
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage(), ex);
        log.error("errors fields: {}", ex.getBindingResult().getFieldErrors());
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::toString)
                .collect(Collectors.joining(", "));
        return new ResponseEntity<>(
                ResponseData.error(CustomResponse.E4101_INVALID_REQUEST.getCode(), errorMessage),
                new HttpHeaders(), HttpStatus.BAD_REQUEST);

    }

    @Override
    @NotNull
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        BindingResult result = ex.getBindingResult();

        log.error("Validation error fields: {}", result.getFieldErrors());
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::toString)
                .collect(Collectors.joining(", "));
        return new ResponseEntity<>(
                ResponseData.error(CustomResponse.E4101_INVALID_REQUEST.getCode(), errorMessage),
                new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<Object> handlerNotFoundException(ApplicationException ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(ResponseData.error(ex.getCode(), ex.getMessage()), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUnknownError(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(ResponseData.error(CustomResponse.E5001_INTERNAL_SERVER_ERROR.getCode(), ex.getMessage()), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
