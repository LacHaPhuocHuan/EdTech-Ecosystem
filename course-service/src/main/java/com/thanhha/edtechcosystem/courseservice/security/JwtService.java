package com.thanhha.edtechcosystem.courseservice.security;



import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
@Slf4j
@Service
public class JwtService {
    private static final String SECRET="2a741e7be5b86880249134af3700ab8b95d9125125ecb4374d99cd5690a821ba";
    private String username;
    public Claims extractAllClaims(String token){
        return Jwts.parser().setSigningKey(SECRET)
                .parseClaimsJws(token).getBody();
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimResole){
        final Claims claims=extractAllClaims(token);
        return claimResole.apply(claims);
    }
    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }
    public String extractUsername(String token){
        username=extractClaim(token, Claims::getSubject);
        return username;
    }

    public Collection<? extends GrantedAuthority> extractAuth(String token) {
        Claims claims = extractAllClaims(token);
        String role = claims.get("Authorization").toString();
        log.info(role);
        String[] cutAuthorities=role.split("authority");
        List<String> cutAuthoritieList= Arrays.stream(cutAuthorities).toList();

        //auto them [] nen phai cat chuoi

        return cutAuthoritieList.stream()
                .filter(string -> string.contains("="))
                .map(string -> {
                    var str="";
                    if(string.contains("]"))
                        str= string.substring(1,string.length()-2);
                    else
                        str= string.substring(1,string.length()-4);
                    return new SimpleGrantedAuthority(str);
                })
                .toList();
    }
    public String generatedClaim(String username, Collection<? extends GrantedAuthority> claims){
        return Jwts.builder()
                .claim("Authorization",claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration((new Date(System.currentTimeMillis() +60*60*24*10*1000)))
                .signWith(SignatureAlgorithm.HS256, SECRET).compact();

    }
    public Boolean validateToken(String token, UserDetails userDetails){
        final String username= extractUsername(token);
        return username.equals(userDetails.getUsername());
    }

    public String getUsername(){return username;}


}
