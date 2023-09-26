package com.thanhha.edtechcosystem.userservice.service.serviceImpl;

import com.thanhha.edtechcosystem.userservice.dto.CourseTakenDto;
import com.thanhha.edtechcosystem.userservice.model.CourseTaken;
import com.thanhha.edtechcosystem.userservice.repositiry.CourseTakenRepository;
import com.thanhha.edtechcosystem.userservice.repositiry.UserRepository;
import com.thanhha.edtechcosystem.userservice.service.CourseTakenService;
import com.thanhha.edtechcosystem.userservice.utils.RedisUtils;
import jakarta.ws.rs.NotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Getter
public class CourseTakenServiceImpl implements CourseTakenService {
    private final CourseTakenRepository courseTakenRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private String maxPage="";





    @Override
    public List<CourseTakenDto> getPage(int page, int size) {
        List<CourseTakenDto> courses=getOnCache();

        return getToPage(page,size,courses);
    }

    @Override
    @Cacheable(value = "user_course_id", key = "#idCourseTaken")
    public CourseTakenDto findById(Long idCourseTaken) {
        var course=courseTakenRepository.findById(idCourseTaken).orElseThrow(NotFoundException::new);
        return modelMapper.map(course, CourseTakenDto.class);
    }

    @Override
    @Cacheable(value = "user_course_idUser", key = "#idUser")
    public List<CourseTakenDto> getPageByIdUser(int page, int size, String idUser) {
        List<CourseTakenDto> courses=getOnCache().stream().filter(courseTakenDto -> courseTakenDto.getIdUser().equals(idUser)).toList();
        return getToPage(page,size,courses);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "user_course_idUser", key ="#idUser"),
                    @CacheEvict(value = "user_course_all", key = "'all'"),
            }
    )
    @Override
    public CourseTakenDto createTakenCourse(CourseTakenDto courseTakenDto, String idUser) {
         deleteCache(getMaxPage());
        var user=userRepository.findById(courseTakenDto.getIdUser()).orElseThrow(()->new NotFoundException("Id user don't correct"));
        var courseTaken=modelMapper.map(courseTakenDto, CourseTaken.class);
        courseTaken.setUser(user);
        var addedCourse=courseTakenRepository.save(courseTaken);
        return modelMapper.map(addedCourse, CourseTakenDto.class);
    }










    @CacheEvict(value = "user_takenCourse", key = "#maxPage")
    private void deleteCache(String maxPage) {
    }

    private List<CourseTakenDto> getToPage(int page, int size, List<CourseTakenDto> courses) {
        int maxPInt=(courses.size()+1)/size+1;
        var maxPStr="page_"+page+maxPInt;
        if(!maxPage.equals(maxPStr))
            maxPage=maxPStr;
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

    @Cacheable(value = "user_course_all", key = "'all'")
    private List<CourseTakenDto> getOnCache() {
        return courseTakenRepository
                .findAll().stream()
                .map(courseTaken -> modelMapper.map(courseTaken, CourseTakenDto.class))
                .toList();
    }
}
