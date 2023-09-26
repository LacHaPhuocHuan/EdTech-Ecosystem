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
import com.thanhha.edtechcosystem.courseservice.utils.RedisCacheUtils;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.ServerErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements ICourseService {
    private String REDIS_CACHE_KEY="";
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;
//    private final RedisUtils redisUtils;
    private final RedisCacheUtils redisCacheUtils;
    private final CategoryRepository categoryRepository;
    private final InstructorRepository instructorRepository;
    private Authentication authentication;

    @Override
    public DataPage getCoursePage(int page, int size) {
        log.info("Get course with cache :{}","page_"+page+size);
        var dataPage =new DataPage(page,size, this.getCourses());
        saveTotalPage(dataPage.getTotal(), size);
        return dataPage;
    }
    @Cacheable(value = "course_course", key = "all")
    private List<CoursesDto> getCourses(){
        List<Course> courses=courseRepository.findAll();
        List<CoursesDto> coursesDto= courses.stream()
                .map(course -> modelMapper.map(course,CoursesDto.class)).toList();
        log.info("Courses size: {}", coursesDto.size());
        return  coursesDto;
    }



    @CachePut(value = "course_course_maxPage_value", key = "'ok'")
    private void saveTotalPage(int total, int size) {
        if(REDIS_CACHE_KEY.equals("page_"+total+size))
            return;
        REDIS_CACHE_KEY="page_"+total+size;

    }

    @Override
    public DataPage findCourse(String keyword, String category, BigDecimal price, int page) {
        if(category==null)
            category="";
        if(price==null)
            price= BigDecimal.valueOf(0.00);
        String finalCategory = category;
        return new DataPage(page,10,
                this.getCourses().stream()
                        .filter(coursesDto -> {
                            var instructor=instructorRepository.findById(coursesDto.getInstructorId()).orElseThrow(()->new ServerErrorException(500));
                            var categoryForThis =categoryRepository.findById(coursesDto.getCategoryId()).orElseThrow(()->new ServerErrorException(500));
                            if(categoryForThis.getDescription()==null)
                                categoryForThis.setDescription("");
                            return
                                    (
                                            coursesDto.getTitle().toLowerCase().contains(keyword.toLowerCase())
                                            || coursesDto.getDescription().toLowerCase().contains(keyword.toLowerCase())
                                            || instructor.getFullName().toLowerCase().contains(keyword.toLowerCase())
                                            || instructor.getEmail().toLowerCase().contains(keyword.toLowerCase())
                                    ) &&
                                    (
                                            categoryForThis.getName().toLowerCase().contains(finalCategory.toLowerCase().trim())
                                            || categoryForThis.getDescription().toLowerCase().contains(finalCategory.toLowerCase().trim())
                                    )
                                    ;
                        })
                        .toList()
        );
    }

    @Override
    public CoursesDto getCourse(String coursesCode) {
        authentication=SecurityContextHolder.getContext().getAuthentication();
        var enrollment =enrollmentRepository.findById(coursesCode).orElseThrow(()->new NotFoundException(""));
        if(!enrollment.getStudent().getEmail().equals(authentication.getPrincipal()))
            throw new BadRequestException("You need enrollment this course.");

        return modelMapper.map(enrollment.getCourse(), CoursesDto.class);
    }

    @Override
    public CoursesDto createCourse(CoursesDto coursesDto) {
        authentication=SecurityContextHolder.getContext().getAuthentication();
        var course=modelMapper.map(coursesDto,Course.class);
        if(!Objects.isNull(course.getId()))
            course.setId(null);
        if(authentication==null)
            log.info("authentication is null");
        course.setInstructor(instructorRepository.findByEmail(authentication.getPrincipal().toString()).orElseThrow(()->new ServerErrorException(500)));
        course.setCategory(categoryRepository.findById(coursesDto.getCategoryId()).orElseThrow(()->new BadRequestException("Category don't correct.")));
        course.setCreateDate(new Date());
        Course saveCourse=courseRepository.save(course);
        deleteCache();
        return modelMapper.map(course,CoursesDto.class);
    }



    @Override
    public EnrollmentDto enrollCourse(Long idCourse) {
        authentication=SecurityContextHolder.getContext().getAuthentication();
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
        authentication=SecurityContextHolder.getContext().getAuthentication();

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
//        redisCacheUtils.evict(REDIS_CACHE_KEY);
        return modelMapper.map(saveCourse,CoursesDto.class);
    }



    @Cacheable(value = "course_course", key ="'ById_'+#id")
    private CoursesDto findById(Long id){
        try {
            List<CoursesDto> courses=getCoursesInCache();
            if(!courses.isEmpty())
                return courses.stream().filter(coursesDto -> coursesDto.getId().equals(id)).findFirst().orElseThrow(NotFoundException::new);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        var courseOnDB =courseRepository.findById(id).orElseThrow(()->new NotFoundException("Course don't exist."));
        return modelMapper.map( courseOnDB, CoursesDto.class);
    }



    //----------------------------------Handle Cache-----------------------------
    @Cacheable(value = "course_course", key = "all")
    private List<CoursesDto> getCoursesInCache() {
        return new ArrayList<>();
    }

    private void deleteCache() {
        log.info("DELETE CACHE");
        String cacheKey= getTotalKey();
        deletePageOnCache(cacheKey);
    }

    @CacheEvict(value = "course_course_data_page", key = "#cacheKey")
    private void deletePageOnCache(String cacheKey) {
        log.info("deletePageOnCache {}", cacheKey);
    }

    @Cacheable(value = "course_course_maxPage_value", key = "'ok'")
    private String getTotalKey() {
        return REDIS_CACHE_KEY;
    }
}
