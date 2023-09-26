package com.thanhha.edtechcosystem.userservice.service;

import com.thanhha.edtechcosystem.userservice.dto.CourseTakenDto;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public interface CourseTakenService {
    @Cacheable(value = "user_takenCourse", key = "'Page_'+#page+#size")
    List<CourseTakenDto> getPage(int page, int size);

    CourseTakenDto findById(Long idCourseTaken);

    List<CourseTakenDto> getPageByIdUser(int page, int size, String id);

    CourseTakenDto createTakenCourse(CourseTakenDto courseTakenDto,String idUser);
}
