package com.thanhha.edtechcosystem.courseservice.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisUtils {
    private final RedisTemplate<String, Object> redisTemplate;

    public void putDataInCache(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }
    public <T> Object getDataFromCache(String key ,Class<T> t)  {
        ObjectMapper obj=new ObjectMapper();
        try {
            log.info("Use Cache!");
            return redisTemplate.opsForValue().get(key);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            try {
                log.info("BYTE [] : {}", redisTemplate.dump(key));
                log.info("Solution 02");
                return obj.readValue(redisTemplate.dump(key), ArrayList.class);
            }catch (Exception e){
                log.error(e.getMessage());
                try{
                    log.info("Solution 03");
                    String json= redisTemplate.getStringSerializer().toString();
                    String fixedJsonString = obj.writeValueAsString(json);
                    return obj.readValue(fixedJsonString, ArrayList.class);
                }catch (Exception exception){
                    log.error(exception.getMessage());
                    try{
                        log.info("solution 04");
                        HashMapping hashMapping=new HashMapping();
                        return hashMapping.loadHash(key);
                    }catch (Exception exception1)
                    {
                        log.error(e.getMessage());

                    }
                }
                redisTemplate.delete(key);
                log.info("Exist after delete :{}", checkExisted(key));
            }
        }
       return null;
    }
    public Object getDataFromCache(String key){
        return redisTemplate.opsForValue().get(key);
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
