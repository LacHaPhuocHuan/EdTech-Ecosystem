package com.thanhha.edtechcosystem.courseservice.service.serviceImpl;

import com.thanhha.edtechcosystem.courseservice.dto.Certificate;
import com.thanhha.edtechcosystem.courseservice.entity.*;
import com.thanhha.edtechcosystem.courseservice.repository.LectureRepository;
import com.thanhha.edtechcosystem.courseservice.repository.QuestionRepository;
import com.thanhha.edtechcosystem.courseservice.repository.StudentAssessmentRepository;
import com.thanhha.edtechcosystem.courseservice.repository.StudentCourseRepository;
import com.thanhha.edtechcosystem.courseservice.service.IEnrollmentService;
import com.thanhha.edtechcosystem.courseservice.utils.KafkaUtils;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements IEnrollmentService {
    private final StudentCourseRepository repository;
    private final KafkaUtils kafkaUtils;
    private final LectureRepository lectureRepository;
    private final StudentAssessmentRepository studentAssessmentRepository;
    private final QuestionRepository questionRepository;
    private final ModelMapper modelMapper;
    @Override
    public Certificate completeCourse(String code) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        var username=authentication.getPrincipal();
        var infoEnrollment= repository.findById(code)
                .orElseThrow(()->new NotFoundException("Code is incorrect"));
        if(!username.toString().trim().equals(infoEnrollment.getStudent().getEmail().trim()))
            throw new AccessDeniedException("You do not have permission to access this resource");
        infoEnrollment.setStatus(Status.completion);
        infoEnrollment.setCompletedDate(new Date());
        Float gpa=computeGPA(infoEnrollment.getStudent().getId(), infoEnrollment.getCourse().getId());
        if(!gpa.isNaN())
            infoEnrollment.setGPA(gpa);
        var saveInfo = repository.save(infoEnrollment);

        Certificate certificate
                =modelMapper.map(saveInfo, Certificate.class );
        if(certificate.getGPA()<3.5) certificate.setClassification("F");
        else if(certificate.getGPA()<5.0) certificate.setClassification("D");
        else if(certificate.getGPA()<6.5) certificate.setClassification("C");
        else if(certificate.getGPA()<8.5) certificate.setClassification("B");
        else if(certificate.getGPA()<10) certificate.setClassification("A");
        kafkaUtils.send("taken-course", certificate);

        return certificate;
    }

    private Float computeGPA(String idStudent, Long idCourse) {
        List<StudentAssessment> studentAssessments = studentAssessmentRepository.findByStudentId(idStudent);
        List<StudentAssessment> assessmentsInCourseByStudent = studentAssessments
                .stream().filter(studentAssessment ->
                        studentAssessment.getAssessment().getCourse().getId().equals(idCourse)
                ).toList();
        Float totalScore = computeGPA(assessmentsInCourseByStudent, 0);
        return totalScore/(float)assessmentsInCourseByStudent.size();
    }
    private Float computeGPA(List<StudentAssessment> studentAssessments, int n){
        if(n<studentAssessments.size()) {
            StudentAssessment studentAssessment=studentAssessments.get(n);
            List<Question> questions=questionRepository.findByQuestionnaireId(studentAssessment.getAssessment().getQuestionnaire().getId());
            int trueAnswer=studentAssessment.getTrueAnswer();
            Float gpa= ((float) trueAnswer/(float) questions.size());
            return gpa+computeGPA(studentAssessments,n+1);
        }
        else return 0.f;
    }

}
