package com.thanhha.edtechcosystem.userservice.service.kafkaService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.thanhha.edtechcosystem.userservice.dto.Certificate;
import com.thanhha.edtechcosystem.userservice.model.CourseTaken;
import com.thanhha.edtechcosystem.userservice.repositiry.CourseTakenRepository;
import com.thanhha.edtechcosystem.userservice.repositiry.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseService {
    private final CourseTakenRepository courseTakenRepository;
    private final UserRepository userRepository;
    @KafkaListener(id = "userService", topics = "taken-course")
    public void ListenerTakenCourse(byte[] bytes) throws IOException {
        log.info(Arrays.toString(bytes));
        ObjectMapper objectMapper=  new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        var certificate=objectMapper.readValue(bytes, Certificate.class);
        var user= userRepository.findById(certificate.getStudentId()).orElseThrow();
        var takenCourse=CourseTaken.builder()
                .idCourse(certificate.getCourseId())
                .name(certificate.getCourseTitle())
                .user(user)
                .completeAt(certificate.getCompleteDate())
                .attendAt(certificate.getEnrolledDate())
                .build();

        courseTakenRepository.save(takenCourse);
    }
}
