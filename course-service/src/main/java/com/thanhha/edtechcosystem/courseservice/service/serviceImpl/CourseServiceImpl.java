package com.thanhha.edtechcosystem.courseservice.service.serviceImpl;

import com.thanhha.edtechcosystem.courseservice.dto.CoursesDto;
import com.thanhha.edtechcosystem.courseservice.dto.DataPage;
import com.thanhha.edtechcosystem.courseservice.dto.EnrollmentDto;
import com.thanhha.edtechcosystem.courseservice.entity.Course;
import com.thanhha.edtechcosystem.courseservice.entity.Enrollment;
import com.thanhha.edtechcosystem.courseservice.entity.Instructor;
import com.thanhha.edtechcosystem.courseservice.entity.Student;
import com.thanhha.edtechcosystem.courseservice.repository.*;
import com.thanhha.edtechcosystem.courseservice.service.ICourseService;
import com.thanhha.edtechcosystem.courseservice.utils.RedisUtils;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.ServerErrorException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.expression.AccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements ICourseService {
    private final String REDIS_CACHE_KEY="course_cache";
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;
    private final RedisUtils redisUtils;
    private final CategoryRepository categoryRepository;
    private final InstructorRepository instructorRepository;
    private final Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
    @Override
    public DataPage getCoursePage(int page, int size) {

        return new DataPage(page,size, this.getCourses());
    }

    @Override
    public DataPage findCourse(String keyword, String category, BigDecimal price, int page) {
        return new DataPage(page,10,
                this.getCourses().stream()
                        .filter(coursesDto -> {
                            var instructor=instructorRepository.findById(coursesDto.getTeacherId()).orElseThrow(()->new ServerErrorException(500));
                            return
                                    coursesDto.getTitle().contains(keyword)
                                    || coursesDto.getDescription().equals(keyword)
                                    || instructor.getFullName().contains(keyword)
                                    || instructor.getEmail().contains(keyword)
                                    ;
                        })
                        .toList()
        );
    }

    @Override
    public CoursesDto getCourse(String coursesCode) {
        var enrollment =enrollmentRepository.findById(coursesCode).orElseThrow(()->new NotFoundException(""));
        if(!enrollment.getStudent().getEmail().equals(authentication.getPrincipal()))
            throw new BadRequestException("You need enrollment this course.");

        return modelMapper.map(enrollment.getCourse(), CoursesDto.class);
    }

    @Override
    public CoursesDto createCourse(CoursesDto coursesDto) {
        var course=modelMapper.map(coursesDto,Course.class);
        if(!Objects.isNull(course.getId()))
            course.setId(null);
        course.setInstructor(instructorRepository.findByEmail(authentication.getPrincipal().toString()).orElseThrow(()->new ServerErrorException(500)));
        course.setCategory(categoryRepository.findById(coursesDto.getCategoryId()).orElseThrow(()->new BadRequestException("Category don't correct.")));
        course.setCreateDate(new Date());
        Course saveCourse=courseRepository.save(course);
        return modelMapper.map(course,CoursesDto.class);
    }

    @Override
    public EnrollmentDto enrollCourse(Long idCourse) {
        String email=authentication.getPrincipal().toString();
        Student student=studentRepository.findByEmail(email).orElseThrow(()->new NotFoundException("Your enrollment is unacceptable"));
        Course course= courseRepository.findById(idCourse).orElseThrow(()->new NotFoundException("Course don't exist."));
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
        if(Objects.isNull(coursesDto.getId()))
            throw new BadRequestException("You need choose course to update");
        var course=courseRepository.findById(coursesDto.getId())
                .orElseThrow(()->new NotFoundException("This course don't exist."));
        Instructor thisInstructor=course.getInstructor();
        if(!thisInstructor.getEmail().equals(authentication.getPrincipal()))
            throw new BadRequestException("You don't have authority to this updating operation.");
        if (coursesDto.getTitle() != null && !Objects.equals(coursesDto.getTitle().trim(), "")) {
            course.setTitle(coursesDto.getTitle());
        }
        if (coursesDto.getDescription() != null && !Objects.equals(coursesDto.getDescription().trim(), "")) {
            course.setDescription(coursesDto.getDescription());
        }
        if (coursesDto.getCategoryId()!= null && !Objects.equals(coursesDto.getCategoryId(), 0L)) {
            var category=categoryRepository.findById(coursesDto.getCategoryId()).orElseThrow(()->new NotFoundException("This category is incorrect."));
            course.setCategory(category);
        }
        if (coursesDto.getExpiredDate() != null && !Objects.equals(coursesDto.getExpiredDate(), course.getExpiredDate())
        ) {
            course.setExpiredDate(coursesDto.getExpiredDate());
        }
        var saveCourse=courseRepository.save(course);
        redisUtils.evictDataFromCache(REDIS_CACHE_KEY);
        return modelMapper.map(saveCourse,CoursesDto.class);
    }


    private List<CoursesDto> getCourses(){
        if(redisUtils.checkExisted(REDIS_CACHE_KEY)) {
            return (List<CoursesDto>) redisUtils.getDataFromCache(REDIS_CACHE_KEY);
        }
        List<Course> courses=courseRepository.findAll();
        List<CoursesDto> coursesDto= courses.stream()
                .map(course -> modelMapper.map(course,CoursesDto.class)).toList();
        redisUtils.putDataInCache(REDIS_CACHE_KEY, coursesDto);
        return  coursesDto;

    }

    private CoursesDto findById(Long id){
        if(redisUtils.checkExisted(REDIS_CACHE_KEY)) {
            var courses= (List<CoursesDto>) redisUtils.getDataFromCache(REDIS_CACHE_KEY);

            return  courses.stream()
                    .filter(coursesDto -> coursesDto.getId().equals(id))
                    .findFirst().orElseThrow(NotFoundException::new)
                    ;
        }
        var courseOnDB =courseRepository.findById(id).orElseThrow(()->new NotFoundException("Course don't exist."));
        return modelMapper.map( courseOnDB, CoursesDto.class);
    }
}
