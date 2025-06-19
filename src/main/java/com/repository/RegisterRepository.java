package com.repository;

import com.entity.Register;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RegisterRepository extends JpaRepository<Register, UUID> {
    Optional<Register> findByUsername(String username);

    Optional<Register> findByEmail(String email);

    Optional<Register> findByPhoneNumber(String phoneNumber);
}
