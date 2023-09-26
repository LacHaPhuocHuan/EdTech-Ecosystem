package com.thanhha.edtechcosystem.userservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thanhha.edtechcosystem.userservice.dto.EducationProfileDto;
import com.thanhha.edtechcosystem.userservice.dto.UpdateProfileRequest;
import com.thanhha.edtechcosystem.userservice.model.EducationProfile;
import com.thanhha.edtechcosystem.userservice.model.User;

import java.util.List;

public interface EducationProfileService {
    EducationProfileDto getMyProfile();

    List<EducationProfileDto> getAllProfile() ;

    EducationProfileDto getProfileById(Long id);

    EducationProfileDto updateByAdmin(UpdateProfileRequest profileDto, Long id);
}
