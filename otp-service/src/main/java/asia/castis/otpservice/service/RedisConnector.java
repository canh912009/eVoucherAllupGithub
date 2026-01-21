package asia.castis.otpservice.service;

import asia.castis.otpservice.exception.RedisConnectorException;

import java.util.Map;

public interface RedisConnector {

    void putData(String otpCode, Map<String, Object> data, long ttl) throws RedisConnectorException;

    Map<String, Object> getOtpByKey(String key) throws RedisConnectorException;

    Long getExpireTimeInSecond(String key) throws RedisConnectorException;

    void updateStatus(String otp, byte status) throws RedisConnectorException;
}
