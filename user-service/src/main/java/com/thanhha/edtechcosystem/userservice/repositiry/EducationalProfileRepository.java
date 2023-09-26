package com.thanhha.edtechcosystem.userservice.repositiry;

import com.thanhha.edtechcosystem.userservice.model.EducationProfile;
import com.thanhha.edtechcosystem.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EducationalProfileRepository  extends JpaRepository<EducationProfile, Long> {
    Optional<EducationProfile> findByUserId(String userId);

    Optional<EducationProfile> findByUserEmail(String email);
}
