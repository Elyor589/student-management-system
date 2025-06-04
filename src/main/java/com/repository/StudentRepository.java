package com.repository;

import com.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, String> {
    Optional<StudentEntity> findByStudentId(String studentId);

    Optional<StudentEntity> findByPhoneNumber(String phoneNumber);

    Optional<StudentEntity> findByEmail(String email);
}
