package com.thanhha.edtechcosystem.courseservice.security;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.function.Function;

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
        //auto them [] nen phai cat chuoi
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.substring(1,role.length()-1)));
        return authorities;
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
