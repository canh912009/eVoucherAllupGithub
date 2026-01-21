package asia.castis.otpservice.service.impl;

import asia.castis.otpservice.common.Constants;
import asia.castis.otpservice.common.DateUtils;
import asia.castis.otpservice.common.EnumOtpStatus;
import asia.castis.otpservice.common.ValidateResult;
import asia.castis.otpservice.dto.CustomOTPRequestDTO;
import asia.castis.otpservice.dto.CustomOtpDTO;
import asia.castis.otpservice.exception.*;
import asia.castis.otpservice.service.OtpService;
import asia.castis.otpservice.service.RedisConnector;
import com.bastiaanjansen.otp.HMACAlgorithm;
import com.bastiaanjansen.otp.HOTPGenerator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {
    private static final Logger logger = LoggerFactory.getLogger(OtpServiceImpl.class);
    @Value("${otp.passwordLength:8}")
    private int defaultLength;
    @Value("${system.oneTimeUse:true}")
    private boolean isOneTimeUse;
    @Value("${otp.timeToLiveInSec}")
    private int defaultTTL;
    private final RedisConnector redisConnector;
    private static final Random rand = new Random();

    private HOTPGenerator buildHOptGenerator(String uuid, int length) {
        byte[] secret = uuid.getBytes();
        return new HOTPGenerator.Builder(secret)
                .withPasswordLength(length)
                .withAlgorithm(HMACAlgorithm.SHA256)
                .build();
    }

    @Override
    public String generateOtpAndSetAsHashKey(String uuid) throws RedisConnectorException, OtpGenerationException {

        int counter = rand.nextInt(Integer.MAX_VALUE);
        String otp = generateOTP(uuid, defaultLength, counter);
        Map<String, Object> mapData = makeOtpData(uuid, otp, counter);
        redisConnector.putData(otp, mapData, defaultTTL);

        return otp;
    }

    private static Map<String, Object> makeOtpData(String key, String otp, int counter) {
        Map<String, Object> mapData = new HashMap<>();
        mapData.put(Constants.REDIS_UUID, key);
        mapData.put(Constants.REDIS_KEY, key);
        mapData.put(Constants.REDIS_OTP, otp);
        mapData.put(Constants.REDIS_COUNTER, counter);
        mapData.put(Constants.REDIS_REG_DT, System.currentTimeMillis());
        mapData.put(Constants.REDIS_STATUS, EnumOtpStatus.NORMAL.getCode());
        return mapData;
    }

    /***
     * Get UUID based on OTP code
     * Before returning UUID, need to verify the OTP if it is valid
     * @param otpCode
     * @return
     * @throws RedisConnectorException
     * @throws InvalidOtpException
     */
    @Override
    public String useOTP(String otpCode) throws RedisConnectorException, InvalidOtpException, NotFoundException {
        String uuid = getUUID(otpCode);
        if (isOneTimeUse) {
            redisConnector.updateStatus(otpCode, EnumOtpStatus.USED.getCode());
        }
        return uuid;
    }

    /**
     * <pre>
     *     Key could be either OTP for INTERNAL CASE or Phone number for voucher list request
     * </pre>
     * @param key
     * @return
     * @throws RedisConnectorException
     * @throws NotFoundException
     * @throws InvalidOtpException
     */
    @Override
    public CustomOtpDTO getDataByKey(String key) throws RedisConnectorException, NotFoundException, InvalidOtpException {
        // 1. Get info from Redis by key
        logger.info("Getting OTP from key={}", key);
        Map<String, Object> redisData = redisConnector.getOtpByKey(key);
        if (redisData.isEmpty() || Objects.isNull(redisData.get(Constants.REDIS_OTP))) {
            throw new NotFoundException(String.format("Data not found by Key=%s", key));
        }
        long expireTimeInSecond = redisConnector.getExpireTimeInSecond(key);
        String otp = Objects.isNull(redisData.get(Constants.REDIS_OTP)) ? null : (String) redisData.get(Constants.REDIS_OTP);
        String uuid = Objects.isNull(redisData.get(Constants.REDIS_UUID)) ? null : (String) redisData.get(Constants.REDIS_UUID);
        Long regDt = Objects.isNull(redisData.get(Constants.REDIS_REG_DT)) ? null : (Long) redisData.get(Constants.REDIS_REG_DT);
        Integer counter = Objects.isNull(redisData.get(Constants.REDIS_COUNTER)) ? null : (Integer) redisData.get(Constants.REDIS_COUNTER);
        byte statusCode = Objects.isNull(redisData.get(Constants.REDIS_STATUS)) ? EnumOtpStatus.UNKNOWN.getCode() : (byte) redisData.get(Constants.REDIS_STATUS);
        String expireDate = getExpireDate(expireTimeInSecond);
        EnumOtpStatus status = EnumOtpStatus.get(statusCode);

        logger.info("OTP={}, UUID={}, expireDate={}", otp, uuid, expireDate);
        return new CustomOtpDTO(otp, uuid, status, counter, regDt, expireDate);
    }

    private String getExpireDate(long expireTimeInSecond) throws InvalidOtpException {
        try {
            Date expireDate = Date.from(Instant.ofEpochMilli(new Date().getTime() + expireTimeInSecond * 1000));
            return DateUtils.toDateString(expireDate);
        } catch (Exception e) {
            logger.error(String.format("Error calculating expire time %d, return null", expireTimeInSecond), e);
            throw new InvalidOtpException(String.format("Error calculating expire time %d, return null", expireTimeInSecond));

        }
    }

    @Override
    public CustomOtpDTO getKeyWithoutValidation(String otpCode) throws RedisConnectorException, InvalidOtpException, NotFoundException {
        // 1. Get info from Redis by otpCode
        logger.info("Getting key from OTP={}", otpCode);
        CustomOtpDTO returnedData = getDataByKey(otpCode);
        logger.info("OTP={} returns Data={}", otpCode, returnedData);
        return returnedData;
    }

    /***
     * Get UUID based on OTP code
     * Before returning UUID, need to verify the OTP if it is valid
     * @param otpCode
     * @return
     * @throws RedisConnectorException
     * @throws InvalidOtpException
     */
    @Override
    public String getUUID(String otpCode) throws RedisConnectorException, InvalidOtpException, NotFoundException {
        // 1. Get info from Redis by otpCode
        logger.info("Getting UUID from OTP={}", otpCode);
        // 1.5. Validate otpCode
        ValidateResult validateResult = validateOtp(otpCode, defaultLength);
        if (!validateResult.isValid()) {
            throw new InvalidOtpException(validateResult.getValidationMessage());
        }
        CustomOtpDTO returnedData = getDataByKey(otpCode);
        if (isOneTimeUse && returnedData.getStatus() == EnumOtpStatus.USED) {
            throw new InvalidOtpException(String.format("OTP is used, OTP=%s", otpCode));
        }
        // 2. Verify
        ValidateResult verifyResult = verifyOtp(otpCode, returnedData.getUuid(), returnedData.getCounter());
        // 3. Get UUID from Otp code
        if (!verifyResult.isValid()) {
            throw new InvalidOtpException(verifyResult.getValidationMessage());
        }
        logger.info("Valid OTP={}", otpCode);
        logger.info("OTP={} returns UUID={}", otpCode, returnedData.getUuid());
        return returnedData.getUuid();
    }

    @Override
    public String generateOTPCustom(CustomOTPRequestDTO request) throws InvalidRequestException,
            OtpGenerationException, RedisConnectorException {
        String key = request.getKey();
        Long timeToLive = request.getTtl();
        Integer length = request.getLength();
        // 0. Validate
        if (StringUtils.isEmpty(key)) {
            throw new InvalidRequestException("Null or empty key");
        }
        if (timeToLive == null || timeToLive <= 0) {
            logger.warn("Invalid TTL: {}, fallback to default TTL: {}", timeToLive, defaultTTL);
            timeToLive = (long) defaultTTL;
        }
        if (length == null || length <= 0) {
            logger.warn("Invalid OTP length: {}, fallback to default length: {}", length, defaultLength);
            length = defaultLength;
        }

        int counter = rand.nextInt(Integer.MAX_VALUE);
        String otp = generateOTP(key, length, counter);
        Map<String, Object> mapData = makeOtpData(key, otp, counter);
        redisConnector.putData(key, mapData, timeToLive);
        return otp;
    }

    private String generateOTP(String key, int length, int counter) throws OtpGenerationException {
        String otp;
        try {
            otp = buildHOptGenerator(key, length).generate(counter);
            logger.info("OTP generated. [UUID={}, Counter={}] > OTP={}", key, counter, otp);
            return otp;
        } catch (Exception e) {
            throw new OtpGenerationException(e.getMessage(), e);
        }
    }

    /***
     * Restore OTP
     * @param otpCode
     */
    @Override
    public void restoreOTP(String otpCode) throws InvalidOtpException, RedisConnectorException, NotFoundException {
        logger.info("Restore OTP={}", otpCode);
        ValidateResult validateResult = validateOtp(otpCode, defaultLength);
        if (!validateResult.isValid()) {
            throw new InvalidOtpException(validateResult.getValidationMessage());
        }
        Map<String, Object> dataByOtp = redisConnector.getOtpByKey(otpCode);
        if (dataByOtp.isEmpty()) {
            throw new NotFoundException(String.format("Data not found by OTP=%s", otpCode));
        }
        redisConnector.updateStatus(otpCode, EnumOtpStatus.NORMAL.getCode());
    }

    private ValidateResult validateOtp(String otpCode, int length) {
        ValidateResult validateResult = new ValidateResult();
        String otpRegex = String.format("^[0-9]{%d}$", length);
        Pattern otpPattern = Pattern.compile(otpRegex);
        boolean isMatched = otpPattern.matcher(otpCode).matches();
        validateResult.setValid(isMatched);
        if (!isMatched) {
            validateResult.setValidationMessage(String.format("Incorrect OTP code format, OTP=%s", otpCode));
        }
        return validateResult;
    }

    private ValidateResult verifyOtp(String otpCode, String uuid, int counter) {
        ValidateResult validateResult = new ValidateResult();
        try {
            // Re-generate OTP generator by uuid & counter
            HOTPGenerator hotpGenerator = buildHOptGenerator(uuid, defaultLength);
            boolean valid = hotpGenerator.verify(otpCode, counter);
            if (valid) {
                validateResult.setValid(true);
                validateResult.setValidationMessage("Success");
            } else {
                validateResult.setValid(false);
                validateResult.setValidationMessage(
                        String.format("OTP can not pass the verification, OTP=%s", otpCode));
            }
        } catch (Exception e) {
            logger.error(
                    String.format("Exception verifying OTP=%s, UUID=%s, counter=%d, Msg=%s ",
                            otpCode, uuid, counter, e.getMessage()), e);
            validateResult.setValid(false);
            validateResult.setValidationMessage(e.getMessage());
        }
        return validateResult;
    }
}
