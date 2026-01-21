package com.evoucher.adminapi.common.service;

import com.evoucher.adminapi.common.utils.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisServiceImpl implements RedisService, Serializable{
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public String getRedisByKey(String redisKey) {
        return redisTemplate.opsForValue().get(redisKey);
    }

    @Override
    public void setRedisByKey(String redisKey, Object dataRequest) {
        if(ObjectUtils.isNotEmpty(dataRequest)) {
            redisTemplate.opsForValue().set(redisKey , Constant.gson.toJson(dataRequest));
        }
    }

    @Override
    public void setRedisByKeyAndExpire(String redisKey, Object dataRequest, long expire) {
        if(ObjectUtils.isNotEmpty(dataRequest)) {
            redisTemplate.opsForValue().set(redisKey , Constant.gson.toJson(dataRequest));
            redisTemplate.expire(redisKey, expire, TimeUnit.MINUTES);
        }
    }

    public void delete(String redisKey) {
        redisTemplate.delete(redisKey);
    }
}
