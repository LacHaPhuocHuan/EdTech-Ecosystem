package com.thanhha.edtechcosystem.userservice.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;

import java.text.SimpleDateFormat;

@Configuration
public class JsonMessageConverter {
    @Bean
    public HttpMessageConverter<Object> customJsonHttpMessageConverter() {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();

        // Tùy chỉnh ObjectMapper để cấu hình JSON theo ý muốn
        ObjectMapper objectMapper = jsonConverter.getObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("dd/MM/yyyy"));
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // Bỏ qua các trường không biết trong JSON

        jsonConverter.setObjectMapper(objectMapper);

        return jsonConverter;
    }
}
