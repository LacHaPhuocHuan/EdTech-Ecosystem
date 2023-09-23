package com.thanhha.edtechcosystem.courseservice.security;

import com.thanhha.edtechcosystem.courseservice.repository.TentativeJwtTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class JWTAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final TentativeJwtTokenRepository jwtTokenRepository;
    @Override
    protected void doFilterInternal(
            @NonNull  HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException
    {

        //phase 01
        var contextPath=request.getServletPath();
        log.info("Request to path : {}", contextPath);
        var authToken=request.getHeader("Authorization");
        if(authToken==null || !authToken.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }
        var token=authToken.substring(7);
        var username=jwtService.extractUsername(token);
        if(
                username!=null
                && SecurityContextHolder.getContext().getAuthentication()==null
        ){
            var jwtToken=jwtTokenRepository.findByToken(token);
            Collection<? extends GrantedAuthority> authorities=jwtService.extractAuth(token);

            log.info("Authority : {} \n Size: {}", authorities.toString(), authorities.size());

            if(!jwtToken.isDisable() &&
                    Duration.between(Instant.now(),jwtToken.getExpireDate()).toMillis()>0
             ){
                UsernamePasswordAuthenticationToken authenticationToken=
                        new UsernamePasswordAuthenticationToken(
                                username, null, authorities
                        );
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                log.info("Authentication Jwt is successful with role {}", username);
            }
        }

        //end

        filterChain.doFilter(request, response);

    }
}
