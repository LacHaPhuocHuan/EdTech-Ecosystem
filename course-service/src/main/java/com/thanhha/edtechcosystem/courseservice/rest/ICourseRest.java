package com.thanhha.edtechcosystem.courseservice.rest;

import com.thanhha.edtechcosystem.courseservice.dto.CoursesDto;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RequestMapping("/api/v1/courses/courses")
public interface ICourseRest {
    //GET
    @GetMapping("/all")
    public ResponseEntity<?> getCoursePage(@RequestParam(value = "page", defaultValue = "1") int page,
                                           @RequestParam(value = "size", defaultValue = "10") int size );

    @GetMapping("/search")
    public ResponseEntity<?> findCourse(@RequestParam(value = "keyword", defaultValue = "") String keyword,
                                        @RequestParam(value = "category", required = false) String category,
                                        @RequestParam(value = "price", required = false) BigDecimal price,
                                        @RequestParam(value = "page", defaultValue = "1") int page);

    //GET OWN COURSES
    @GetMapping({"/",""})
    public ResponseEntity<?> goCourses(@RequestParam("courseCode") String coursesCode);

    //POST
    @PostMapping({"","/"})
    @PreAuthorize("hasAuthority('teacher_change')")
    public ResponseEntity<?> createCourse(@RequestBody CoursesDto coursesDto);

    @PostMapping({"/registration/{id}"})
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public ResponseEntity<?> enrollCourse(@PathVariable("id") Long idCourse);

    //Update
    @PatchMapping({"/{id}"})
    @Caching(
            evict = {
                    @CacheEvict(value = "course_course", key ="ById_'+#id"),
                    @CacheEvict(value = "course_course", key = "all")
            }
    )
    @PreAuthorize("hasAnyRole('ROLE_TEACHER','ROLE_ADMIN')")
    public ResponseEntity<?> updateCourse(@RequestBody CoursesDto coursesDto, @PathVariable("id") Long id);







}
