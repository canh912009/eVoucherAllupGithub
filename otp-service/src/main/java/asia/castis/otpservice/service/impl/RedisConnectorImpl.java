package asia.castis.otpservice.service.impl;

import asia.castis.otpservice.common.Constants;
import asia.castis.otpservice.exception.RedisConnectorException;
import asia.castis.otpservice.service.RedisConnector;
import io.lettuce.core.RedisCommandTimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisConnectorImpl implements RedisConnector {
    private final RedisTemplate<String, Map<String, Object>> redisTemplate;

    @Override
    public void putData(String key, Map<String, Object> data, long ttl) throws RedisConnectorException {
        try {
            HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
            hashOperations.putAll(key, data);
            redisTemplate.expire(key, ttl, TimeUnit.SECONDS);
            log.info("OTP put to redis. Key={}, value={}, ttl={}", key, data, ttl);
        } catch (RedisConnectionFailureException | RedisSystemException | RedisCommandTimeoutException e) {
            throw redisException(key, e);
        }
    }

    @Override
    public Map<String, Object> getOtpByKey(String key) throws RedisConnectorException {
        try {
            HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
            return hashOperations.entries(key);
        } catch (RedisConnectionFailureException | RedisSystemException | RedisCommandTimeoutException e) {
            throw redisException(key, e);
        }
    }

    @Override
    public Long getExpireTimeInSecond(String key) throws RedisConnectorException {
        try {
            return redisTemplate.getExpire(key, TimeUnit.SECONDS);
        } catch (RedisConnectionFailureException | RedisSystemException | RedisCommandTimeoutException e) {
            throw redisException(key, e);
        }
    }
    @Override
    public void updateStatus(String otp, byte status) throws RedisConnectorException {
        try {
            HashOperations<String, String, Byte> hashOps = redisTemplate.opsForHash();
            hashOps.put(otp, Constants.REDIS_STATUS, status);
            log.info("OTP update status key={}, status={}", otp, status);
        } catch (RedisConnectionFailureException | RedisSystemException | RedisCommandTimeoutException e) {
            throw redisException(otp, e);
        }
    }

    private static RedisConnectorException redisException(String key, RuntimeException e) {
        return new RedisConnectorException(String.format("%s, OTP=%s", e.getMessage(), key), e);
    }
}
