package com.authtest.atuthTest.repository;

import com.authtest.atuthTest.entities.AppUser;
import com.authtest.atuthTest.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    public Optional<RefreshToken> findByJti(String jti);
    public List<RefreshToken> findByUser(AppUser user);
}