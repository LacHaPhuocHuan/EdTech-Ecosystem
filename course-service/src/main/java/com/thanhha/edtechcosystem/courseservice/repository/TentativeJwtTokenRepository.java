package com.thanhha.edtechcosystem.courseservice.repository;

import com.thanhha.edtechcosystem.courseservice.entity.JwtTokenDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TentativeJwtTokenRepository extends JpaRepository<JwtTokenDto, Long> {
    JwtTokenDto findByToken(String token);
}
