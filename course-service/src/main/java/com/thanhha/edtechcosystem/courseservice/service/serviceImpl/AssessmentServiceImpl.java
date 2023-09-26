package com.thanhha.edtechcosystem.courseservice.service.serviceImpl;

import com.thanhha.edtechcosystem.courseservice.dto.AssessmentDto;
import com.thanhha.edtechcosystem.courseservice.dto.QuestionDto;
import com.thanhha.edtechcosystem.courseservice.entity.Assessment;
import com.thanhha.edtechcosystem.courseservice.entity.Question;
import com.thanhha.edtechcosystem.courseservice.entity.Questionnaire;
import com.thanhha.edtechcosystem.courseservice.repository.*;
import com.thanhha.edtechcosystem.courseservice.service.IAssessmentService;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.ServerErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class,RuntimeException.class})
@Slf4j
public class AssessmentServiceImpl implements IAssessmentService {
    private final AssessmentRepository assessmentRepository;
    private final ModelMapper modelMapper;
    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    private final QuestionnaireRepository questionnaireRepository;
    private final QuestionRepository questionRepository;


    @Override
    public AssessmentDto getById(Long id) {
        //todo.. use redis cache
        var assessment=assessmentRepository.findById(id).orElseThrow(()-> new NotFoundException("Assessment don't exist."));
        var result =modelMapper.map(assessment,AssessmentDto.class);
        result.setQuestions(
                questionRepository.findByQuestionnaireId(assessment.getQuestionnaire().getId()).stream().map(question -> modelMapper.map(question, QuestionDto.class)).toList()
        );
        return result;
    }

    @Override
    public List<AssessmentDto> getAllOnCourse(String idCourse) {
        //todo.. use redis cache
        try {
            var assessment = assessmentRepository.findByCourseId(Long.valueOf(idCourse));
            return assessment.stream()//
                    .map(asm -> modelMapper.map(asm, AssessmentDto.class))
                    .peek(assessmentDto -> assessmentDto.setQuestions(
                            questionRepository
                            .findByQuestionnaireId(assessmentDto.getQuestionnaireId())
                                    .stream().map(question -> modelMapper.map(question, QuestionDto.class)).toList()
                    ))
                    .collect(Collectors.toList());
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ArrayList<>();
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
                    .build();
            final var saveQuestionnaire= questionnaireRepository.save(questionnaire);
            List<Question> questions=assessmentDto.getQuestions()
                    .stream()
                    .map(questionDto -> {
                        var question=modelMapper.map(questionDto, Question.class);
                        question.setQuestionnaire(questionnaire);
                        return question;
                    }
                    )
                    .peek(questionRepository::save)
                    .toList();
            assessment.setQuestionnaire(saveQuestionnaire);
            assessment.setCourse(course);
            var saveAssessment=assessmentRepository.save(assessment);
            saveQuestionnaire.setAssessment(saveAssessment);
            var result=modelMapper.map(saveAssessment,AssessmentDto.class);
            result.setQuestions(questions.stream().map(question -> modelMapper.map(question, QuestionDto.class)).collect(Collectors.toList()));
            return result;
        }catch (Exception ignored){
            ignored.printStackTrace();
            throw new ServerErrorException(500);
        }

    }
    private boolean validAssessment(AssessmentDto assessmentDto) {
//        todo...
        return true;
    }

    @Override
    public AssessmentDto updateAssessment(AssessmentDto assessmentDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email= (String) authentication.getPrincipal();
        var course=courseRepository.findById(assessmentDto.getCourseId()).orElseThrow(BadRequestException::new);
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
