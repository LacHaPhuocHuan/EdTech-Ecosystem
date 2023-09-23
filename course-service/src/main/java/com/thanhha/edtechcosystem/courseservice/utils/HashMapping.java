package com.thanhha.edtechcosystem.courseservice.utils;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.data.redis.hash.ObjectHashMapper;

import java.util.Map;
@NoArgsConstructor
public class HashMapping {

    @Autowired
    HashOperations<String, byte[], byte[]> hashOperations;

    HashMapper<Object, byte[], byte[]> mapper = new ObjectHashMapper();

    public void writeHash(String key, Object person) {

        Map<byte[], byte[]> mappedHash = mapper.toHash(person);
        hashOperations.putAll(key, mappedHash);
    }

    public Object loadHash(String key) {

        Map<byte[], byte[]> loadedHash = hashOperations.entries("key");
        return (Object) mapper.fromHash(loadedHash);
    }
}
