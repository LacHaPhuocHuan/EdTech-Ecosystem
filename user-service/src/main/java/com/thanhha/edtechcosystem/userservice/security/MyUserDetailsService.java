package com.thanhha.edtechcosystem.userservice.security;

import com.thanhha.edtechcosystem.userservice.model.User;
import com.thanhha.edtechcosystem.userservice.repositiry.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private User user;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         user=userRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("Email don't correct! "));

        return MyUserDetails.builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .password(user.getPassword())
                .isNonExpired(user.getIsNonExpired())
                .isNonClock(user.getIsNonClock())
                .role(user.getRole())
                .build();

    }
    public User getCurrentUser(){return user;}
}
