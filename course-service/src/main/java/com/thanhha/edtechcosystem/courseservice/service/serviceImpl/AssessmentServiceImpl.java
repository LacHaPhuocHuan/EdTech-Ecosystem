package com.thanhha.edtechcosystem.courseservice.service.serviceImpl;

import com.thanhha.edtechcosystem.courseservice.dto.AssessmentDto;
import com.thanhha.edtechcosystem.courseservice.entity.Assessment;
import com.thanhha.edtechcosystem.courseservice.entity.Question;
import com.thanhha.edtechcosystem.courseservice.entity.Questionnaire;
import com.thanhha.edtechcosystem.courseservice.repository.AssessmentRepository;
import com.thanhha.edtechcosystem.courseservice.repository.CourseRepository;
import com.thanhha.edtechcosystem.courseservice.repository.InstructorRepository;
import com.thanhha.edtechcosystem.courseservice.service.IAssessmentService;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssessmentServiceImpl implements IAssessmentService {
    private final AssessmentRepository assessmentRepository;
    private final ModelMapper modelMapper;
    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;

    @Override
    public AssessmentDto getById(String id) {
        //todo.. use redis cache
        var assessment=assessmentRepository.findById(Long.parseLong(id)).orElseThrow(()-> new NotFoundException("Assessment don't exist."));
        return modelMapper.map(assessment,AssessmentDto.class);
    }

    @Override
    public List<AssessmentDto> getAllOnCourse(String idCourse) {
        //todo.. use redis cache
        var assessment=assessmentRepository.findByCourseId(idCourse);
        return assessment.stream()//
                .map(asm ->modelMapper.map(asm,AssessmentDto.class) ).collect(Collectors.toList());
    }

    @Override
    public AssessmentDto createAssessment(AssessmentDto assessmentDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email= (String) authentication.getPrincipal();
        var course=courseRepository.findById(Long.valueOf(assessmentDto.getCourseId())).orElseThrow(BadRequestException::new);
        if(!course.getInstructor().getEmail().trim().equals(email.trim()))
            throw new BadRequestException("You don't have authority to create assessment on this course.");
        if(!validAssessment(assessmentDto))
            throw new BadRequestException("Assessment Information don't correct. ");
        var assessment=modelMapper.map(assessmentDto, Assessment.class);
        try {
            var questionnaire = Questionnaire.builder()
                    .assessment(assessment)
                    .questions(assessmentDto.getQuestions().stream().map(questionDto -> modelMapper.map(questionDto, Question.class)).toList())
                    .build();
            assessment.setQuestionnaire(questionnaire);
        }catch (Exception ignored){
        }
        assessment.setCourse(course);
        var saveAssessment=assessmentRepository.save(assessment);
        return modelMapper.map(saveAssessment,AssessmentDto.class);
    }

    private boolean validAssessment(AssessmentDto assessmentDto) {
//        todo...

        return true;
    }

    @Override
    public AssessmentDto updateAssessment(AssessmentDto assessmentDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email= (String) authentication.getPrincipal();
        var course=courseRepository.findById(Long.valueOf(assessmentDto.getCourseId())).orElseThrow(BadRequestException::new);
        if(!course.getInstructor().getEmail().trim().equals(email.trim()))
            throw new BadRequestException("You don't have authority to update assessment on this course.");
        var assessment=assessmentRepository.findById(assessmentDto.getId()).orElseThrow(NotFoundException::new);
        if(!Objects.isNull(assessmentDto.getStatus()))
            assessment.setStatus(assessment.getStatus());
        if(!Objects.isNull(assessmentDto.getDescription()))
            assessment.getQuestionnaire().setDescription(assessmentDto.getDescription());
        var updatedAssessment=assessmentRepository.save(assessment);
        return modelMapper.map(updatedAssessment,AssessmentDto.class);
    }
}
