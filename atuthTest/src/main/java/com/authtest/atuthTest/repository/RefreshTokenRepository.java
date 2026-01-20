package com.authtest.atuthTest.repository;

import com.authtest.atuthTest.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    public Optional<RefreshToken> findByJti(String jti);
}