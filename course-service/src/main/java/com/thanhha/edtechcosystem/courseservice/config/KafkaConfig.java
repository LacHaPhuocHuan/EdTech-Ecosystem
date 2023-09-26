package com.thanhha.edtechcosystem.courseservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {
    @Bean
    NewTopic newJwtTopic(){
        return new NewTopic("taken-course",3, (short) 1);
    }

}
