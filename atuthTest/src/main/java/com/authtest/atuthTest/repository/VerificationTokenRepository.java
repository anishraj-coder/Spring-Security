package com.authtest.atuthTest.repository;

import com.authtest.atuthTest.entities.AppUser;
import com.authtest.atuthTest.entities.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, UUID> {
    Optional<VerificationToken> findByUser(AppUser user);
    Optional<VerificationToken> findByToken(String token);
}