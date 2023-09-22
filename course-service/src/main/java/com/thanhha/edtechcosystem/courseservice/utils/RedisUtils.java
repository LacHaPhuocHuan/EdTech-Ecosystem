package com.thanhha.edtechcosystem.courseservice.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisUtils {
    private final RedisTemplate<String, Object> redisTemplate;

    public void putDataInCache(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }
    public Object getDataFromCache(String key) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            log.info("Use Cache!");
            return redisTemplate.opsForValue().get(key);

        } catch (Exception ex) {
            try {
                log.info("BYTE [] : {}", redisTemplate.dump(key));
                String jsonData= (String) redisTemplate.opsForValue().get(key);
                if(StringUtils.isBlank(jsonData))
                    return null;
                ObjectMapper obj=new ObjectMapper();
                log.info("Solution 02");
                return obj.readValue(jsonData, List.class);
            }catch (Exception e){
                redisTemplate.delete(key);
            }
        }
       return null;
    }

//    Xóa dữ liệu khỏi cache:
    public void evictDataFromCache(String key) {
        redisTemplate.delete(key);
    }

    public void setTTL(String key, long timeoutInSeconds) {
        redisTemplate.expire(key, timeoutInSeconds, TimeUnit.SECONDS);
    }
    private CacheManager cacheManager;

    public void putDataInCache(String key, Object value, Class<?> T) {
        redisTemplate.opsForValue().set(key, value);
    }

    public boolean checkExisted(String key){
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

}
