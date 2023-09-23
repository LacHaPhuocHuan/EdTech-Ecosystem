package com.thanhha.edtechcosystem.courseservice.service.serviceImpl;

import com.thanhha.edtechcosystem.courseservice.dto.InstructorDto;
import com.thanhha.edtechcosystem.courseservice.repository.InstructorRepository;
import com.thanhha.edtechcosystem.courseservice.service.IInstructorService;
import com.thanhha.edtechcosystem.courseservice.utils.RedisUtils;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InstructorServiceImpl implements IInstructorService {
    private final InstructorRepository instructorRepository;
    private final RedisUtils redisUtils;
    private final ModelMapper modelMapper;
    private final String KEY_REDIS_CACHE_INSTRUCTOR="instructor_data";
    @Override
    public InstructorDto getById(String id) {
        if(redisUtils.checkExisted(KEY_REDIS_CACHE_INSTRUCTOR))
            return (InstructorDto) redisUtils.getDataFromCache(KEY_REDIS_CACHE_INSTRUCTOR+id);
        var instructor= modelMapper.map(
                instructorRepository.findById(id).orElseThrow(NotFoundException::new),
                InstructorDto.class
        );
        redisUtils.putDataInCache(KEY_REDIS_CACHE_INSTRUCTOR+id, instructor);
        return instructor;
    }
}
