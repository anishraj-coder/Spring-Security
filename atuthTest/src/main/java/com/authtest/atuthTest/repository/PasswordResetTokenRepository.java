package com.authtest.atuthTest.repository;

import com.authtest.atuthTest.entities.AppUser;
import com.authtest.atuthTest.entities.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {

    Optional<PasswordResetToken> findByUser(AppUser user);
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByToken(String token);
}