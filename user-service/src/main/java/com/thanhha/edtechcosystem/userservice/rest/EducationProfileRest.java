package com.thanhha.edtechcosystem.userservice.rest;

import com.thanhha.edtechcosystem.userservice.dto.EducationProfileDto;
import com.thanhha.edtechcosystem.userservice.dto.UpdateProfileRequest;
import com.thanhha.edtechcosystem.userservice.model.EducationProfile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/users/education-profile")
public interface EducationProfileRest {
    @GetMapping({"/",""})
    //User
    public ResponseEntity<?> getMyProfile();

    @GetMapping({"/all"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAll(

    );
    @GetMapping({"/{id}"})
    //User
    public ResponseEntity<?> getProfileById(@PathVariable("id") Long id);

    //    @PostMapping({"/{id}"})
    //    public
    //Khi user duoc tao co role la student thi se tu dong tao profile

//    @PatchMapping({"","/"})
//    public ResponseEntity<?> updateProfileByUser(@RequestBody EducationProfileDto profileDto);
//    Đối với hoof sơ

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateProfileByAdmin(@RequestBody UpdateProfileRequest profileDto, @PathVariable("id") Long idProfile);

    // DELETE: It truong hop duoc phep xoa profile. Du la admin.... Nhung admin : delete ... thi nen ??
}
