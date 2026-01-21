package asia.castis.otpservice.service;

import asia.castis.otpservice.dto.CustomOTPRequestDTO;
import asia.castis.otpservice.dto.CustomOtpDTO;
import asia.castis.otpservice.exception.*;

public interface OtpService {
    String generateOtpAndSetAsHashKey(String uuid) throws RedisConnectorException, OtpGenerationException;

    String useOTP(String otpCode) throws RedisConnectorException, InvalidOtpException, NotFoundException;

    void restoreOTP(String otpCode) throws InvalidOtpException, RedisConnectorException, NotFoundException;

    CustomOtpDTO getDataByKey(String key) throws RedisConnectorException, InvalidOtpException, NotFoundException;

    CustomOtpDTO getKeyWithoutValidation(String otpCode) throws RedisConnectorException, InvalidOtpException, NotFoundException;

    String getUUID(String otpCode) throws RedisConnectorException, InvalidOtpException, NotFoundException;

    String generateOTPCustom(CustomOTPRequestDTO requestDTO) throws InvalidRequestException, OtpGenerationException, RedisConnectorException;
}
