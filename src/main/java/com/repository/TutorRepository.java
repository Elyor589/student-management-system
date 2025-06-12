package com.repository;

import com.entity.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TutorRepository extends JpaRepository<Tutor, String> {
    Optional<Tutor> findByTutorId(String tutorId);

    Optional<Tutor> findByUsername(String username);

    Optional<Tutor> findByPhoneNumber(String phoneNumber);
}
