package com.thanhha.edtechcosystem.courseservice.service;

import com.thanhha.edtechcosystem.courseservice.dto.LectureDto;
import com.thanhha.edtechcosystem.courseservice.dto.LectureVideoDto;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ILectureVideoService {
    LectureVideoDto getById(Long id);


    LectureVideoDto store(MultipartFile file,String id);

    Resource loadAsResource(String filename);
}
