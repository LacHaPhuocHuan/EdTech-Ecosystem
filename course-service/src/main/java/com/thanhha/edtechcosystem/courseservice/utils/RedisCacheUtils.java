package com.thanhha.edtechcosystem.courseservice.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RedisCacheUtils {
    private final RedisCacheManager redisCacheManager;
//    private final RedisCache redisCache;
//
//
//
//
//    public <T> void putData(String key, T value){
//        redisCache.put(key,value);
//    }
//
//    public <T> T getData(String key, Class<T> tClass ){
//        return  (T) redisCache.get(key);
//    }
//
//
//    public <T> void evict(String key){
//        redisCache.evict(key);
//    }

}
