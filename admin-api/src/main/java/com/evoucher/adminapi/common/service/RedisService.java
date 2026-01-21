package com.evoucher.adminapi.common.service;


public interface RedisService {
    String getRedisByKey(String redisKey);
    void setRedisByKey(String redisKey, Object dataRequest);
    void setRedisByKeyAndExpire(String redisKey, Object dataRequest, long expire);
    void delete(String redisKey);
}
