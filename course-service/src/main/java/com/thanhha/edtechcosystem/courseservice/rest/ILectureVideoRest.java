package com.thanhha.edtechcosystem.courseservice.rest;

import com.thanhha.edtechcosystem.courseservice.dto.LectureDto;
import com.thanhha.edtechcosystem.courseservice.dto.LectureVideoDto;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api/v1/courses/video")
public interface ILectureVideoRest {
    //GET
    @GetMapping("/{id}")
    public ResponseEntity<?> getVideo(@PathVariable("id") Long id);

    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @PostMapping("/{idLecture}")
    public ResponseEntity<?> uploadVideo( @RequestParam("file") MultipartFile file,@PathVariable("idLecture") String id);

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) ;


}
