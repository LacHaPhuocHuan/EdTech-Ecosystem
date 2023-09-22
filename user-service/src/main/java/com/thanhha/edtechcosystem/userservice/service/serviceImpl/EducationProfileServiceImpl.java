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
    private final RedisUtils redisUtils;
    @Override
    public EducationProfileDto getMyProfile(User user) {
        if(!Objects.isNull(user.getEducationProfile()))
            return modelMapper.map(user.getEducationProfile(), EducationProfileDto.class);
        return findUserById(user.getId());
    }

    private EducationProfileDto findUserById(String id) {
        List<EducationProfileDto> educationInCache = getOnCache();
        try {
            if (educationInCache != null)
                return educationInCache.stream().filter(profileDto -> profileDto.getUserId().equals(id)).findFirst().orElseThrow(Exception::new);
        }catch (Exception ignored){

        }
        var profileOnDB= educationalProfileRepository.findByUserId(id).orElseThrow(()->new UsernameNotFoundException("System don't exist your education profile."));
        return modelMapper.map(profileOnDB,EducationProfileDto.class);
    }

    @Override
    public List<EducationProfileDto> getAllProfile()  {
        List<EducationProfileDto> educationProfileDtoList=getOnCache();
        if(educationProfileDtoList!=null)
            return educationProfileDtoList;
        List<EducationProfile> educationProfiles=
                educationalProfileRepository.findAll();
        educationProfileDtoList=
                educationProfiles
                        .stream()
                        .map(educationProfile ->
                                modelMapper.map(educationProfile, EducationProfileDto.class)
                        )
                        .toList();
        redisUtils.putDataInCache("edu_profile_data", educationProfileDtoList);
        redisUtils.setTTL("edu_profile_data", 60*60*24);
        return educationProfileDtoList;
    }

    @Override
    public EducationProfileDto getProfileById(Long id) {
        var educationProfileDtoList=getOnCache();
        if(educationProfileDtoList!=null){
            try {
                return educationProfileDtoList.stream().filter(profileDto -> profileDto.getId().equals(id)).findFirst().orElseThrow(Exception::new);
            } catch (Exception ignored) {

            }
        }
        var profileFoundOnDB=educationalProfileRepository.findById(id).orElseThrow(NotFoundException::new);
        return modelMapper.map(profileFoundOnDB, EducationProfileDto.class);
    }

    @Override
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
        redisUtils.evictDataFromCache("edu_profile_data");
        return modelMapper.map(updateProfile,EducationProfileDto.class);
    }

    private boolean validGPA(Float gpa) {
        return gpa>=0.0f && gpa<=4.0f;
    }

    private List<EducationProfileDto> getOnCache(){
        List<EducationProfileDto> educationProfileDtoList;
        try {
            List<?> educationInCache = (List<?>) redisUtils.getDataFromCache("edu_profile_data");
            if (educationInCache != null)
                if (!educationInCache.isEmpty()) {
                    log.info("Using data on cache which name profile_data ");
                    educationProfileDtoList =
                            educationInCache.stream().map(profile ->
                                         (EducationProfileDto) profile
                                    ).toList();
                    return educationProfileDtoList;
                }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
