package com.verification.emailverification.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.verification.emailverification.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User>findByEmail(String email);

}
