package com.thanhha.edtechcosystem.courseservice.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class KafkaUtils {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    public void send(String topic, Object value){
        kafkaTemplate.send(topic,value);
    }
}
