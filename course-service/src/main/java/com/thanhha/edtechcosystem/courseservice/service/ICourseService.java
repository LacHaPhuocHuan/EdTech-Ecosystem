package com.thanhha.edtechcosystem.courseservice.service;

import com.thanhha.edtechcosystem.courseservice.dto.CoursesDto;
import com.thanhha.edtechcosystem.courseservice.dto.DataPage;
import com.thanhha.edtechcosystem.courseservice.dto.EnrollmentDto;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.math.BigDecimal;

public interface ICourseService {
    @Cacheable(value = "course_course_page", key = "'page_'+#page+'_'+#size")
    DataPage getCoursePage(int page, int size);

    DataPage findCourse(String keyword, String category, BigDecimal price, int page);

    CoursesDto getCourse(String coursesCode);

    CoursesDto createCourse(CoursesDto coursesDto);

    EnrollmentDto enrollCourse(Long idCourse);

    CoursesDto updateCourse(CoursesDto coursesDto);
}
