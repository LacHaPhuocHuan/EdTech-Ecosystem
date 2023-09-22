package com.thanhha.edtechcosystem.userservice.service.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thanhha.edtechcosystem.userservice.dto.AdditionalUserRequest;
import com.thanhha.edtechcosystem.userservice.dto.UpdateUserRequest;
import com.thanhha.edtechcosystem.userservice.dto.UserDto;
import com.thanhha.edtechcosystem.userservice.dto.UserPage;
import com.thanhha.edtechcosystem.userservice.model.EducationProfile;
import com.thanhha.edtechcosystem.userservice.model.EducationalLevel;
import com.thanhha.edtechcosystem.userservice.model.EducationalStatus;
import com.thanhha.edtechcosystem.userservice.model.User;
import com.thanhha.edtechcosystem.userservice.repositiry.EducationalProfileRepository;
import com.thanhha.edtechcosystem.userservice.repositiry.UserRepository;
import com.thanhha.edtechcosystem.userservice.security.MyUserDetailsService;
import com.thanhha.edtechcosystem.userservice.security.Role;
import com.thanhha.edtechcosystem.userservice.service.UserService;
import com.thanhha.edtechcosystem.userservice.utils.DateUtils;
import com.thanhha.edtechcosystem.userservice.utils.RedisUtils;
import com.thanhha.edtechcosystem.userservice.utils.ValidatedUtils;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ServerErrorException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EducationalProfileRepository educationalProfileRepository;
    private final ModelMapper modelMapper;
    private final RedisUtils redisUtils;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ValidatedUtils validatedUtils;

    @Override
    public UserDto getUserById(String id) {
        UserDto userInfo;
        List<?> userOnCache= (List<?>) redisUtils.getDataFromCache("user_data");
        List<UserDto> userList;
        if( userOnCache !=null) {
            log.info("System using data on cache.");
            userList= (List<UserDto>) userOnCache.stream()
                    .map(element-> (UserDto) element )
                    .toList();
            userInfo=userList.stream().filter(userDto -> userDto.getId().equals(id)).findFirst()
                    .orElseThrow(()-> new ServerErrorException("Server arisen problem", 500) );
        }else {
            var user = userRepository.findById(id).orElseThrow(() -> new ServerErrorException("Server arisen problem", 500));
            userInfo = modelMapper.map(user, UserDto.class);
            try {
                userInfo.setEducationProfileId(user.getEducationProfile().getId());
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return userInfo;
    }

    @Override
    public UserPage getAllUser(int page, int size)  {
        List<?> userOnCache= (List<?>) redisUtils.getDataFromCache("user_data");
        List<UserDto> userList;
        if( userOnCache !=null && !userOnCache.isEmpty()) {
            redisUtils.setTTL("user_data", 60*60*24);
            log.info("Using data on cache.");
             userList= (List<UserDto>) userOnCache.stream()
                    .map(element-> (UserDto) element )
                    .toList();
             if(!userList.isEmpty())
                return new UserPage(userList,page,size);
        }
        var userListOnDB=userRepository.findAll();
        userList=userListOnDB.stream()
                .map(user ->{
                    var mapUser=modelMapper.map(user, UserDto.class);
                    try {
                        mapUser.setEducationProfileId(user.getEducationProfile().getId());
                    }catch (Exception e){
                        log.error(e.getMessage());
                    }
                    return mapUser;
                }).collect(Collectors.toList());
        redisUtils.putDataInCache("user_data", userList);


        return new UserPage(userList,page,size);
    }


    @Override
    public UserDto addUser(AdditionalUserRequest additionalUser)  {
        if(!validAdditionalUserRequest(additionalUser)){
            throw new IllegalArgumentException(" The user information is invalid.");
        }
        List<UserDto> userDtoList=getAllUser(1, 100).getUserDtoList();
        if(
                userDtoList.stream().anyMatch(userDto -> userDto.getEmail().trim().equals(additionalUser.getEmail()))
        )
            throw new BadRequestException("Email existed!");
        var user=modelMapper.map(additionalUser,User.class);
        user.setPassword(passwordEncoder.encode(DateUtils.convertDateToString(user.getBirthDate())));
        user.setCreateAt(new Date());
        user.setIsNonExpired(true);
        user.setIsNonClock(true);
        user.setId(UUID.randomUUID().toString());
        userRepository.save(user);
        redisUtils.evictDataFromCache("user_data");
        if(user.getRole().equals(Role.STUDENT)) {
            var educationProfile=EducationProfile.builder()
                    .user(user)
                    .status(EducationalStatus.NOT)
                    .GPA(0.0F)
                    .level(EducationalLevel.DIAGRAM)
                    .build();
            user.setEducationProfile(educationProfile);
            userRepository.save(user);
            redisUtils.evictDataFromCache("edu_profile_data");
            educationalProfileRepository.save(educationProfile);

        }

        redisUtils.evictDataFromCache("user_data");
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto updateUser(UpdateUserRequest updateUser, String idUser) {
        var userFindOnDB=userRepository.findById(idUser).orElseThrow(()-> new ServerErrorException("Server arisen problem.", 500));
        if(!Objects.isNull(updateUser.getBirthDate()))
            userFindOnDB.setBirthDate(updateUser.getBirthDate());
        if(!Objects.isNull(updateUser.getLastname()))
            userFindOnDB.setLastname(updateUser.getLastname());
        if(!Objects.isNull(updateUser.getFirstname()))
            userFindOnDB.setFirstname(updateUser.getFirstname());
        var updatedUser=userRepository.save(userFindOnDB);
        redisUtils.evictDataFromCache("user_data");
        return modelMapper.map(updatedUser, UserDto.class);
    }

    @Override
    public UserDto deactivateAccount(String idUser) {
        var userFindInDB=userRepository.findById(idUser).orElseThrow(()->new UsernameNotFoundException("User don't exist."));
        userFindInDB.setIsNonClock(false);
        redisUtils.evictDataFromCache("user_data");
        return modelMapper.map(userRepository.save(userFindInDB), UserDto.class);
    }

    @Override
    public UserDto deactivateMyAccount(String id, String password) {

        var userFindInDB=userRepository.findById(id).orElseThrow(()->new ServerErrorException("Server arisen problem.", 500));
        if(!passwordEncoder.encode(password).equals(userFindInDB.getPassword()))
            throw new BadRequestException("Password don't correct");
        userFindInDB.setIsNonClock(false);
        userRepository.save(userFindInDB);
        return modelMapper.map(userRepository.save(userFindInDB), UserDto.class);
    }

    @Override
    public void resetPassword(String oldPassword, String newPassword, String confirmPassword, String idUser) {
        var userFoundInDB=userRepository.findById(idUser).orElseThrow(()->new ServerErrorException("Server arisen problem.", 500));
        log.info("Password: {}", userFoundInDB.getPassword());
        if(!passwordEncoder.matches(oldPassword,userFoundInDB.getPassword()))
            throw new BadRequestException("Password is incorrect");
        if(!validPassword(newPassword, confirmPassword))
            throw new IllegalArgumentException("Password is invalid.");
        userFoundInDB.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userFoundInDB);
        return;
    }

    private boolean validPassword(String newPassword, String confirmPassword) {
        return validatedUtils.validPassword(newPassword) && newPassword.equals(confirmPassword);

    }

    private boolean validAdditionalUserRequest(AdditionalUserRequest additionalUser) {

        return !Objects.isNull(additionalUser.getBirthDate()) &&
                !Objects.isNull(additionalUser.getRole()) &&
                !Objects.isNull(additionalUser.getEmail()) &&
                !Objects.isNull(additionalUser.getLastname());
    }



}
