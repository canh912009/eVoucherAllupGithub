package asia.castis.otpservice.advise;

import asia.castis.otpservice.dto.ResponseBuilder;
import asia.castis.otpservice.dto.ResponseObject;
import asia.castis.otpservice.exception.*;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ControllerExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ApiResponse(
            responseCode = "404",
            content = {@Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseObject.class),
                    examples = @ExampleObject(value = "{\n" +
                            "    \"errorType\": \"asia.castis.otpservice.exception.NotFoundException\",\n" +
                            "    \"error\": \"Data not found by OTP=03985734\",\n" +
                            "    \"path\": \"/otp/use/03985734\",\n" +
                            "    \"data\": null" +
                            "}")
            )})
    public ResponseEntity<ResponseObject> notFoundException(NotFoundException ex, WebRequest request) {
        ResponseObject exceptionMsg = new ResponseBuilder<>().exception(ex, request);
        logger.error(exceptionMsg.toString(), ex);
        return new ResponseEntity<>(exceptionMsg, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {InvalidUUIDException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ApiResponse(
            responseCode = "400",
            content = {@Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseObject.class),
                    examples = @ExampleObject(value = "{\n" +
                            "    \"errorType\": \"asia.castis.otpservice.exception.InvalidOtpException\",\n" +
                            "    \"error\": \"Incorrect OTP code format, OTP=039857\",\n" +
                            "    \"path\": \"/otp/use/039857\",\n" +
                            "    \"data\": null" +
                            "}")
            )})
    public ResponseEntity<ResponseObject> invalidOtpException(InvalidUUIDException ex, WebRequest request) {
        ResponseObject exceptionMsg = new ResponseBuilder<>().exception(ex, request);
        logger.error(exceptionMsg.toString(), ex);
        return new ResponseEntity<>(exceptionMsg, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = {InvalidRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ApiResponse(
            responseCode = "400",
            content = {@Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseObject.class),
                    examples = @ExampleObject(value = "{\n" +
                            "    \"errorType\": \"asia.castis.otpservice.exception.InvalidRequestException\",\n" +
                            "    \"error\": \"Time to live is invalid\",\n" +
                            "    \"path\": \"/generateOtpTTL\",\n" +
                            "    \"data\": null" +
                            "}")
            )})
    public ResponseEntity<ResponseObject> invalidRequest(InvalidRequestException ex, WebRequest request) {
        ResponseObject exceptionMsg = new ResponseBuilder<>().exception(ex, request);
        logger.error(exceptionMsg.toString(), ex);
        return new ResponseEntity<>(exceptionMsg, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = {InvalidOtpException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ApiResponse(
            responseCode = "400",
            content = {@Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseObject.class),
                    examples = @ExampleObject(value = "{\n" +
                            "    \"errorType\": \"asia.castis.otpservice.exception.InvalidOtpException\",\n" +
                            "    \"error\": \"Incorrect OTP code format, OTP=039857\",\n" +
                            "    \"path\": \"/otp/use/039857\",\n" +
                            "    \"data\": null" +
                            "}")
            )})
    public ResponseEntity<ResponseObject> invalidOtpException(InvalidOtpException ex, WebRequest request) {
        ResponseObject exceptionMsg = new ResponseBuilder<>().exception(ex, request);
        logger.error(exceptionMsg.toString(), ex);
        return new ResponseEntity<>(exceptionMsg, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {RedisConnectorException.class})
    @ApiResponse(
            responseCode = "500",
            content = {@Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseObject.class),
                    examples = @ExampleObject(value = "{\n" +
                            "    \"errorType\": \"asia.castis.otpservice.exception.RedisConnectorException\",\n" +
                            "    \"error\": \"Unable to connect to Redis; nested exception is io.lettuce.core.RedisConnectionException: Unable to connect to localhost:6379, OTP=03985734\",\n" +
                            "    \"path\": \"/otp/use/03985734\",\n" +
                            "    \"data\": null" +
                            "}")
            )})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ResponseObject> redisConnectorException(RedisConnectorException ex, WebRequest request) {
        ResponseObject exceptionMsg = new ResponseBuilder<>().exception(ex, request);
        logger.error(exceptionMsg.toString(), ex);
        return new ResponseEntity<>(exceptionMsg, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {OtpGenerationException.class})
    @ApiResponse(
            responseCode = "500",
            content = {@Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseObject.class),
                    examples = @ExampleObject(value = "{\n" +
                            "    \"errorType\": \"asia.castis.otpservice.exception.OtpGenerationException\",\n" +
                            "    \"error\": \"Can not build OTP Generator, OTP=03985734\",\n" +
                            "    \"path\": \"/otp/use/03985734\",\n" +
                            "    \"data\": null" +
                            "}")
            )})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ResponseObject> invalidOtpException(OtpGenerationException ex, WebRequest request) {
        ResponseObject exceptionMsg = new ResponseBuilder<>().exception(ex, request);
        logger.error(exceptionMsg.toString(), ex);
        return new ResponseEntity<>(exceptionMsg, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {Exception.class})
    @ApiResponse(
            responseCode = "500",
            content = {@Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseObject.class),
                    examples = @ExampleObject(value = "{\n" +
                            "    \"errorType\": \"asia.castis.otpservice.exception.Exception\",\n" +
                            "    \"error\": \"General exception, OTP=03985734\",\n" +
                            "    \"path\": \"/otp/use/03985734\",\n" +
                            "    \"data\": null" +
                            "}")
            )})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ResponseObject> redisConnectorException(Exception ex, WebRequest request) {
        ResponseObject exceptionMsg = new ResponseBuilder<>().exception(ex, request);
        logger.error(exceptionMsg.toString(), ex);
        return new ResponseEntity<>(exceptionMsg, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}