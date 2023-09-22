package com.thanhha.edtechcosystem.courseservice.service.kafka.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.thanhha.edtechcosystem.courseservice.dto.UserDto;
import com.thanhha.edtechcosystem.courseservice.entity.Instructor;
import com.thanhha.edtechcosystem.courseservice.entity.JwtTokenDto;
import com.thanhha.edtechcosystem.courseservice.entity.Student;
import com.thanhha.edtechcosystem.courseservice.repository.InstructorRepository;
import com.thanhha.edtechcosystem.courseservice.repository.StudentRepository;
import com.thanhha.edtechcosystem.courseservice.repository.TentativeJwtTokenRepository;
import com.thanhha.edtechcosystem.courseservice.security.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final TentativeJwtTokenRepository jwtTokenRepository;
    private final ModelMapper modelMapper;
    private final InstructorRepository instructorRepository;
    private final StudentRepository studentRepository;
    @KafkaListener(id = "authGroup", topics = "auth-token")
    public void listenCreateToken( byte[] bytes) throws IOException {
        log.info(Arrays.toString(bytes));
        ObjectMapper objectMapper=  new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        JwtTokenDto jwtToken =objectMapper.readValue(bytes,JwtTokenDto.class);
        jwtTokenRepository.save(jwtToken);
    }
    @KafkaListener(id = "userGroup", topics = "new-user")
    public void listenCreateUserToken( byte[] bytes) throws IOException {
        log.info(Arrays.toString(bytes));
        ObjectMapper objectMapper=  new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        UserDto userDto=objectMapper.readValue(bytes, UserDto.class);
        if(userDto.getRole().equals(Role.TEACHER)){
            var instructor=modelMapper.map(userDto, Instructor.class);
            instructor.setId(userDto.getId());
            instructor.setFullName(userDto.getFirstname() +" " +userDto.getLastname());
            instructorRepository.save(instructor);
        }else if(userDto.getRole().equals(Role.STUDENT)){
            var student=modelMapper.map(userDto, Student.class);
            student.setId(userDto.getId());
            student.setFullName(userDto.getFirstname() +" " +userDto.getLastname());
            studentRepository.save(student);
        }else{
            return;
        }
    }
}
