package com.thanhha.edtechcosystem.userservice.rest;

import com.thanhha.edtechcosystem.userservice.dto.CourseTakenDto;
import jakarta.ws.rs.Path;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/users/course-taken")
public interface CourseTakenRest {
    @GetMapping({"/all"})
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    );

    @GetMapping("/info/{idCourseTaken}")
    public ResponseEntity<?> getCourseTakenById( @PathVariable("idCourseTaken") Long idCourseTaken);

    @GetMapping({"/{idUser}"})
    // PHAN TRANG:::: dung Pageable
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllByIdUser(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable("idUser") String id
    );
    @GetMapping({"/",""})
    public ResponseEntity<?> getMyCourses();

    @PostMapping("")
    //System//
    //tu dong tao
    @PreAuthorize("hasRole('ROLE_SYSTEM')")
    public ResponseEntity<?> completeCourse(CourseTakenDto courseTakenDto);


//    @PutMapping("/{idUser}/{idCourseTaken}")
//    public Res
    //Trên thực tế, Course taken là bản kết nối giữa bản user và course nên việc tạo ra thì sẽ thì không nên ược update nữa.
    // Có xuất hiện trươờng hợp thay đổi điểm __ không nên xuất hiện ?
}
