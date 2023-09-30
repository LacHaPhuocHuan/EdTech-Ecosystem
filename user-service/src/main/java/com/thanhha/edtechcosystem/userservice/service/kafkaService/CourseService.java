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
    @KafkaListener(id="user_service", topics = "taken-course")
    public void ListenerTakenCourse(String jsonPayload) throws IOException {
        log.info("ListenerTakenCourse");
        log.info(jsonPayload);
        ObjectMapper objectMapper=  new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        var certificate=objectMapper.readValue(jsonPayload, Certificate.class);
        var user= userRepository.findById(certificate.getStudentId()).orElseThrow();
        if(courseTakenRepository.existsByUserIdAndIdCourse(user.getId(), certificate.getCourseId())) {
            log.info("Existed!");
            return;
        }
        var takenCourse=CourseTaken.builder()
                .idCourse(certificate.getCourseId())
                .name(certificate.getCourseTitle())
                .user(user)
                .completeAt(certificate.getCompletedDate())
                .attendAt(certificate.getEnrolledDate())
                .build();

        courseTakenRepository.save(takenCourse);
    }
}
