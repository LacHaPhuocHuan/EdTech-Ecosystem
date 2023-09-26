package com.thanhha.edtechcosystem.userservice.service.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thanhha.edtechcosystem.userservice.dto.EducationProfileDto;
import com.thanhha.edtechcosystem.userservice.dto.UpdateProfileRequest;
import com.thanhha.edtechcosystem.userservice.model.EducationProfile;
import com.thanhha.edtechcosystem.userservice.model.EducationalStatus;
import com.thanhha.edtechcosystem.userservice.model.User;
import com.thanhha.edtechcosystem.userservice.repositiry.EducationalProfileRepository;
import com.thanhha.edtechcosystem.userservice.service.EducationProfileService;
import com.thanhha.edtechcosystem.userservice.utils.RedisUtils;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EducationProfileServiceImpl implements EducationProfileService {
    private final EducationalProfileRepository educationalProfileRepository;
    private final ModelMapper modelMapper;
    private Authentication authentication;
    @Override

    public EducationProfileDto getMyProfile() {
        authentication= SecurityContextHolder.getContext().getAuthentication();
        return findProfileByUserEmail(authentication.getPrincipal().toString());
    }
    @Cacheable(value = "user_EducationProfile_UserEmail",key = "#email")
    private EducationProfileDto findProfileByUserEmail(String email) {
        var profileOnDB= educationalProfileRepository.findByUserEmail(email).orElseThrow(()->new UsernameNotFoundException("System don't exist your education profile."));
        return modelMapper.map(profileOnDB,EducationProfileDto.class);
    }


    @Override
    @Cacheable(value = "user_EducationProfile_all",key = "'all'")
    public List<EducationProfileDto> getAllProfile()  {
        List<EducationProfile> educationProfiles=
                educationalProfileRepository.findAll();
        return educationProfiles
                .stream()
                .map(educationProfile ->
                        modelMapper.map(educationProfile, EducationProfileDto.class)
                )
                .toList();
    }

    @Override
    @Cacheable(value = "user_EducationProfile_id",key = "#id")
    public EducationProfileDto getProfileById(Long id) {
        var profileFoundOnDB=educationalProfileRepository.findById(id).orElseThrow(NotFoundException::new);
        return modelMapper.map(profileFoundOnDB, EducationProfileDto.class);
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "user_EducationProfile_id",key = "#id"),
                    @CacheEvict(value = "user_EducationProfile_all",key = "'all'"),
            }
    )
    public EducationProfileDto updateByAdmin(UpdateProfileRequest profileDto, Long id) {
        var profile=educationalProfileRepository.findById(id).orElseThrow(NotFoundException::new);
        if(profileDto.getGPA()!=null)
            if(validGPA(profileDto.getGPA()))
                profile.setGPA(profileDto.getGPA());
            else
                throw new IllegalArgumentException("GPA must between 0 and 4!");
        if(profileDto.getLevel()!=null)
            profile.setLevel(profile.getLevel());
        if(profileDto.getStatus()!=null)
            profile.setStatus(EducationalStatus.valueOf(profileDto.getStatus()));
        var updateProfile=educationalProfileRepository.save(profile);
        return modelMapper.map(updateProfile,EducationProfileDto.class);
    }

    private boolean validGPA(Float gpa) {
        return gpa>=0.0f && gpa<=4.0f;
    }


}
