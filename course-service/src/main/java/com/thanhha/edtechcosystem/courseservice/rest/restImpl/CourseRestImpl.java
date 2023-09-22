package com.thanhha.edtechcosystem.courseservice.rest.restImpl;

import com.thanhha.edtechcosystem.courseservice.dto.CoursesDto;
import com.thanhha.edtechcosystem.courseservice.dto.DataPage;
import com.thanhha.edtechcosystem.courseservice.dto.DataResponse;
import com.thanhha.edtechcosystem.courseservice.dto.EnrollmentDto;
import com.thanhha.edtechcosystem.courseservice.rest.ICourseRest;
import com.thanhha.edtechcosystem.courseservice.service.ICourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CourseRestImpl implements ICourseRest {
    private final ICourseService iCourseService;
    @Override
    public ResponseEntity<?> getCoursePage(int page, int size) {
        DataPage courses=iCourseService.getCoursePage(page,size);
        List<EntityModel<CoursesDto>> courseEntityModels=courses
                .getDataList().stream().map(o -> {
                            CoursesDto coursesDto=(CoursesDto) o;
                            return hateoas(coursesDto);
                        }
                ).toList();
        return ResponseEntity.ok(DataResponse.builder()
                .data(courseEntityModels)
                .message("Page : " +page)
                .build()
        );

    }

    @Override
    public ResponseEntity<?> findCourse(String keyword, String category, BigDecimal price, int page) {
        DataPage courses=iCourseService.findCourse(keyword,category, price, page);
        List<EntityModel<CoursesDto>> courseEntityModels=courses
                .getDataList().stream().map(o -> {
                            CoursesDto coursesDto=(CoursesDto) o;
                            return hateoas(coursesDto);
                        }
                ).toList();
        return ResponseEntity.ok(DataResponse.builder()
                .data(courseEntityModels)
                .message("Find by: "+ keyword +" "+category+" "+price
                +" \n"+"Page : " +page)
                .build()
        );

    }

    @Override
    public ResponseEntity<?> goCourses(String coursesCode) {
        CoursesDto courses=iCourseService.getCourse(coursesCode);
        EntityModel<CoursesDto> coursesDtoEntityModel=hateoas(courses);
        return ResponseEntity.ok(DataResponse.builder()
                .data(coursesDtoEntityModel)
                .message("Entry this course is successful.")
                .build()
        );
    }

    @Override
    public ResponseEntity<?> createCourse(CoursesDto coursesDto) {
        CoursesDto courses=iCourseService.createCourse(coursesDto);
        EntityModel<CoursesDto> coursesDtoEntityModel=hateoas(courses);
        return ResponseEntity.ok(DataResponse.builder()
                .data(coursesDtoEntityModel)
                .message("Course creation  is successful.")
                .build()
        );
    }

    @Override
    public ResponseEntity<?> enrollCourse(Long idCourse) {
        EnrollmentDto enrollmentDto=iCourseService.enrollCourse(idCourse);
        EntityModel<EnrollmentDto> coursesDtoEntityModel=hateoas(enrollmentDto);
        return ResponseEntity.ok(DataResponse.builder()
                .data(coursesDtoEntityModel)
                .message("Course enrollment  is successful.")
                .build()
        );
    }

    @Override
    public ResponseEntity<?> updateCourse(CoursesDto coursesDto) {
        CoursesDto courses=iCourseService.updateCourse(coursesDto);
        EntityModel<CoursesDto> coursesDtoEntityModel=hateoas(courses);
        return ResponseEntity.ok(DataResponse.builder()
                .data(coursesDtoEntityModel)
                .message("Course update  is successful.")
                .build()
        );
    }


    private <T> EntityModel<T> hateoas(T object){
        EntityModel<T> entityModel=EntityModel.of(object);
        //todo....
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ICourseRest.class).getCoursePage(1,10)).withRel("GET"));
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ICourseRest.class).createCourse(new CoursesDto())).withRel("POST"));
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ICourseRest.class).findCourse("keyword","", BigDecimal.valueOf(100.00),1)).withRel("GET"));
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ICourseRest.class).updateCourse(new CoursesDto())).withRel("PATCH"));

        return entityModel;
    }

}
