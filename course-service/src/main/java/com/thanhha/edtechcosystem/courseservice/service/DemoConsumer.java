package com.thanhha.edtechcosystem.courseservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.thanhha.edtechcosystem.courseservice.dto.UserDto;
import com.thanhha.edtechcosystem.courseservice.entity.JwtTokenDto;
import com.thanhha.edtechcosystem.courseservice.repository.TentativeJwtTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;

@Service
@Slf4j
@RequiredArgsConstructor
public class DemoConsumer {

}
