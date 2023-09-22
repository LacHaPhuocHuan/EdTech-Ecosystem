package com.thanhha.edtechcosystem.userservice.security;

import com.thanhha.edtechcosystem.userservice.repositiry.JwtTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Service
@RequiredArgsConstructor
@Slf4j
public class JWTAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final JwtTokenRepository jwtTokenRepository;
    private final  MyUserDetailsService userService;
    @Override
    protected void doFilterInternal(
            @NonNull  HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException
    {
        var contextPath=request.getContextPath();
        log.info("Request to path : {}", contextPath);
        if(contextPath.contains("/auth"))
            filterChain.doFilter(request,response);
        var authToken=request.getHeader("Authorization");
        if(authToken==null || !authToken.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }
        var token=authToken.substring(7);
        var username=jwtService.extractUsername(token);
        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            var user=userService.loadUserByUsername(username);
            var jwtToken=jwtTokenRepository.findByToken(token);

            if(jwtService.validateToken(token, user) && jwtToken.isPresent() ){
                UsernamePasswordAuthenticationToken authenticationToken=
                        new UsernamePasswordAuthenticationToken(
                                username, null, user.getAuthorities()
                        );
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                log.info("Authentication Jwt is successful with role {}", user.getAuthorities());
            }
        }
        filterChain.doFilter(request, response);

    }
}
