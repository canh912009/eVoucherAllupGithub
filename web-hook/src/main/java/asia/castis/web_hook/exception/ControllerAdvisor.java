package asia.castis.web_hook.exception;


import asia.castis.web_hook.exception.defined.BadRequestException;
import asia.castis.web_hook.exception.defined.EntityNotFoundException;
import asia.castis.web_hook.exception.defined.ServerRuntimeException;
import lombok.extern.slf4j.Slf4j;
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

import java.rmi.ServerException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;


@RestControllerAdvice
@Slf4j
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> handleMissingReportIdException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({HttpClientErrorException.Unauthorized.class})
    public ResponseEntity<Object> handleUnauthorizedException(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Access denied"), new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    public ResponseEntity<Object> handleBadRequestException(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Bad Request"), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public ResponseEntity<Object> handleNotFoundException(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Page Not Found"), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpClientErrorException.MethodNotAllowed.class)
    public ResponseEntity<Object> handleMethodNotAllowedException(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.METHOD_NOT_ALLOWED.value(), "Method Not Allowed"), new HttpHeaders(), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(
                ex.getCode() == null ?
                        new ApiError(HttpStatus.NOT_FOUND, ex.getMessage(), ex)
                        : new ApiError(ex.getCode(), ex.getMessage(), ex)
                , HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<Object> handleServerException(ServerException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(
                new ApiError(HttpStatus.NOT_FOUND, e.getMessage(), e),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(
                new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex)
                , HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnknownError.class)
    public ResponseEntity<Object> handleUnknownError(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(
                new ApiError(HttpStatus.NO_CONTENT, ex.getMessage(), ex)
                , HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(
                new ApiError(HttpStatus.METHOD_NOT_ALLOWED, "Request Method Not Supported", ex)
                , HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(ex.getMessage(), ex);
        var error = new ApiError(HttpStatus.BAD_REQUEST, "Method Argument Not Valid", ex);
        error.setSubErrors(new ArrayList<>());
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(
                        e -> error.getSubErrors()
                                .add(new ApiValidationError(e.getField(), e.getDefaultMessage()))
                );
        return new ResponseEntity<>(
                error
                , HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Not Found"), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Object> handleDuplicateKey(DuplicateKeyException ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ServerRuntimeException.class)
    public ResponseEntity<Object> handleServerError(ServerRuntimeException ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(
                ex.getCode() == null
                        ? new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex)
                        : new ApiError(ex.getCode(), ex.getMessage(), ex)
                , HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(
                new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), ex)
                , HttpStatus.BAD_REQUEST
        );
    }
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<Object> handleDateTimeParseException(DateTimeParseException ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InterruptedException.class)
    public ResponseEntity<Object> handleInterruptedException(InterruptedException ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
