package com.thanhha.edtechcosystem.userservice.repositiry;

import com.thanhha.edtechcosystem.userservice.model.CourseTaken;
import com.thanhha.edtechcosystem.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CourseTakenRepository extends JpaRepository<CourseTaken, Long> {
    List<CourseTaken> findByUser(User user);

    boolean existsByUserIdAndIdCourse(String id, Long idCourse);
}
