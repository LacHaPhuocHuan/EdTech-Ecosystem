package com.thanhha.edtechcosystem.courseservice.service.serviceImpl;

import com.thanhha.edtechcosystem.courseservice.dto.CoursesDto;
import com.thanhha.edtechcosystem.courseservice.dto.DataPage;
import com.thanhha.edtechcosystem.courseservice.dto.EnrollmentDto;
import com.thanhha.edtechcosystem.courseservice.entity.Course;
import com.thanhha.edtechcosystem.courseservice.entity.Enrollment;
import com.thanhha.edtechcosystem.courseservice.entity.Student;
import com.thanhha.edtechcosystem.courseservice.repository.CourseRepository;
import com.thanhha.edtechcosystem.courseservice.repository.EnrollmentRepository;
import com.thanhha.edtechcosystem.courseservice.repository.StudentRepository;
import com.thanhha.edtechcosystem.courseservice.service.ICourseService;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements ICourseService {
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;
    private final Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
    @Override
    public DataPage getCoursePage(int page, int size) {
        return null;
    }

    @Override
    public DataPage findCourse(String keyword, String category, BigDecimal price, int page) {
        return null;
    }

    @Override
    public CoursesDto getCourse(String coursesCode) {
        return null;
    }

    @Override
    public CoursesDto createCourse(CoursesDto coursesDto) {
        return null;
    }

    @Override
    public EnrollmentDto enrollCourse(String idCourse) {
        String email=authentication.getPrincipal().toString();
        Student student=studentRepository.findByEmail(email).orElseThrow(()->new NotFoundException("Your enrollment is unacceptable"));
        Course course=courseRepository.findById(idCourse).orElseThrow(()->new NotFoundException("Course don't exist."));
        Enrollment enrollment=Enrollment.builder()
                .course(course)
                .student(student)
                .enrolledCode(generateEnrollCode())
                .enrolledDate(new Date())
                .build();
        var saveEnrollment=enrollmentRepository.save(enrollment);
        return modelMapper.map(enrollment, EnrollmentDto.class);
    }

    private String generateEnrollCode() {
        String randomCode= UUID.randomUUID().toString();
        String code=randomCode.substring(9,16);
        if(studentRepository.existsById(code))
            return generateEnrollCode();
        return code;
    }

    @Override
    public CoursesDto updateCourse(CoursesDto coursesDto) {
        return null;
    }
}
