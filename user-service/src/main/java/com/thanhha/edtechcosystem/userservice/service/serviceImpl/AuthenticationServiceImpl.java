package com.thanhha.edtechcosystem.userservice.service.serviceImpl;

import com.thanhha.edtechcosystem.userservice.dto.AuthenticatedUserRequest;
import com.thanhha.edtechcosystem.userservice.dto.JwtTokenDto;
import com.thanhha.edtechcosystem.userservice.dto.RegisterUserRequest;

import com.thanhha.edtechcosystem.userservice.dto.UserDto;
import com.thanhha.edtechcosystem.userservice.model.*;
import com.thanhha.edtechcosystem.userservice.repositiry.EducationalProfileRepository;
import com.thanhha.edtechcosystem.userservice.repositiry.JwtTokenRepository;
import com.thanhha.edtechcosystem.userservice.repositiry.UserRepository;
import com.thanhha.edtechcosystem.userservice.security.JwtService;
import com.thanhha.edtechcosystem.userservice.security.MyUserDetailsService;
import com.thanhha.edtechcosystem.userservice.security.Role;
import com.thanhha.edtechcosystem.userservice.service.AuthenticationService;
import com.thanhha.edtechcosystem.userservice.utils.KafkaUtils;
import com.thanhha.edtechcosystem.userservice.utils.RedisUtils;
import com.thanhha.edtechcosystem.userservice.utils.ValidatedUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.ServerErrorException;
import jakarta.ws.rs.core.Response;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final ValidatedUtils validatedUtils;
    private final ModelMapper modelMapper;
    private final JwtTokenRepository jwtTokenRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final MyUserDetailsService userService;
    private final EducationalProfileRepository educationalProfileRepository;
    private final RedisUtils redisUtils;
    private final KafkaUtils kafkaUtils;
    @Override
    public String signUp(RegisterUserRequest registerUser)  {
        if(validRegisterUser(registerUser))
                throw new BadCredentialsException("Email and password don't enclosed! ");
        var user=convertToUserSave(registerUser);
        var userSave=userRepository.save(user);
        redisUtils.evictDataFromCache("user_data");
        if(user.getRole().equals(Role.STUDENT)) {
            var educationProfile= EducationProfile.builder()
                    .user(user)
                    .status(EducationalStatus.NOT)
                    .level(EducationalLevel.DIAGRAM)
                    .build();
            user.setEducationProfile(educationProfile);
            redisUtils.evictDataFromCache("edu_profile_data");
            userRepository.save(user);
            educationalProfileRepository.save(educationProfile);
        }
        try {
            var jwtToken=extractTokenByUser(userSave);
            var jwtTokenSave = jwtTokenRepository.save(jwtToken);
            JwtTokenDto jwtTokenDto= modelMapper.map(jwtTokenSave, JwtTokenDto.class);
            kafkaUtils.send("new-user", modelMapper.map(user, UserDto.class));
            kafkaUtils.send("auth-token",jwtTokenDto);
            return jwtTokenSave.getToken();
        }catch (
                Exception e
        ){
            userRepository.delete(userSave);
            e.printStackTrace();
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    private JwtToken extractTokenByUser(User userSave) {
        return JwtToken.builder()
                .token(jwtService.generatedClaim(userSave.getEmail(), userSave.getRole().getAuthority()))
                .disable(false)
                .user(userSave)
                .expireDate(Instant.now().plusSeconds(100000))
                .build();
    }

    private User convertToUserSave(RegisterUserRequest registerUser) {
        User user=modelMapper.map(registerUser, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setId(UUID.randomUUID().toString());
        user.setCreateAt(new Date());
        if (user.getRole()==null){
            if(registerUser.getRole().equals("student"))
                user.setRole(Role.STUDENT);
            else if (registerUser.getRole().equals("teacher"))
                user.setRole(Role.TEACHER);
            else
                throw new BadCredentialsException("Role is null!");
        }
        user.setIsNonClock(true);
        user.setIsNonExpired(true);
        log.info("Role is set {}", user.getRole().name());
        return user;
    }

    private boolean validRegisterUser(RegisterUserRequest registerUser)  {
        log.info("{} {} {} {} {}",userRepository.existsByEmail(registerUser.getEmail())
                , !validatedUtils.validPassword(registerUser.getPassword())
                ,!validatedUtils.validEmail(registerUser.getEmail()), registerUser.getEmail()
                , !registerUser.getPassword().equals(registerUser.getConfirmPassword()));
       return userRepository.existsByEmail(registerUser.getEmail())
        || !validatedUtils.validPassword(registerUser.getPassword())
                || !validatedUtils.validEmail(registerUser.getEmail())
                || !registerUser.getPassword().equals(registerUser.getConfirmPassword());

    }

    @Override
    public String signIn(AuthenticatedUserRequest authenticatedUser)  {
        Authentication authentication;
        try {
            authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(authenticatedUser.getEmail(), authenticatedUser.getPassword())
                    );
        }catch (Exception e){
            log.error(e.getMessage());
            throw new InternalServerErrorException("Server arisen bug");
        }
        if (authentication==null)
            log.debug("Authentication failed: no credentials provided");
        else if(authentication.isAuthenticated())
        {
            var user= userService.getCurrentUser();
            JwtToken jwtToken= extractTokenByUser(user);
            clockJwtOtherByUser(user);
            var jwtTokenSave = jwtTokenRepository.save(jwtToken);
            JwtTokenDto jwtTokenDto= modelMapper.map(jwtTokenSave, JwtTokenDto.class);
            kafkaUtils.send("auth-token",jwtTokenDto);
            return jwtTokenSave.getToken();
        }
        throw new InternalServerErrorException("Server arisen bug");
    }

    @Override
    public String refreshToken(HttpServletRequest request) {
        String authToken=request.getHeader("Authorization");
        if( authToken == null || !authToken.startsWith("Bearer"))
            throw new BadCredentialsException("Don't see token to refresh.");
        var token=authToken.substring(7);
        var jwtToken=jwtTokenRepository.findByToken(token).orElseThrow(()->new BadCredentialsException("Token don't correct."));
        User userOnToken=jwtToken.getUser();
        String refreshingJwtToken=jwtService.generatedRefreshToken(userOnToken);
        clockJwtOtherByUser(userOnToken);
        JwtToken jwtTokenSave=jwtTokenRepository.save(
                JwtToken.builder()
                        .token(refreshingJwtToken)
                        .disable(false)
                        .user(userOnToken)
                        .expireDate(Instant.now().plusSeconds(10000))
                .build()
        );
        JwtTokenDto jwtTokenDto= modelMapper.map(jwtTokenSave, JwtTokenDto.class);
        kafkaUtils.send("auth-token",jwtTokenDto);
        return jwtTokenSave.getToken();
    }

//    private boolean validAuthenticateUser(AuthenticatedUserRequest authenticatedUser) {
//        return validatedUtils.validEmail(authenticatedUser.email) && validatedUtils.validPassword(authenticatedUser.getPassword());
//    }

    private void clockJwtOtherByUser(User user){
        List<JwtToken> jwtTokenList=jwtTokenRepository.findAllByDisableIsAndUser(false, user);
        log.info("Count of Tokens is enable {}", jwtTokenList.size());
        var jwtTokenClocked=jwtTokenList.stream()
                .peek(jwt-> jwt.setDisable(true))
                .toList();
        jwtTokenRepository.saveAll(jwtTokenClocked);
    }


}
