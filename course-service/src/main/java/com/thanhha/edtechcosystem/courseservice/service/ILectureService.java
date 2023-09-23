package com.thanhha.edtechcosystem.courseservice.service;

import com.thanhha.edtechcosystem.courseservice.dto.LectureDto;

import java.util.List;

public interface ILectureService {
    LectureDto getById(String id);

    List<LectureDto> getLectureByCourse(Long idCourse);

    LectureDto createLecture(LectureDto lectureDto);

    LectureDto updateLecture(LectureDto lectureDto, String id);
}
