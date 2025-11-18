package com.verification.emailverification.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.verification.emailverification.model.OtpVerification;

import java.util.Optional;

public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Long> {
    Optional<OtpVerification> findByEmail(String email);
}

