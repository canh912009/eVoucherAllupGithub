package asia.castis.otpservice.controller;

import asia.castis.otpservice.dto.*;
import asia.castis.otpservice.exception.*;
import asia.castis.otpservice.service.OtpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/otp")
@RequiredArgsConstructor
public class OtpController {

    private final OtpService otpService;

    @Operation(description = "Request to generate OTP from UUID")
    @PostMapping("/generateOtpTTL")
    @ApiResponse(
            responseCode = "200",
            content = {@Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseObject.class),
                    examples = @ExampleObject(value = "{\n" +
                            "    \"errorType\": \"\",\n" +
                            "    \"error\": \"\",\n" +
                            "    \"path\": \"/otp/generateOtpTTL\",\n" +
                            "    \"data\": {\n" +
                            "        \"otp\": \"12345678\"\n" +
                            "    }\n" +
                            "}")
            )
            })
    public ResponseEntity<ResponseObject<OtpDTO>> generateOtpCustom(
            @RequestBody
            CustomOTPRequestDTO requestObj,
            HttpServletRequest request)
            throws RedisConnectorException, OtpGenerationException, InvalidRequestException {
        String otp = otpService.generateOTPCustom(requestObj);
        return new ResponseEntity<>(new ResponseBuilder<OtpDTO>().success(new OtpDTO(otp), request), HttpStatus.OK);
    }

    @Operation(description = "Request to generate OTP from UUID")
    @PostMapping("/generate/{uuid}")
    @ApiResponse(
            responseCode = "200",
            content = {@Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseObject.class),
                    examples = @ExampleObject(value = "{\n" +
                            "    \"errorType\": \"\",\n" +
                            "    \"error\": \"\",\n" +
                            "    \"path\": \"/otp/generate/c33e8e34-52bd-4a1e-9b1d-9c15bfe63b7a\",\n" +
                            "    \"data\": {\n" +
                            "        \"otp\": \"12345678\"\n" +
                            "    }\n" +
                            "}")
            )
            })
    public ResponseEntity<ResponseObject<OtpDTO>> generateOtp(
            @Parameter(description = "UUID",
                    required = true,
                    example = "c33e8e34-52bd-4a1e-9b1d-9c15bfe63b7a")
            @PathVariable
            String uuid,
            HttpServletRequest request)
            throws RedisConnectorException, OtpGenerationException {
        String otp = otpService.generateOtpAndSetAsHashKey(uuid);
        return new ResponseEntity<>(new ResponseBuilder<OtpDTO>().success(new OtpDTO(otp), request), HttpStatus.OK);
    }

    /***
     * Use OTP
     * @param otpCode
     * @param request
     * @return UUID
     * @throws RedisConnectorException
     * @throws InvalidOtpException
     * @throws NotFoundException
     */
    @Operation(description = "Use OTP, OTP will be set to USED after this action")
    @GetMapping("/use/{otpCode}")
    @ApiResponse(
            responseCode = "200",
            content = {@Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseObject.class),
                    examples = @ExampleObject(value = "{\n" +
                            "    \"errorType\": \"\",\n" +
                            "    \"error\": \"\",\n" +
                            "    \"path\": \"/otp/use/03985734\",\n" +
                            "    \"data\": {\n" +
                            "        \"uuid\": \"03985734\"\n" +
                            "    }\n" +
                            "}")
            )
            })
    public ResponseEntity<ResponseObject<UuidDTO>> useOTP(
            @PathVariable
            @Parameter(description = "OTP",
                    required = true,
                    example = "03985734")
            String otpCode,
            HttpServletRequest request)
            throws RedisConnectorException, InvalidOtpException, NotFoundException {
        String uuid = otpService.useOTP(otpCode);
        return new ResponseEntity<>(new ResponseBuilder<UuidDTO>().success(new UuidDTO(uuid), request), HttpStatus.OK);
    }

    /***
     * Use OTP
     * @param otpCode
     * @param request
     * @return UUID
     * @throws RedisConnectorException
     * @throws InvalidOtpException
     * @throws NotFoundException
     */
    @Operation(description = "Get UUID from OTP, OTP still available after this action")
    @GetMapping("/get/{otpCode}")
    @ApiResponse(
            responseCode = "200",
            content = {@Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseObject.class),
                    examples = @ExampleObject(value = "{\n" +
                            "    \"errorType\": \"\",\n" +
                            "    \"error\": \"\",\n" +
                            "    \"path\": \"/otp/use/03985734\",\n" +
                            "    \"data\": {\n" +
                            "        \"uuid\": \"c33e8e34-52bd-4a1e-9b1d-9c15bfe63b7a\"\n" +
                            "    }\n" +
                            "}")
            )
            })
    public ResponseEntity<ResponseObject<UuidDTO>> getUUID(
            @PathVariable
            @Parameter(description = "OTP",
                    required = true,
                    example = "03985734")
            String otpCode,
            HttpServletRequest request)
            throws RedisConnectorException, InvalidOtpException, NotFoundException {
        String uuid = otpService.getUUID(otpCode);
        return new ResponseEntity<>(new ResponseBuilder<UuidDTO>().success(new UuidDTO(uuid), request), HttpStatus.OK);
    }

    @Operation(description = "Get Key from OTP, no status affect")
    @GetMapping("/key/{otpCode}")
    @ApiResponse(
            responseCode = "200",
            content = {@Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseObject.class),
                    examples = @ExampleObject(value = "{\n" +
                            "    \"errorType\": \"\",\n" +
                            "    \"error\": \"\",\n" +
                            "    \"path\": \"/otp/key/03985734\",\n" +
                            "    \"data\": {\n" +
                            "        \"key\": \"c33e8e34-52bd-4a1e-9b1d-9c15bfe63b7a\"\n" +
                            "    }\n" +
                            "}")
            )
            })
    public ResponseEntity<ResponseObject<CustomOtpDTO>> getKey(
            @PathVariable
            @Parameter(description = "OTP",
                    required = true,
                    example = "03985734")
            String otpCode,
            HttpServletRequest request)
            throws RedisConnectorException, InvalidOtpException, NotFoundException {
        CustomOtpDTO key = otpService.getKeyWithoutValidation(otpCode);
        return new ResponseEntity<>(new ResponseBuilder<CustomOtpDTO>().success(key, request), HttpStatus.OK);
    }

    @Operation(description = "Restore USED OTP, OTP status will be set to NORMAL again")
    @PutMapping("/restore/{otpCode}")
    @ApiResponse(
            responseCode = "200",
            content = {@Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResponseObject.class),
                    examples = @ExampleObject(value = "{\n" +
                            "    \"errorType\": \"\",\n" +
                            "    \"error\": \"\",\n" +
                            "    \"path\": \"/otp/restore/03985734\",\n" +
                            "    \"data\": {\n" +
                            "        \"otp\": \"03985734\"\n" +
                            "    }\n" +
                            "}")
            )
            })
    public ResponseEntity<ResponseObject<UuidDTO>> restoreOTP(
            @PathVariable
            @Parameter(description = "OTP",
                    required = true,
                    example = "03985734")
            String otpCode,
            HttpServletRequest request)
            throws RedisConnectorException, InvalidOtpException, NotFoundException {
        otpService.restoreOTP(otpCode);
        return new ResponseEntity<>(
                new ResponseBuilder<UuidDTO>().success(new UuidDTO(otpCode), request), HttpStatus.OK);
    }

    @Operation(description = "Get Key from OTP, no status affect")
    @GetMapping("/get-otp")
    public ResponseEntity<ResponseObject<CustomOtpDTO>> getOtpByKey(
            @RequestParam
            @Parameter(description = "key",
                    required = true,
                    example = "03985734")
            String key,
            HttpServletRequest request)
            throws RedisConnectorException, InvalidOtpException, NotFoundException {
        CustomOtpDTO otp = otpService.getDataByKey(key);
        return new ResponseEntity<>(new ResponseBuilder<CustomOtpDTO>().success(otp, request), HttpStatus.OK);
    }
}
