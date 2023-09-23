package com.thanhha.edtechcosystem.courseservice.service.serviceImpl;

import com.thanhha.edtechcosystem.courseservice.dto.LectureDto;
import com.thanhha.edtechcosystem.courseservice.entity.Lecture;
import com.thanhha.edtechcosystem.courseservice.repository.CourseRepository;
import com.thanhha.edtechcosystem.courseservice.repository.LectureRepository;
import com.thanhha.edtechcosystem.courseservice.service.ILectureService;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LectureServiceImpl implements ILectureService {
    private final LectureRepository lectureRepository;
    private final ModelMapper modelMapper;
    private final CourseRepository courseRepository;
    @Override
    public LectureDto getById(String id) {
//        ..todo use cache.
        return modelMapper.map(lectureRepository.findById(id).orElseThrow(NotFoundException::new), LectureDto.class);
    }

    @Override
    public List<LectureDto> getLectureByCourse(Long idCourse) {
 //        ..todo use cache.
        var lectures=lectureRepository.findByCourseId(idCourse);
        return lectures.stream().map(lecture -> modelMapper.map(lecture, LectureDto.class)).toList();
    }

    @Override
    public LectureDto createLecture(LectureDto lectureDto) {
        if(!validLecture(lectureDto))
            throw new BadRequestException("Lecture don't correct");
        var lecture=modelMapper.map(lectureDto, Lecture.class);
        lecture.setCourse(courseRepository.findById(lectureDto.getCourseId()).orElseThrow());
        var saveLecture=lectureRepository.save(lecture);
        return modelMapper.map(saveLecture,LectureDto.class);
    }

    private boolean validLecture(LectureDto lectureDto) {
        return courseRepository.existsById(lectureDto.getCourseId()) &&
                Objects.isNull(lectureDto.getTitle())
                && Objects.isNull(lectureDto.getContent());
    }

    @Override
    public LectureDto updateLecture(LectureDto lectureDto, String id) {
        var lecture=lectureRepository.findById(id).orElseThrow(()->new BadRequestException("Lecture is incorrect"));
        if(!Objects.isNull(lectureDto.getContent())&&!lecture.getContent().trim().isEmpty())
            lecture.setContent(lectureDto.getContent());
        if(!Objects.isNull(lectureDto.getTitle())&&!lecture.getTitle().trim().isEmpty())
            lecture.setTitle(lectureDto.getTitle());
        var saveLecture=lectureRepository.save(lecture);
        return modelMapper.map(saveLecture,LectureDto.class);
    }
}
