package com.thanhha.edtechcosystem.userservice.service.serviceImpl;

import com.thanhha.edtechcosystem.userservice.dto.CourseTakenDto;
import com.thanhha.edtechcosystem.userservice.model.CourseTaken;
import com.thanhha.edtechcosystem.userservice.repositiry.CourseTakenRepository;
import com.thanhha.edtechcosystem.userservice.repositiry.UserRepository;
import com.thanhha.edtechcosystem.userservice.service.CourseTakenService;
import com.thanhha.edtechcosystem.userservice.utils.RedisUtils;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseTakenServiceImpl implements CourseTakenService {
    private final CourseTakenRepository courseTakenRepository;
    private final RedisUtils redisUtils;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    @Override
    public List<CourseTakenDto> getPage(int page, int size) {
        List<CourseTakenDto> courses=getOnCache();
        if(courses==null){
            courses=courseTakenRepository
                    .findAll().stream()
                    .map(courseTaken -> modelMapper.map(courseTaken, CourseTakenDto.class))
                    .toList();
            redisUtils.putDataInCache("course_data", courses);
        }

        return getToPage(page,size,courses);
    }

    @Override
    public CourseTakenDto findById(Long idCourseTaken) {
        List<CourseTakenDto> courses=getOnCache();
        if(courses!=null){
            if(courses.stream().anyMatch(courseTakenDto -> courseTakenDto.getIdCourse().equals(idCourseTaken)))
                return courses.stream().filter(courseTakenDto -> courseTakenDto.getIdCourse().equals(idCourseTaken)).findFirst().orElseThrow(NotFoundException::new);
        }
        var course=courseTakenRepository.findById(idCourseTaken).orElseThrow(NotFoundException::new);
        return modelMapper.map(course, CourseTakenDto.class);
    }

    @Override
    public List<CourseTakenDto> getPageByIdUser(int page, int size, String id) {
        List<CourseTakenDto> courses=getOnCache();
        if(courses==null){
            courses=courseTakenRepository
                    .findByUser(userRepository.findById(id).orElseThrow(NotFoundException::new)).stream()
                    .map(courseTaken -> modelMapper.map(courseTaken, CourseTakenDto.class))
                    .toList();
            redisUtils.putDataInCache("course_data", courses);
        }else{
            courses=courses.stream().filter(courseTakenDto -> courseTakenDto.getIdUser().equals(id)).toList();
        }
        return getToPage(page,size,courses);
    }

    @Override
    public CourseTakenDto createTakenCourse(CourseTakenDto courseTakenDto) {
        var user=userRepository.findById(courseTakenDto.getIdUser()).orElseThrow(()->new NotFoundException("Id user don't correct"));
        var courseTaken=modelMapper.map(courseTakenDto, CourseTaken.class);
        courseTaken.setUser(user);
        var addedCourse=courseTakenRepository.save(courseTaken);
        return modelMapper.map(addedCourse, CourseTakenDto.class);
    }

    private List<CourseTakenDto> getToPage(int page, int size, List<CourseTakenDto> courses) {
        int startNumber=page*size-size;
        int endNumber=page*size-1;
        log.info("Starting Number is {}", startNumber);
        log.info("Ending Number is {}", endNumber);
        if(startNumber>=courses.size())
            return new ArrayList<>();
        if(endNumber>=courses.size())
            endNumber=courses.size()-1;
        return toPage(startNumber,endNumber,courses, new ArrayList<CourseTakenDto>());
    }

    private List<CourseTakenDto> toPage(int startNumber, int endNumber, List<CourseTakenDto> courses, ArrayList<CourseTakenDto> courseTakenDtos) {
        if(startNumber==endNumber){
             courseTakenDtos.add(courses.get(startNumber));
             return courseTakenDtos;
        }
        if(startNumber<endNumber)
            courseTakenDtos.add(courses.get(startNumber));
        if(startNumber>endNumber)
            return new ArrayList<>();
        return toPage(startNumber+1,endNumber,courses,courseTakenDtos);
    }

    private List<CourseTakenDto> getOnCache() {
        List<?> dataOnCache = (List<?>) redisUtils.getDataFromCache("course_data");
        if(dataOnCache!=null)
            return dataOnCache.stream().map(data->(CourseTakenDto) data).toList();
        return null;
    }
}
