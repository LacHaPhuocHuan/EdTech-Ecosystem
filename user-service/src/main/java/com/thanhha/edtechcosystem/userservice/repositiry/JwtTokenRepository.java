package com.thanhha.edtechcosystem.userservice.repositiry;

import com.thanhha.edtechcosystem.userservice.model.JwtToken;
import com.thanhha.edtechcosystem.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {
//    List<JwtToken> findAllByDisableIs(boolean b);

    List<JwtToken> findAllByDisableIsAndUser(boolean b, User user);

    Optional<JwtToken> findByToken(String token);
}
